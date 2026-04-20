# jdbc-ops

Relational database operations for Ops4J via JDBC.

This is an optional plugin module. Add it to a project that already depends on `ops4j-core` to read from and write to any JDBC-compatible database inside a pipeline.

---

## Overview

`jdbc-ops` integrates relational databases into the Ops4J pipeline model. Records flow in as JSON and are inserted, or result-set rows flow out as JSON records. The module ships with drivers for HSQLDB (default, in-memory) and H2 for zero-setup development, and can be configured for PostgreSQL, Oracle, or any other JDBC driver on the classpath.

---

## Operations

| Operation | Description |
| --- | --- |
| `jdbc-create` (`JdbcCreate`) | Inspects a sample of incoming JSON records and creates a table whose schema is inferred from the data. A configurable threshold controls how many records to sample before creating the table. |
| `jdbc-insert` (`JdbcInsert`) | Inserts records into a table using parameterized SQL with JSON field interpolation. |
| `jdbc-stream` (`JdbcStream`) | Executes a SQL query and streams each row of the `ResultSet` as a JSON record into the pipeline. |
| `jdbc-drop` (`JdbcDrop`) | Drops a named database table. |

All operations extend `JdbcOp`, which provides shared configuration for the JDBC driver class, connection URL, username, and password.

---

## Usage Examples

Create a table from data, insert records, then stream them back:

```bash
cat people.json | jdbc-create -t people | jdbc-insert -t people
jdbc-stream -q "SELECT * FROM people WHERE age > 30" | print
```

Load a CSV file into an in-memory database and query it:

```bash
cat sales.csv | map | jdbc-create -t sales | jdbc-insert -t sales
jdbc-stream -q "SELECT region, SUM(amount) FROM sales GROUP BY region" | print
```

---

## Configuration

`jdbc-ops` registers itself automatically. Preconfigured database profiles in `jdbc.conf`:

```hocon
jdbc {
  default = hsql

  hsql {
    driver = "org.hsqldb.jdbc.JDBCDriver"
    url    = "jdbc:hsqldb:mem:ops4j"
  }
  h2 {
    driver = "org.h2.Driver"
    url    = "jdbc:h2:mem:ops4j"
  }
  postgres {
    driver = "org.postgresql.Driver"
    url    = "jdbc:postgresql://localhost:5432/ops4j"
  }
  oracle {
    driver = "oracle.jdbc.OracleDriver"
    url    = "jdbc:oracle:thin:@localhost:1521:ops4j"
  }
}
```

Override the active profile or supply connection details at runtime via operation options.

---

## Key Dependencies

| Dependency | Purpose |
| --- | --- |
| [ops4j-core](../ops4j-core/) | Ops4J runtime and base classes |
| HSQLDB | Default in-memory database for zero-config development |
| H2 | Alternative in-memory/file database |
