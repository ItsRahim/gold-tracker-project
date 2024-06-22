package com.rahim.userservice.request;

import com.rahim.userservice.entity.Account;
import com.rahim.userservice.entity.Profile;
import com.rahim.userservice.request.account.AccountCreationRequest;
import com.rahim.userservice.request.profile.ProfileCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * @author Rahim Ahmed
 * @created 18/05/2024
 */
@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    @Mapping(target = "password", source = "password", qualifiedByName = "stringToCharArray")
    Account toAccountEntity(AccountCreationRequest accountDto);

    Profile toProfileEntity(ProfileCreationRequest profileDto);

    @Named("stringToCharArray")
    default char[] map(String value) {
        return value.toCharArray();
    }
}
