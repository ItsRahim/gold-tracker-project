package com.rahim.investmentservice.dto;

import com.rahim.investmentservice.model.Holding;
import com.rahim.investmentservice.model.Investment;
import com.rahim.investmentservice.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    Holding toHoldingEntity(HoldingRequestDto holdingDto);

    Investment toInvestmentEntity(InvestmentRequestDto investmentDto);

    Transaction toTransactionEntity(TranactionRequestDto transactionDto);
}
