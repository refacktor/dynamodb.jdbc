package net.alexramos.dynamodb.jdbc;

import java.io.File;
import java.io.IOException;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.Config;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.util.SourceStringReader;
import org.assertj.core.util.Files;
import org.junit.Test;

public class MysqldumpReloadTest {

    private static final Config CONFIG = SqlParser.configBuilder().setParserFactory(SqlDdlParserImpl.FACTORY)
            .setQuoting(Quoting.DOUBLE_QUOTE).setUnquotedCasing(Casing.TO_UPPER).setQuotedCasing(Casing.UNCHANGED)
            .setConformance(SqlConformanceEnum.MYSQL_5).build();

    @Test
    public void testMultipleStatements() throws SqlParseException, IOException {
        final String SQL = Files.contentOf(new File("src/test/mysqldump-test-1.ddl"), "utf8");
        SqlParser sqlParser = SqlParser.create(new SourceStringReader(SQL), CONFIG);
        SqlNodeList sqlNodeList = (SqlNodeList) sqlParser.parseStmtList();
        
        sqlNodeList.forEach(sqlNode -> {
            System.out.println(sqlNode.toString());
        });
    }

}
