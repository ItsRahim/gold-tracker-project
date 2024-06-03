package com.rahim.accountservice.dto;

import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rahim Ahmed
 * @created 18/05/2024
 */
@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    Account toAccountEntity(AccountRequestDto accountDto);

    Profile toProfileEntity(ProfileRequestDto profileDto);
}
