package net.alexramos.dynamodb.sqlengine;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

import net.alexramos.dynamodb.jdbc.DynamoSqlDataSource;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;

public class DynamoSqlEngineTest {

    private static DynamoDBProxyServer server;

    private DynamoDbClient client;

    private DataSource ds;

    @Before
    public void init() throws Exception {
	System.setProperty("sqlite4java.library.path", "native-libs");
	String port = "8001";
	server = ServerRunner
		.createServerFromCommandLineArgs(new String[] { "-inMemory", "-port", port });
	server.start();

	client = DynamoDbClient.builder()
			.endpointOverride(new URI("http://localhost:" + port))
			.region(Region.AWS_GLOBAL)
			.credentialsProvider(() -> new AwsCredentials() {
			    @Override
			    public String accessKeyId() {
				return "fakeId";
			    }

			    @Override
			    public String secretAccessKey() {
				return "fakePass";
			    }
			})
		.build();
	
	ds = new DynamoSqlDataSource(client);
    }

    @Test
    public void testCreateTable() throws SQLException {

	try (Connection con = ds.getConnection(); Statement stmt = con.createStatement()) {

	    stmt.execute("create table TEST1 (things int, x int, s varchar(99), PRIMARY KEY (things))");
	}

	DescribeTableResponse check = client
		.describeTable(dtr -> dtr.tableName("TEST1").build());
	Assert.assertEquals("TEST1", check.table().tableName());
	List<AttributeDefinition> attribs = check.table().attributeDefinitions();
	Assert.assertEquals("THINGS", attribs.get(0).attributeName());
	Assert.assertEquals("N", attribs.get(0).attributeTypeAsString());
    }

    @AfterClass
    public static void teardownClass() throws Exception {
	server.stop();
    }
}
