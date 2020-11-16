package net.alexramos.dynamodb.jdbc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.cli.ParseException;

import com.amazonaws.services.dynamodbv2.exceptions.DynamoDBLocalServiceException;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoJdbcLocalDataSource extends DynamoJdbcDataSource {

    public DynamoJdbcLocalDataSource() {
        super(createClient());
    }

    private static DynamoDbClient createClient() {
        try {
            DynamoDBProxyServer fakeCloud;
            System.setProperty("sqlite4java.library.path", "test-libs");
            Integer port = getFreePort();
            fakeCloud = ServerRunner.createServerFromCommandLineArgs(
                    new String[] { "-inMemory", "-port", port.toString() });
            try {
                fakeCloud.start();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to start fake cloud for testing.", e);
            }
            return DynamoDbClient.builder()
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
                    }).build();
        } catch (URISyntaxException | DynamoDBLocalServiceException
                | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer getFreePort() {
        try {
            ServerSocket socket = new ServerSocket(0);
            int port = socket.getLocalPort();
            socket.close();
            return port;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
