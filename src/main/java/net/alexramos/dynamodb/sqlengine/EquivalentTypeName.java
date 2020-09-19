package net.alexramos.dynamodb.sqlengine;

public enum EquivalentTypeName {
	
	  BOOLEAN("BOOL"),
	  TINYINT("N"),
	  SMALLINT("N"),
	  INTEGER("N"),
	  BIGINT("N"),
	  DECIMAL("N"),
	  FLOAT("N"),
	  REAL("N"),
	  DOUBLE("N"),
	  DATE("S"),
	  TIME("S"),
	  TIME_WITH_LOCAL_TIME_ZONE("S"),
	  TIMESTAMP("S"),
	  TIMESTAMP_WITH_LOCAL_TIME_ZONE("S"),
	  INTERVAL_YEAR("S"),
	  INTERVAL_YEAR_MONTH("S"),
	  INTERVAL_MONTH("S"),
	  INTERVAL_DAY("S"),
	  INTERVAL_DAY_HOUR("S"),
	  INTERVAL_DAY_MINUTE("S"),
	  INTERVAL_DAY_SECOND("S"),
	  INTERVAL_HOUR("S"),
	  INTERVAL_HOUR_MINUTE("S"),
	  INTERVAL_HOUR_SECOND("S"),
	  INTERVAL_MINUTE("S"),
	  INTERVAL_MINUTE_SECOND("S"),
	  INTERVAL_SECOND("S"),
	  CHAR("S"),
	  VARCHAR("S"),
	  BINARY("B"),
	  VARBINARY("B"),
	  NULL("S"),
	  ANY("S"),
	  SYMBOL("S"),
	  MULTISET("S"),
	  ARRAY("SS"),
	  MAP("S"),
	  DISTINCT("S"),
	  STRUCTURED("S"),
	  ROW("S"),
	  OTHER("S"),
	  CURSOR("S"),
	  COLUMN_LIST("SS"),
	  DYNAMIC_STAR("S"),
	  GEOMETRY("NS");

	private String dynamoEquivalent;

	EquivalentTypeName(String dynamoEquivalent) {
		this.dynamoEquivalent = dynamoEquivalent;
	}

	public final String getDynamoEquivalent() {
		return dynamoEquivalent;
	}

}
