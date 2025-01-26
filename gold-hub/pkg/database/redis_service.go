package database

import (
	"context"
	"github.com/redis/go-redis/v9"
	"gold-hub/internal/config"
	"log"
)

type Service struct {
	Client *redis.Client
}

func NewRedisService() *Service {
	redisConfig := config.GetRedisConfig()

	client := redis.NewClient(&redis.Options{
		Addr:     redisConfig.Addr,
		Password: redisConfig.Password,
		DB:       redisConfig.DB,
	})

	_, err := client.Ping(context.Background()).Result()
	if err != nil {
		log.Fatalf("Failed to connect to Redis: %v", err)
		return nil
	}

	log.Println("Connected to Redis")
	return &Service{Client: client}
}
