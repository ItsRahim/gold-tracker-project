package main

import (
	"gold-hub/internal/config"
	"gold-hub/pkg/database"
	"gold-hub/pkg/manager"
	regPb "gold-hub/pkg/protos/manager"
	"gold-hub/pkg/server"
	"google.golang.org/grpc"
)

func main() {
	grpcAddr := config.GetGRPCAddr()
	redisService := database.NewRedisService()

	registrationServer := manager.NewRegistrationService(redisService)
	server.StartGRPCServer(grpcAddr, func(s *grpc.Server) {
		regPb.RegisterRegistrationServer(s, registrationServer)
	})
}
