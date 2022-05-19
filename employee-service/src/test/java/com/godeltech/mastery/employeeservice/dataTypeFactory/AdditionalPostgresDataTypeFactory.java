package com.godeltech.mastery.employeeservice.dataTypeFactory;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.postgresql.GenericEnumType;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

import java.sql.Types;

public class AdditionalPostgresDataTypeFactory extends PostgresqlDataTypeFactory {
    @Override
    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        if (isEnumType(sqlTypeName)) {
            return new GenericEnumType(sqlTypeName);
        }else {
            return super.createDataType(sqlType, sqlTypeName);
        }
    }
    @Override
    public boolean isEnumType(String sqlTypeName) {
        return "gender_type".equals(sqlTypeName);
    }
}
