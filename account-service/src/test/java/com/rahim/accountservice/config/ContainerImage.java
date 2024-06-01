package com.rahim.accountservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.testcontainers.utility.DockerImageName;

@Getter
@AllArgsConstructor
public enum ContainerImage {
    POSTGRES("postgres:latest"),
    KAFKA("confluentinc/cp-kafka:6.2.1"),
    HAZELCAST("hazelcast/hazelcast:latest");

    private final String imageName;

    public DockerImageName getDockerImageName() {
        return DockerImageName.parse(imageName);
    }
}

