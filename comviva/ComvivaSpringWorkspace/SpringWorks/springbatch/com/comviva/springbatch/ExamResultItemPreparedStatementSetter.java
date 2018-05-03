package com.comviva.springbatch;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
 
/*
 * 
 * Note that since we are using Joda-time LocalDate which can not be mapped to MySQL date directly, 
 * we need to provide custom-logic to handle this conversion.Below is the class for this conversion.
 */
public class ExamResultItemPreparedStatementSetter implements ItemPreparedStatementSetter<ExamResult> {
 
    public void setValues(ExamResult result, PreparedStatement ps) throws SQLException {
        ps.setString(1, result.getStudentName());
        ps.setDate(2, new java.sql.Date(result.getDob().toDate().getTime()));
        ps.setDouble(3, result.getPercentage());
    }
 
}