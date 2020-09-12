package net.alexramos.dynamodb.jdbc;

import java.lang.reflect.Proxy;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlNumericLiteral;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.ddl.SqlColumnDeclaration;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.ddl.SqlKeyConstraint;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParser.Config;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.sql.util.SqlVisitor;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.util.SourceStringReader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Some quick sanity checks to ensure the Calcite SQL Parser does what 
 * we think the documentation that we didn't read says it does.
 * 
 * @author atram
 *
 */
public class SQLParserAssumptionsTest {

	private static final Config CONFIG = SqlParser.configBuilder().setParserFactory(SqlDdlParserImpl.FACTORY)
			.setQuoting(Quoting.DOUBLE_QUOTE).setUnquotedCasing(Casing.TO_UPPER).setQuotedCasing(Casing.UNCHANGED)
			.setConformance(SqlConformanceEnum.STRICT_99).build();

	@Test
	public void testSimpleSelect() throws SqlParseException {
		SqlParser sqlParser = SqlParser.create(new SourceStringReader("select x,y from z"), CONFIG);
		SqlNode sqlNode = sqlParser.parseQuery();

		Assert.assertEquals(SqlKind.SELECT, sqlNode.getKind());

		@SuppressWarnings("unchecked")
		SqlVisitor<Void> visitor = (SqlVisitor<Void>) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[] { SqlVisitor.class }, (proxy, method, args) -> {
					Assert.assertEquals("public abstract java.lang.Object org.apache.calcite.sql.util.SqlVisitor.visit(org.apache.calcite.sql.SqlCall)", method.toString());
					return (Void) null;
				});
		sqlNode.accept(visitor);
		
		SqlSelect select = (SqlSelect) sqlNode;
		SqlIdentifier from = (SqlIdentifier) select.getFrom();
		Assert.assertEquals("Z", from.getSimple());
		SqlNodeList selectList = select.getSelectList();
		SqlIdentifier x = (SqlIdentifier) selectList.get(0);
		Assert.assertEquals("X", x.getSimple());
		SqlIdentifier y = (SqlIdentifier) selectList.get(1);
		Assert.assertEquals("Y", y.getSimple());
	}
	
	@Test
	public void testSimpleCreate() throws SqlParseException {
		SqlParser sqlParser = SqlParser.create(new SourceStringReader("create table t1 (x int)"), CONFIG);
		SqlNode sqlNode = sqlParser.parseQuery();

		Assert.assertEquals(SqlKind.CREATE_TABLE, sqlNode.getKind());
		
		SqlCreateTable create = (SqlCreateTable) sqlNode;
		Assert.assertEquals("T1", create.name.getSimple());
		final SqlColumnDeclaration sqlColumnDeclaration = (SqlColumnDeclaration)create.columnList.get(0);
		Assert.assertEquals("X", sqlColumnDeclaration.name.getSimple());
		Assert.assertEquals("INTEGER", sqlColumnDeclaration.dataType.getTypeName().getSimple());
	}

	@Test
	public void testCreateWithKey() throws SqlParseException {
		SqlParser sqlParser = SqlParser.create(new SourceStringReader("create table t1 (x int, y int, z int, primary key (y)) "), CONFIG);
		SqlNode sqlNode = sqlParser.parseQuery();

		Assert.assertEquals(SqlKind.CREATE_TABLE, sqlNode.getKind());
		
		SqlCreateTable create = (SqlCreateTable) sqlNode;
		Assert.assertEquals("T1", create.name.getSimple());
		final SqlColumnDeclaration sqlColumnDeclaration = (SqlColumnDeclaration)create.columnList.get(0);
		Assert.assertEquals("X", sqlColumnDeclaration.name.getSimple());
		Assert.assertEquals("INTEGER", sqlColumnDeclaration.dataType.getTypeName().getSimple());
		
		SqlKeyConstraint pk = (SqlKeyConstraint) create.columnList.get(3);
		final SqlNode sqlNode3 = pk.getOperandList().get(1);
		Assert.assertEquals("PRIMARY KEY", pk.getOperator().getName());
		Assert.assertEquals("Y", ((SqlIdentifier)((SqlNodeList)sqlNode3).get(0)).getSimple());
		
	}

	@Test
	public void testSimpleInsert() throws SqlParseException {
		SqlParser sqlParser = SqlParser.create(new SourceStringReader("insert into t1 (x) values (42)"), CONFIG);
		SqlNode sqlNode = sqlParser.parseQuery();

		Assert.assertEquals(SqlKind.INSERT, sqlNode.getKind());
		
		SqlInsert insert = (SqlInsert) sqlNode;
		Assert.assertEquals("T1", ((SqlIdentifier)insert.getTargetTable()).getSimple());
		Assert.assertEquals("X", ((SqlIdentifier)insert.getTargetColumnList().get(0)).getSimple());
		final SqlBasicCall source = (SqlBasicCall)insert.getSource();
		final SqlBasicCall row = (SqlBasicCall) source.getOperands()[0];
		final SqlNumericLiteral operand = (SqlNumericLiteral) row.operands[0];
		Assert.assertEquals(42, operand.intValue(true));
	}
}