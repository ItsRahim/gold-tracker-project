package com.rahim.userservice;

import lombok.Getter;
import org.testcontainers.utility.DockerImageName;

@Getter
public enum ContainerImage {
    POSTGRES("postgres:16.0"),
    KAFKA("confluentinc/cp-kafka:6.2.1");

    private final String imageName;

    ContainerImage(String imageName) {
        this.imageName = imageName;
    }

    public DockerImageName getDockerImageName() {
        return DockerImageName.parse(imageName);
    }
}

