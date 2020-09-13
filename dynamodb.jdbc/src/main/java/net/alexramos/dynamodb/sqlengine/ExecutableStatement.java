package net.alexramos.dynamodb.sqlengine;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;

public interface ExecutableStatement {

	public DynamoJdbcResultSet execute(DynamoJdbcConnection dynamoConnection);

}
