package com.dtstep.lighthouse.insights;
import com.dtstep.lighthouse.insights.modal.Column;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JsonArrayTypeHandler<T> extends BaseTypeHandler<List<T>> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TypeReference<List<T>> typeReference = null;

    public JsonArrayTypeHandler(TypeReference<List<T>> typeReference) {
        this.typeReference = typeReference;
    }

    public JsonArrayTypeHandler(){}

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (JsonProcessingException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private List<T> parseJson(String json) throws SQLException {
        if (json != null && !json.isEmpty()) {
            try {
                System.out.println("json:" + json);
                System.out.println("typeReference:" + typeReference);
                return objectMapper.readValue(json, typeReference);
            } catch (IOException e) {
                throw new SQLException(e);
            }
        }
        return null;
    }
}