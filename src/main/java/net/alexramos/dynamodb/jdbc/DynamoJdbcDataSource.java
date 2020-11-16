package net.alexramos.dynamodb.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.Config;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoJdbcDataSource implements DataSource {

    private PrintWriter logWriter = new PrintWriter(System.err);

    private DynamoDbClient client;

    private Config parserConfig;

    public DynamoJdbcDataSource(DynamoDbClient client) {
        super();
        this.client = client;
    }

    public void setDialect(String dialectName) {
        switch (dialectName) {
        case "mysql":
            parserConfig = SqlParser.configBuilder()
                    .setParserFactory(SqlDdlParserImpl.FACTORY)
                    .setQuoting(Quoting.BACK_TICK)
                    .setUnquotedCasing(Casing.TO_UPPER)
                    .setQuotedCasing(Casing.UNCHANGED)
                    .setConformance(SqlConformanceEnum.MYSQL_5).build();
            break;
        default:
            parserConfig = SqlParser.configBuilder()
                    .setParserFactory(SqlDdlParserImpl.FACTORY)
                    .setQuoting(Quoting.DOUBLE_QUOTE)
                    .setUnquotedCasing(Casing.TO_UPPER)
                    .setQuotedCasing(Casing.UNCHANGED)
                    .setConformance(SqlConformanceEnum.STRICT_99).build();
        }
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(getClass().getName());
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("Not Implemented");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLException("Not Implemented");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new DynamoJdbcConnection(client, parserConfig);
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

}
