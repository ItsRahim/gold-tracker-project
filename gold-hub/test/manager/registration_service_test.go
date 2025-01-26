package manager

import (
	"gold-hub/pkg/manager"
	pb "gold-hub/pkg/protos/manager"
	"google.golang.org/grpc"
	"google.golang.org/grpc/test/bufconn"
	"log"
	"testing"
)

func server(t *testing.T) {
	lis := bufconn.Listen(1024 * 1024)
	t.Cleanup(func() {
		lis.Close()
	})

	srv := grpc.NewServer()
	t.Cleanup(func() {
		srv.Stop()
	})

	svc := manager.RegistrationServer{}
	pb.RegisterRegistrationServer(srv, &svc)

	go func() {
		if err := srv.Serve(lis); err != nil {
			log.Fatalf("srv.Serv %v", err)
		}
	}()
}
