package server

import (
	"google.golang.org/grpc"
	"log"
	"net"
)

func StartGRPCServer(addr string, registerFunc func(*grpc.Server)) {
	listener, err := net.Listen("tcp", addr)
	if err != nil {
		log.Fatalf("Failed to listen on %s: %v", addr, err)
	}

	grpcServer := grpc.NewServer()
	registerFunc(grpcServer)

	log.Printf("Starting gRPC server on %s...", addr)
	if err := grpcServer.Serve(listener); err != nil {
		log.Fatalf("Failed to serve gRPC server: %v", err)
	}
}
