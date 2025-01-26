package utils

import (
	"encoding/json"
	"errors"
)

func Serialize(data interface{}) (string, error) {
	bytes, err := json.Marshal(data)
	if err != nil {
		return "", err
	}
	return string(bytes), nil
}

func Deserialize(jsonStr string, out interface{}) error {
	if jsonStr == "" {
		return errors.New("cannot deserialize empty string")
	}
	return json.Unmarshal([]byte(jsonStr), out)
}
