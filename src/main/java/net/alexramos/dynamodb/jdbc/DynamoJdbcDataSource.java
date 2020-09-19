package net.alexramos.dynamodb.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoJdbcDataSource implements DataSource {
    
    	private PrintWriter logWriter = new PrintWriter(System.err);
    	
	private DynamoDbClient client;

	public DynamoJdbcDataSource(DynamoDbClient client) {
	    super();
	    this.client = client;
	}
	
	public DynamoJdbcDataSource() {
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
		return new DynamoJdbcConnection(client);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
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
