package net.alexramos.dynamodb.jdbc;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.ddl.SqlColumnDeclaration;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.Config;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.util.SourceStringReader;
import org.junit.Assert;
import org.junit.Test;

public class SqlParserMysqlTest {

// For Calcite 1.x, use:    
//    private static final Config CONFIG = SqlParser.configBuilder()
//            .setParserFactory(SqlDdlParserImpl.FACTORY)
//            .setQuoting(Quoting.DOUBLE_QUOTE).setUnquotedCasing(Casing.TO_UPPER)
//            .setQuotedCasing(Casing.UNCHANGED).setLex(Lex.MYSQL)
//            .setConformance(SqlConformanceEnum.MYSQL_5).build();

    private static final Config CONFIG = SqlParser.Config.DEFAULT
            .withLex(Lex.MYSQL).withConformance(SqlConformanceEnum.MYSQL_5)
            .withParserFactory(SqlDdlParserImpl.FACTORY);

    @Test
    public void testMysqlCreate() throws SqlParseException {
        SqlParser sqlParser = SqlParser.create(
                new SourceStringReader("create table `t1` (x int(11))"),
                CONFIG);
        SqlNode sqlNode = sqlParser.parseQuery();

        Assert.assertEquals(SqlKind.CREATE_TABLE, sqlNode.getKind());

        SqlCreateTable create = (SqlCreateTable) sqlNode;
        Assert.assertEquals("T1", create.name.getSimple());
        final SqlColumnDeclaration sqlColumnDeclaration = (SqlColumnDeclaration) create.columnList
                .get(0);
        Assert.assertEquals("X", sqlColumnDeclaration.name.getSimple());
        Assert.assertEquals("INTEGER",
                sqlColumnDeclaration.dataType.getTypeName().getSimple());
    }

}
