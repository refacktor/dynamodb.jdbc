package net.alexramos.dynamodb.sqlengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;

import net.alexramos.dynamodb.jdbc.DynamoJdbcConnection;
import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;
import net.alexramos.dynamodb.util.DynamodbSqlUtil;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

public class ExecutableSqlInsert implements ExecutableStatement {

    private String tableName;

    private List<String> columns = new ArrayList<>();

    private List<SqlLiteral[]> values = new LinkedList<>();

    public ExecutableSqlInsert(SqlInsert sql) {
        this.tableName = ((SqlIdentifier) sql.getTargetTable()).getSimple();
        sql.getTargetColumnList().forEach(node -> {
            columns.add(((SqlIdentifier) node).getSimple());
        });
        final SqlNode[] rows = ((SqlBasicCall) sql.getSource()).getOperands();
        for (SqlNode row : rows) {
            assert row.getKind() == SqlKind.ROW;
            SqlLiteral[] litRow = Stream.of(((SqlBasicCall) row).operands).map(x -> {
                if (x instanceof SqlLiteral) {
                    return (SqlLiteral) x;
                } else {
                    throw new UnsupportedOperationException(x.toString());
                }
            }).toArray(SqlLiteral[]::new);
            values.add(litRow);
        }
    }

    private Map<String, AttributeValue> rowValue(SqlLiteral[] row) {
        Map<String, AttributeValue> out = new LinkedHashMap<>();
        for(int i=0; i < row.length; ++i) {
            final SqlLiteral sqlLiteral = row[i];
            AttributeValue av = DynamodbSqlUtil.toAV(sqlLiteral);
            out.put(columns.get(i), av);
        }
        return out;
    }

    private WriteRequest toWriteRequest(Map<String, AttributeValue> row) {
        return WriteRequest.builder().putRequest(PutRequest.builder()
                .item(row).build()).build();
    }

    @Override
    public DynamoJdbcResultSet execute(DynamoJdbcConnection dynamoConnection) {
        DynamoDbClient client = dynamoConnection.getClient();
        Map<String, List<WriteRequest>> requestItems = new HashMap<>();
        final List<WriteRequest> requestItemList = values.stream().map(this::rowValue)
                .map(this::toWriteRequest).collect(Collectors.toList());
        
        requestItems.put(tableName, requestItemList);

        BatchWriteItemResponse response = client.batchWriteItem(
                BatchWriteItemRequest.builder().requestItems(requestItems).build());
        
        DynamoJdbcResultSet result = new DynamoJdbcResultSet();
        int unprocessedCount = 0;
        if(response.hasUnprocessedItems())
            if(response.unprocessedItems().get(tableName) != null)
                unprocessedCount = response.unprocessedItems().get(tableName).size();
        
        result.setUpdateCount(requestItemList.size() - unprocessedCount);
        return result;
    }

}
