package manager

import (
	pb "gold-hub/pkg/protos/manager"
)

type Service struct {
	ServiceName       string
	Address           string
	SupportedMessages []*pb.SupportedMessage
}
