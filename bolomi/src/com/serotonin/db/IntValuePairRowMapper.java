/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.serotonin.db.spring.GenericRowMapper;

/**
 * @author Matthew Lohbihler
 */
public class IntValuePairRowMapper implements GenericRowMapper<IntValuePair> {
    private final int intIndex;
    private final int valueIndex;

    public IntValuePairRowMapper() {
        this(1, 2);
    }

    public IntValuePairRowMapper(int intIndex, int valueIndex) {
        this.intIndex = intIndex;
        this.valueIndex = valueIndex;
    }

    public IntValuePair mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new IntValuePair(rs.getInt(intIndex), rs.getString(valueIndex));
    }
}
