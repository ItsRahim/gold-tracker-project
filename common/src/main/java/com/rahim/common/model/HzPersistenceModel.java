package com.rahim.common.model;

import com.rahim.common.enums.HzObjectOperation;
import com.rahim.common.enums.HzObjectType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
@Getter
@Setter
@Builder
public class HzPersistenceModel {
    private HzObjectType objectType;
    private String objectName;
    private String objectKey;
    private Object objectValue;
    private HzObjectOperation objectOperation;

}
