package net.alexramos.dynamodb.util;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.Config;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:spring-test.xml" })
public class SpringInitTest extends AbstractJUnit4SpringContextTests {

    private static final Config CONFIG = SqlParser.configBuilder()
            .setParserFactory(SqlDdlParserImpl.FACTORY)
            .setQuoting(Quoting.DOUBLE_QUOTE).setUnquotedCasing(Casing.TO_UPPER)
            .setQuotedCasing(Casing.UNCHANGED)
            .setConformance(SqlConformanceEnum.MYSQL_5).build();

    @Test
    public void test() {
        Assert.assertTrue(true);
    }

}
