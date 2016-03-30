package com.serotonin.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.serotonin.db.spring.GenericRowMapper;

public class KeyValuePairRowMapper implements GenericRowMapper<KeyValuePair> {
    private final int keyIndex;
    private final int valueIndex;

    public KeyValuePairRowMapper() {
        this(1, 2);
    }

    public KeyValuePairRowMapper(int keyIndex, int valueIndex) {
        this.keyIndex = keyIndex;
        this.valueIndex = valueIndex;
    }

    public KeyValuePair mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new KeyValuePair(rs.getString(keyIndex), rs.getString(valueIndex));
    }
}
