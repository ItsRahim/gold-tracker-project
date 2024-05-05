package com.rahim.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
@Getter
@Setter
@Builder
@ToString
public class HzPersistenceModel {
    private ObjectType objectType;
    private String objectName;
    private String objectKey;
    private Object objectValue;
    private ObjectOperation objectOperation;

    public enum ObjectOperation {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    public enum ObjectType {
        MAP,
        SET
    }
}
