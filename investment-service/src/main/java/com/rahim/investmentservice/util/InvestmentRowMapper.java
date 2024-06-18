package com.rahim.investmentservice.util;

import com.rahim.investmentservice.model.InvestmentResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Rahim Ahmed
 * @created 18/06/2024
 */
public class InvestmentRowMapper implements RowMapper<InvestmentResponse> {

    @Override
    public InvestmentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        InvestmentResponse investmentResponse = new InvestmentResponse();
        investmentResponse.setCurrentValue(rs.getBigDecimal(1));
        investmentResponse.setPurchasePrice(rs.getBigDecimal(2));
        investmentResponse.setPurchaseDate(rs.getDate(3).toLocalDate());

        return investmentResponse;
    }
}
