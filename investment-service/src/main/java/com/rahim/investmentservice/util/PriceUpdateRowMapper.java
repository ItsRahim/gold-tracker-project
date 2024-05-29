package com.rahim.investmentservice.util;

import com.rahim.investmentservice.dao.HoldingDataAccess;
import com.rahim.investmentservice.dao.InvestmentsDataAccess;
import com.rahim.investmentservice.model.PriceUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Rahim Ahmed
 * @created 29/05/2024
 */
public class PriceUpdateRowMapper implements RowMapper<PriceUpdate> {

    private static final Logger LOG = LoggerFactory.getLogger(PriceUpdateRowMapper.class);

    @Override
    public PriceUpdate mapRow(ResultSet rs, int rowNum) {
        try {
            PriceUpdate model = new PriceUpdate();
            model.setHoldingId(rs.getInt(HoldingDataAccess.COL_HOLDING_ID));
            model.setGoldTypeId(rs.getInt(InvestmentsDataAccess.COL_GOLD_TYPE_ID));
            model.setPurchaseAmount(rs.getBigDecimal(HoldingDataAccess.COL_TOTAL_PURCHASE_AMOUNT));
            model.setCurrentValue(rs.getBigDecimal(HoldingDataAccess.COL_CURRENT_VALUE));
            return model;
        } catch (SQLException e) {
            LOG.error("Error mapping row number {} to PriceUpdate: {}", rowNum, e.getMessage(), e);
            throw new RuntimeException("Error mapping row to PriceUpdate", e);
        }
    }
}

