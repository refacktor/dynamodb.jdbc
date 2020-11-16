package net.alexramos.dynamodb.sqlengine;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser.Config;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;

public class DynamoSqlEngine {

    private DynamoJdbcConnection dynamoConnection;

    private ExecutableStatementFactory parser;

    public DynamoSqlEngine(DynamoJdbcConnection dynamoConnection,
            Config parserConfig) {
        this.dynamoConnection = dynamoConnection;
        this.parser = new ExecutableStatementFactory(parserConfig);
    }

    public DynamoJdbcResultSet executeQuery(String sql) throws SQLException {
        try {
            ExecutableStatement exec = parser.compile(sql);
            return exec.execute(dynamoConnection);
        } catch (SqlParseException | NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new SQLException(sql, e);
        }
    }
}
