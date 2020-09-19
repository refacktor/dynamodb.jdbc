package net.alexramos.dynamodb.sqlengine;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.apache.calcite.sql.parser.SqlParseException;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest.Builder;

public class DynamoSqlEngine {

	private DynamoJdbcConnection dynamoConnection;
	
	private ExecutableStatementFactory parser;

	public DynamoSqlEngine(DynamoJdbcConnection dynamoConnection) {
		this.dynamoConnection = dynamoConnection;
		this.parser = new ExecutableStatementFactory();
	}

	public DynamoJdbcResultSet executeQuery(String sql) throws SQLException {
		try {
			ExecutableStatement exec = parser.compile(sql);
			return exec.execute(dynamoConnection);
		} catch (SqlParseException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new SQLException(sql, e);
		}
	}
}
