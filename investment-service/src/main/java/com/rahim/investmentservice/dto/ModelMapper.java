package com.rahim.investmentservice.dto;

import com.rahim.investmentservice.model.Investment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    Investment toInvestmentEntity(InvestmentRequestDto investmentDto);

}
