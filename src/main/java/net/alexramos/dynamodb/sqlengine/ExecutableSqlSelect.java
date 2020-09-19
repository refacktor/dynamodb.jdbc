package net.alexramos.dynamodb.sqlengine;

import java.util.Map;

import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlSelect;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

public class ExecutableSqlSelect implements ExecutableStatement {

    private String tableName;

    private SqlIdentifier[] columnList;
    
    private SqlBasicCall where;

    public ExecutableSqlSelect(SqlSelect sql) {
        SqlIdentifier from = (SqlIdentifier) sql.getFrom();
        this.tableName = from.getSimple();
        this.columnList = sql.getSelectList().getList().stream().map(o -> (SqlIdentifier) o)
                .toArray(SqlIdentifier[]::new);
        this.where = (SqlBasicCall) sql.getWhere();
        
        if(where.getOperator().getKind() != SqlKind.EQUALS) {
            throw new IllegalArgumentException("WHERE clause must consist of a simple equality comparison to the primary key.");
        }
    }

    @Override
    public DynamoJdbcResultSet execute(DynamoJdbcConnection dynamoConnection) {
        DynamoDbClient client = dynamoConnection.getClient();
        SqlIdentifier key = (SqlIdentifier) where.getOperands()[0];
        SqlLiteral value = (SqlLiteral) where.getOperands()[1];
        GetItemResponse response = client.getItem(GetItemRequest.builder().tableName(tableName)
                .key(Map.of(key.getSimple(), ExecutableStatement.toAV(value))).build());
        return new GetItemResponseResultSet(columnList, response);
        
    }

}
