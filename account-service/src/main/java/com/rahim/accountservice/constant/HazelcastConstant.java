package com.rahim.accountservice.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 02/05/2024
 */
@Getter
@Component
public class HazelcastConstant {

    @Value("${hazelcast.sets.account-id}")
    String accountIdSet;
}
