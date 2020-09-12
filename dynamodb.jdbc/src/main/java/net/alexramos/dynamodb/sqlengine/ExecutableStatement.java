package net.alexramos.dynamodb.sqlengine;

import net.alexramos.dynamodb.jdbc.DynamoConnection;
import net.alexramos.dynamodb.jdbc.DynamoResultSet;

public interface ExecutableStatement {

	public DynamoResultSet execute(DynamoConnection dynamoConnection);

}
