package net.alexramos.dynamodb.sqlengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.ddl.SqlColumnDeclaration;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.ddl.SqlKeyConstraint;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.KeyType;

class ExecutableSqlCreateTable implements ExecutableStatement {

    private final String tableName;

    private String primaryKey = null;

    private Map<String, EquivalentTypeName> columnsWithTypes = new LinkedHashMap<>();

    ExecutableSqlCreateTable(SqlCreateTable sql)
	    throws NoSuchMethodException, SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	this.tableName = sql.name.getSimple();
	for (SqlNode node : sql.columnList) {
	    Method handler = Stream.of(this.getClass().getMethods()).filter(
		    method -> method.getParameterTypes()[0].isAssignableFrom(node.getClass()))
		    .findFirst().orElseThrow();
	    handler.invoke(this, node);
	}
    }

    public void parse(SqlColumnDeclaration sqlColumnDeclaration) {
	columnsWithTypes.put(sqlColumnDeclaration.name.getSimple(), EquivalentTypeName
		.valueOf(sqlColumnDeclaration.dataType.getTypeName().getSimple()));
    }

    public void parse(SqlKeyConstraint keyConstraint) {
	final SqlNode keyName = keyConstraint.getOperandList().get(1);
	switch (keyConstraint.getOperator().getName()) {
	case "PRIMARY KEY":
	    this.primaryKey = ((SqlIdentifier) ((SqlNodeList) keyName).get(0)).getSimple();
	    break;
	case "UNIQUE":
	case "CHECK":
	default:
	    throw new UnsupportedOperationException(keyConstraint.toString());
	}

    }

    @SuppressWarnings("unchecked")
    @Override
    public DynamoJdbcResultSet execute(DynamoJdbcConnection dynamoConnection) {
	DynamoDbClient client = dynamoConnection.getClient();
	client.createTable(ctr -> {
	    ctr.tableName(this.tableName);
	    ctr.attributeDefinitions(columnsWithTypes.entrySet().stream()
		    .filter(e -> e.getKey().equals(primaryKey)).map(e -> {
			return AttributeDefinition.builder().attributeName(e.getKey())
				.attributeType(e.getValue().getDynamoEquivalent()).build();
		    }).collect(Collectors.toList()));
	    ctr.keySchema(builder -> builder.attributeName(primaryKey).keyType(KeyType.HASH));
	    ctr.billingMode(BillingMode.PAY_PER_REQUEST);
	});
	return DynamoJdbcResultSet.OK;
    }

}
