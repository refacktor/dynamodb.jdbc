package net.alexramos.dynamodb.jdbc;

import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

public class DataSourceLocalDynamoTest {

    private static DynamoDBProxyServer fakeCloud;

    private static DynamoDbClient cloudClient;

    private DataSource ds;
    
    @BeforeClass
    public static void init() throws Exception {
	System.setProperty("sqlite4java.library.path", "test-libs");
	String port = "8002";
	fakeCloud = ServerRunner
		.createServerFromCommandLineArgs(new String[] { "-inMemory", "-port", port });
	fakeCloud.start();

	cloudClient = DynamoDbClient.builder()
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
    }

    @Before
    public void beforeTest() throws Exception {
	
	ds = new DynamoJdbcDataSource(cloudClient);
    }

    @Test
    public void testCreateTable() throws SQLException {

	try (Connection con = ds.getConnection(); Statement stmt = con.createStatement()) {

	    stmt.execute("create table TEST1 (things int, x int, s varchar(99), PRIMARY KEY (things))");
	}

	DescribeTableResponse check = cloudClient
		.describeTable(dtr -> dtr.tableName("TEST1").build());
	Assert.assertEquals("TEST1", check.table().tableName());
	List<AttributeDefinition> attribs = check.table().attributeDefinitions();
	Assert.assertEquals("THINGS", attribs.get(0).attributeName());
	Assert.assertEquals("N", attribs.get(0).attributeTypeAsString());
    }
    
    @Test
    public void testInsert() throws SQLException {
	try (Connection con = ds.getConnection(); Statement stmt = con.createStatement()) {
	    stmt.execute("insert into TEST1 (things, x, words) values (5, 2, 'five and two')");
	}
	
	Map<String, AttributeValue> map = new HashMap<>();
	map.put("THINGS", AttributeValue.builder().n("5").build());
	GetItemResponse response = cloudClient.getItem(GetItemRequest.builder().tableName("TEST1")
	        .key(map).build());
	Assert.assertEquals("five and two", response.item().get("WORDS").s());
    }
    
    @Test
    public void testSelect() throws SQLException {
        try (Connection con = ds.getConnection(); Statement stmt = con.createStatement()) {
            stmt.execute("insert into TEST1 (things, x, words) values (3, 3, 'double threes')");
            ResultSet rs = stmt.executeQuery("SELECT things, x, words FROM TEST1 where things = 5");
            Assert.assertTrue(rs.next());
            Assert.assertEquals(5, rs.getInt(1));
            Assert.assertEquals(2, rs.getInt(2));
            Assert.assertEquals("five and two", rs.getString(3));
        }
        
    }

    @AfterClass
    public static void teardownClass() throws Exception {
	fakeCloud.stop();
    }
}
