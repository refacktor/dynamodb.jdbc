# Getting Started

```
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import javax.sql.*;
import net.alexramos.dynamodb.jdbc.DynamoJdbcDataSource;
```

*Step 1. Get a connection to AWS DynamoDB*

```
DynamoDbClient cloudClient = DynamoDbClient
    .builder()
    .region(...)                // optional: leave this out to use environment variables
    .credentialsProvider(...)   // optional: leave this out to use ~/.aws/config or environment variables
    .build();
```

*Step 2. Create a DynamoJdbcDataSource*

```
DataSource ds = new DynamoJdbcDataSource(cloudClient);
```

*Step 3. Use it like any other JDBC DataSource*

```
try (Connection con = ds.getConnection(); Statement stmt = con.createStatement()) {
    stmt.execute(...);
}
```

# Does it work?

The following SQL Statements are currently implemented:

 * **CREATE TABLE**
 
   Simple tables only: columns with types that map to String, Boolean, or Number.
   
   The table must have a PRIMARY KEY.
   
 * **INSERT**
 
   Simple inserts of the form `INSERT INTO [table] ([cols]) VALUES ([values])` are supported.
   
   The column list must be specified.

 * **SELECT**
 
   Simple queries of the form `SELECT [column list] FROM [table] WHERE [primaryKey] = [value]` are supported.
   
   Table scans are not supported: you must specify primary key equality.
   
   Expressions and functions are not supported. 
   
   JOINs are not supported. 
   
   Columns must be listed: `SELECT *` is not supported.

# Architecture Considerations


 * **JDBC is pervasively dependent on SQL, but I heard DynamoDB is a NoSQL database. What gives?**

   Many JDBC SQL constructs have direct functional equivalents in the DynamoDB API, and vice-versa. Constructs
   that do not have a direct equivalent can generally be emulated with additional client-side code. DynamoDB's
   performance characteristics are different from most SQL-native databases, but those performance
   differences have no bearing on functionality. Many common use-cases, such as inserting a single data item
   or querying an item by primary key or performing a full table scan, have very comparable performance 
   at the scale required of many real-world applications. 

 * **Did you write a SQL parser from scratch?**
 
   Actually no, I didn't. The driver uses Apache Calcite (the same one used by Presto and Hive) to parse SQL
   code into an abstract syntax tree. This makes it fairly robust and easy to add support for more
   features.

 * **What's the point of this project?**
 
   I won't claim to know all possible use-cases. I created it because I wanted to get rid of the ongoing monthly
   costs of maintaining an instance of MySQL running 24x7 for one of my low-traffic web applications, without 
   sacrificing application availability or the user experience.
   I looked into Aurora Serverless but found the auto-start latency after idle periods to be detrimental to
   end-user experience. DynamoDB provides low latency 24x7, without the overhead of fixed monthly costs.
   My application would not benefit from reorganizing the data to maximize DynamoDB performance, so the effort
   boils down to swapping one API for another. Encapsulating all the changes in the JDBC driver was a cleaner,
   more self-contained effort and it's readily reusable when I need to do the same thing to the next application.

 * **Why should I use this?**

   If you have to ask, you probably shouldn't.
