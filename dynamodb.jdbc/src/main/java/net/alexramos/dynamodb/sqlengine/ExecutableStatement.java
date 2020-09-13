package net.alexramos.dynamodb.sqlengine;

import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNumericLiteral;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public interface ExecutableStatement {

    public DynamoJdbcResultSet execute(DynamoJdbcConnection dynamoConnection);

    static AttributeValue toAV(final SqlLiteral sqlLiteral) {
        AttributeValue av;
        if (sqlLiteral instanceof SqlNumericLiteral) {
            av = AttributeValue.builder().n(sqlLiteral.bigDecimalValue().toPlainString())
                    .build();
        } else {
            av = AttributeValue.builder().s(sqlLiteral.toValue()).build();
        }
        return av;
    }

}
