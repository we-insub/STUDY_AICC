package kr.co.aicc.infra.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValueEnumImplTypeHandler<E extends Enum <E>> implements TypeHandler<ValueEnum> {

    private Class <E> type;

    public ValueEnumImplTypeHandler(Class <E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, ValueEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public ValueEnum getResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return getCodeEnum(code);
    }

    @Override
    public ValueEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return getCodeEnum(code);
    }

    @Override
    public ValueEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return getCodeEnum(code);
    }

    private ValueEnum getCodeEnum (String code) {
        try {
            ValueEnum[] enumConstants = (ValueEnum[]) type.getEnumConstants();
            for (ValueEnum codeNum : enumConstants) {
                if (codeNum.getValue().equals(code)) {
                    return codeNum;
                }
            }
            return null;
        } catch (Exception e) {
            throw new TypeException("can't make enum '" + type + "'", e);
        }
    }

}
