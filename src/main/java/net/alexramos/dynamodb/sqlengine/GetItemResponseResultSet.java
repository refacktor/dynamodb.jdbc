package net.alexramos.dynamodb.sqlengine;

import java.sql.SQLException;
import java.util.Map;

import org.apache.calcite.sql.SqlIdentifier;

import net.alexramos.dynamodb.jdbc.DynamoJdbcResultSet;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

public class GetItemResponseResultSet extends DynamoJdbcResultSet {

    private SqlIdentifier[] columnList;

    private Map<String, AttributeValue> item;
    
    private int next = 1;

    public GetItemResponseResultSet(SqlIdentifier[] columnList, GetItemResponse response) {
        this.item = response.item();
        this.columnList = columnList;
    }
    
    @Override
    public boolean next() throws SQLException {
        return next-- > 0;
    }
    
    @Override
    public int getInt(int columnIndex) throws SQLException {
        return Integer.parseInt(item.get(columnList[columnIndex-1].getSimple()).n());
    }
    
    @Override
    public String getString(int columnIndex) throws SQLException {
        return item.get(columnList[columnIndex-1].getSimple()).s();
    }
    
}
