package net.alexramos.dynamodb.util;

import java.io.File;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;

class SqlUtilTest {

    @Test
    void test() {
        final String SQL = Files
                .contentOf(new File("src/test/mysqldump-test-1.ddl"), "utf8");
        Stream<String> sqlStream = StreamSupport
                .stream(DynamodbSqlUtil.splitStatements(SQL), false);

        sqlStream.forEach(sqlNode -> {
            System.out.println("======================== \n" + sqlNode);
        });

    }

}
