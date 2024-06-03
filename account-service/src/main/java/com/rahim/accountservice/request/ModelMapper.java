package com.rahim.accountservice.request;

import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.entity.Profile;
import com.rahim.accountservice.request.account.AccountCreationRequest;
import com.rahim.accountservice.request.profile.ProfileCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rahim Ahmed
 * @created 18/05/2024
 */
@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);
    Account toAccountEntity(AccountCreationRequest accountDto);
    Profile toProfileEntity(ProfileCreationRequest profileDto);
}
