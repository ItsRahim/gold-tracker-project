package manager

import (
	"context"
	"errors"
	"fmt"
	"github.com/redis/go-redis/v9"
	"gold-hub/internal/utils"
	"gold-hub/pkg/database"
	pb "gold-hub/pkg/protos/manager"
	"log"
	"sync"
)

type RegistrationServer struct {
	pb.UnimplementedRegistrationServer
	services     []Service
	mutex        sync.Mutex
	redisService *database.Service
}

func NewRegistrationService(redisService *database.Service) *RegistrationServer {
	return &RegistrationServer{
		redisService: redisService,
	}
}

func (server *RegistrationServer) RegisterService(ctx context.Context, request *pb.RegisterRequest) (*pb.RegisterResponse, error) {
	if err := validateRegisterRequest(request); err != nil {
		log.Println("Invalid request:", err)
		return generateRegisterResponse(pb.ServiceResponseCode_FAILURE, err.Error()), err
	}

	log.Printf("Received registration request for service: %s at address: %s", request.ServiceDetails.ServiceName, request.ServiceDetails.Address)

	server.mutex.Lock()
	defer server.mutex.Unlock()

	incomingService := Service{
		ServiceName:       request.ServiceDetails.ServiceName,
		Address:           request.ServiceDetails.Address,
		SupportedMessages: request.SupportedMessages,
	}

	if server.serviceRegistered(ctx, &incomingService.Address, &incomingService.ServiceName) {
		return generateRegisterResponse(pb.ServiceResponseCode_FAILURE, "A service is already registered at: "+incomingService.Address), nil
	}

	serviceJSON, err := utils.Serialize(incomingService)
	if err != nil {
		log.Printf("Error serializing incoming service: %v", err)
		return generateRegisterResponse(pb.ServiceResponseCode_INTERNAL_ERROR, "Failed to serialize service"), err
	}

	err = server.saveServiceToRedis(ctx, request.ServiceDetails.Address, serviceJSON)
	if err != nil {
		return generateRegisterResponse(pb.ServiceResponseCode_FAILURE, "Failed to register service in Redis"), err
	}

	log.Printf("Successfully saved to Redis: %v", incomingService)
	return generateRegisterResponse(pb.ServiceResponseCode_SUCCESS, request.ServiceDetails.ServiceName+" registered successfully"), nil
}

func (server *RegistrationServer) serviceRegistered(ctx context.Context, address *string, serviceName *string) bool {
	if address == nil || serviceName == nil {
		log.Printf("Address or service name is nil")
		return false
	}

	serviceJSON, err := server.redisService.Client.Get(ctx, *address).Result()
	if err != nil {
		if errors.Is(err, redis.Nil) {
			return false
		}
		log.Printf("Error checking service in Redis: %v", err)
		return false
	}

	var storedService Service
	err = utils.Deserialize(serviceJSON, &storedService)
	if err != nil {
		log.Printf("Error deserializing service from Redis: %v", err)
		return false
	}

	if storedService.Address == *address {
		return true
	}

	return false
}

func (server *RegistrationServer) saveServiceToRedis(ctx context.Context, address string, serviceJSON string) error {
	err := server.redisService.Client.Set(ctx, address, serviceJSON, 0).Err()
	if err != nil {
		log.Printf("Error saving to Redis: %v", err)
		return err
	}
	return nil
}

func validateRegisterRequest(request *pb.RegisterRequest) error {
	if request.ServiceDetails == nil {
		return fmt.Errorf("missing ServiceDetails")
	}

	if request.ServiceDetails.ServiceName == "" {
		return fmt.Errorf("missing ServiceName")
	}

	if request.ServiceDetails.Address == "" {
		return fmt.Errorf("missing Address")
	}

	if len(request.SupportedMessages) == 0 {
		return fmt.Errorf("missing SupportedMessages")
	}

	return nil
}

func generateRegisterResponse(status pb.ServiceResponseCode, message string) *pb.RegisterResponse {
	return &pb.RegisterResponse{
		Response: &pb.ServiceResponse{
			Status:  status,
			Message: message,
		},
	}
}
