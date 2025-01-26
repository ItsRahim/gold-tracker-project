// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.5.1
// - protoc             v5.29.1
// source: heartbeat.proto

package heartbeat

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.64.0 or later.
const _ = grpc.SupportPackageIsVersion9

const (
	Heartbeat_SendHeartbeat_FullMethodName = "/hub.Heartbeat/SendHeartbeat"
)

// HeartbeatClient is the client API for Heartbeat service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type HeartbeatClient interface {
	SendHeartbeat(ctx context.Context, in *HeartbeatRequest, opts ...grpc.CallOption) (*HeartbeatResponse, error)
}

type heartbeatClient struct {
	cc grpc.ClientConnInterface
}

func NewHeartbeatClient(cc grpc.ClientConnInterface) HeartbeatClient {
	return &heartbeatClient{cc}
}

func (c *heartbeatClient) SendHeartbeat(ctx context.Context, in *HeartbeatRequest, opts ...grpc.CallOption) (*HeartbeatResponse, error) {
	cOpts := append([]grpc.CallOption{grpc.StaticMethod()}, opts...)
	out := new(HeartbeatResponse)
	err := c.cc.Invoke(ctx, Heartbeat_SendHeartbeat_FullMethodName, in, out, cOpts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// HeartbeatServer is the server API for Heartbeat service.
// All implementations must embed UnimplementedHeartbeatServer
// for forward compatibility.
type HeartbeatServer interface {
	SendHeartbeat(context.Context, *HeartbeatRequest) (*HeartbeatResponse, error)
	mustEmbedUnimplementedHeartbeatServer()
}

// UnimplementedHeartbeatServer must be embedded to have
// forward compatible implementations.
//
// NOTE: this should be embedded by value instead of pointer to avoid a nil
// pointer dereference when methods are called.
type UnimplementedHeartbeatServer struct{}

func (UnimplementedHeartbeatServer) SendHeartbeat(context.Context, *HeartbeatRequest) (*HeartbeatResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method SendHeartbeat not implemented")
}
func (UnimplementedHeartbeatServer) mustEmbedUnimplementedHeartbeatServer() {}
func (UnimplementedHeartbeatServer) testEmbeddedByValue()                   {}

// UnsafeHeartbeatServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to HeartbeatServer will
// result in compilation errors.
type UnsafeHeartbeatServer interface {
	mustEmbedUnimplementedHeartbeatServer()
}

func RegisterHeartbeatServer(s grpc.ServiceRegistrar, srv HeartbeatServer) {
	// If the following call pancis, it indicates UnimplementedHeartbeatServer was
	// embedded by pointer and is nil.  This will cause panics if an
	// unimplemented method is ever invoked, so we test this at initialization
	// time to prevent it from happening at runtime later due to I/O.
	if t, ok := srv.(interface{ testEmbeddedByValue() }); ok {
		t.testEmbeddedByValue()
	}
	s.RegisterService(&Heartbeat_ServiceDesc, srv)
}

func _Heartbeat_SendHeartbeat_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(HeartbeatRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HeartbeatServer).SendHeartbeat(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: Heartbeat_SendHeartbeat_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HeartbeatServer).SendHeartbeat(ctx, req.(*HeartbeatRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// Heartbeat_ServiceDesc is the grpc.ServiceDesc for Heartbeat service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var Heartbeat_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "hub.Heartbeat",
	HandlerType: (*HeartbeatServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "SendHeartbeat",
			Handler:    _Heartbeat_SendHeartbeat_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "heartbeat.proto",
}
