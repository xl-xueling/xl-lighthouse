package com.dtstep.lighthouse.insights.types;

import com.dtstep.lighthouse.common.entity.annotation.DBEnumMapperAnnotation;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.VARCHAR)
public class GenericEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private final Class<E> type;

    private Method getCodeMethod;

    private static String upperFirst(String toUpper){
        return toUpper.substring(0, 1).toUpperCase()+ toUpper.substring(1);
    }

    public GenericEnumTypeHandler(Class<E> type) {
        this.type = type;
        try {
            Field[] fields = type.getDeclaredFields();
            for(Field field : fields){
                Annotation annotation = field.getAnnotation(DBEnumMapperAnnotation.class);
                if(annotation != null){
                    getCodeMethod = type.getMethod("get"+upperFirst(field.getName()));
                }
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Enum does not have a getCode() method", e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        try {
            Object code = getCodeMethod.invoke(parameter);
            ps.setString(i, code.toString());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SQLException("Error setting enum parameter", e);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return getEnumByCode(code);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return getEnumByCode(code);
    }


    @Override
    public E getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }

    private E getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (E e : type.getEnumConstants()) {
            try {
                Object enumCode = getCodeMethod.invoke(e);
                if (code.equals(enumCode.toString())) {
                    return e;
                }
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }
        return null;
    }
}
