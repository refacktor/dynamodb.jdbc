package net.alexramos.dynamodb.sqlengine;

import java.sql.SQLException;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.ddl.SqlDropTable;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

public class ExecutableSqlDropTable implements ExecutableStatement {

    private String tableName;

    private boolean ifExists;

    public ExecutableSqlDropTable(SqlDropTable sql) {
        this.tableName = sql.name.getSimple();
        this.ifExists = sql.ifExists;
    }

    @Override
    public DynamoJdbcResultSet execute(DynamoJdbcConnection dynamoConnection)
            throws SQLException {
        DynamoDbClient client = dynamoConnection.getClient();
        try {
            client.deleteTable(builder -> builder.tableName(tableName));
        } catch (ResourceNotFoundException rnfe) {
            if (!ifExists) {
                throw new SQLException(tableName, rnfe);
            }
        }
        return DynamoJdbcResultSet.OK;
    }

}
