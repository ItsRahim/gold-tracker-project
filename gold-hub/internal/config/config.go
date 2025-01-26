package config

import (
	"os"
	"strconv"
)

type Config struct {
	RedisHost string
	RedisPort string
	RedisDB   int
	RedisPass string
	GRPCHost  string
	GRPCPort  string
}

type RedisConfig struct {
	Addr     string
	Password string
	DB       int
}

func LoadConfig() *Config {
	return &Config{
		RedisHost: getEnv("REDIS_HOST", "localhost"),
		RedisPort: getEnv("REDIS_PORT", "6379"),
		RedisDB:   getEnvAsInt("REDIS_DB", 0),
		RedisPass: getEnv("REDIS_PASSWORD", ""),
		GRPCHost:  getEnv("GRPC_HOST", "localhost"),
		GRPCPort:  getEnv("GRPC_PORT", "9090"),
	}
}

func GetRedisConfig() *RedisConfig {
	config := LoadConfig()
	redisAddr := config.RedisHost + ":" + config.RedisPort
	return &RedisConfig{
		Addr:     redisAddr,
		Password: config.RedisPass,
		DB:       config.RedisDB,
	}
}

func GetGRPCAddr() string {
	config := LoadConfig()
	return config.GRPCHost + ":" + config.GRPCPort
}

func getEnv(key, defaultValue string) string {
	value := os.Getenv(key)
	if value == "" {
		return defaultValue
	}
	return value
}

func getEnvAsInt(name string, defaultValue int) int {
	valueStr := getEnv(name, "")
	if value, err := strconv.Atoi(valueStr); err == nil {
		return value
	}
	return defaultValue
}
