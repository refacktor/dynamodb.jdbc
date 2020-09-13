package net.alexramos.dynamodb.sqlengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.ddl.SqlColumnDeclaration;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.Config;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.util.SourceStringReader;

/**
 * SQL Language reference:
 * https://calcite.apache.org/docs/reference.html
 * 
 * @author atram
 *
 */
public class ExecutableStatementFactory {

	private static final Config CONFIG = SqlParser.configBuilder().setParserFactory(SqlDdlParserImpl.FACTORY)
	.setQuoting(Quoting.DOUBLE_QUOTE).setUnquotedCasing(Casing.TO_UPPER).setQuotedCasing(Casing.UNCHANGED)
	.setConformance(SqlConformanceEnum.STRICT_99).build();

	ExecutableStatement compile(SqlNode sqlNode) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method handler = this.getClass().getMethod("compile", new Class[] { sqlNode.getClass() });
		return (ExecutableStatement) handler.invoke(this, sqlNode);
	}

	public ExecutableStatement compile(SqlCreateTable sql) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return new ExecutableSqlCreateTable(sql);
	}

	public ExecutableStatement compile(SqlInsert sql) {
		return new ExecutableSqlInsert(sql);
	}

	public ExecutableStatement compile(SqlSelect sql) {
		return null;
	}

	public ExecutableStatement compile(String sql) throws SqlParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SqlParser sqlParser = SqlParser.create(new SourceStringReader(sql), CONFIG);
		SqlNode sqlNode = sqlParser.parseQuery();
		return this.compile(sqlNode);
	}

}
