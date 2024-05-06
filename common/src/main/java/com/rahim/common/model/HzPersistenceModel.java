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
    private HzSetData setData;
    private HzMapData mapData;
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

    public static HzPersistenceModel createSetPersistenceModel(String setName, Object setValue, ObjectOperation operation) {
        HzSetData setData = new HzSetData(setName, setValue);
        return HzPersistenceModel.builder()
                .objectType(ObjectType.SET)
                .setData(setData)
                .objectOperation(operation)
                .build();
    }

    public static HzPersistenceModel createMapPersistenceModel(String mapName, String mapKey, Object mapValue, ObjectOperation operation) {
        HzMapData mapData = new HzMapData(mapName, mapKey, mapValue);
        return HzPersistenceModel.builder()
                .objectType(ObjectType.MAP)
                .mapData(mapData)
                .objectOperation(operation)
                .build();
    }
}
