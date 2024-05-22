package com.rahim.investmentservice.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

}
