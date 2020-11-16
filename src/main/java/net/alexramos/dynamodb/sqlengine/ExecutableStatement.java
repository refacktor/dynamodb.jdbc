package net.alexramos.dynamodb.sqlengine;

import java.sql.SQLException;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;

public interface ExecutableStatement {

    public DynamoJdbcResultSet execute(DynamoJdbcConnection dynamoConnection)
            throws SQLException;

}
