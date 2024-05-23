package com.rahim.investmentservice.service.holding;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 23/05/2024
 */
public interface HoldingDeletionService {

    List<Integer> sellHolding(List<Integer> holdingIds, int accountId);
}
