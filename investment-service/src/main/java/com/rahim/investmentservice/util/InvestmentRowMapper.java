package com.rahim.investmentservice.util;

import com.rahim.investmentservice.model.InvestmentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Rahim Ahmed
 * @created 18/06/2024
 */
public class InvestmentRowMapper implements RowMapper<InvestmentResponse> {

    private static final Logger log = LoggerFactory.getLogger(InvestmentRowMapper.class);

    @Override
    public InvestmentResponse mapRow(ResultSet rs, int rowNum) {
        InvestmentResponse investmentResponse = new InvestmentResponse();

        try {
            investmentResponse.setCurrentValue(rs.getBigDecimal(1));
            investmentResponse.setPurchasePrice(rs.getBigDecimal(2));
            investmentResponse.setPurchaseDate(rs.getDate(3).toLocalDate());
        } catch (SQLException e) {
            log.error("Error mapping investment response row", e);
            throw new RuntimeException("Error mapping investment response row", e);
        }

        return investmentResponse;
    }
}
