package org.ops4j.jdbc.op;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.ops4j.base.BaseOp;
import org.ops4j.exception.OpsException;

import lombok.Getter;
import lombok.Setter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "jdbc:op",
    description = "Insert documents into a mongo collection.")
public abstract class JdbcOp<T extends JdbcOp<?>> extends BaseOp<JdbcOp<T>>
{
  @Option(names = { "-c", "-class" }, required = false,
      description = "The driver class.")
  private @Getter @Setter String     driverClass = "org.postgresql.Driver";

  @Option(names = { "-u", "-url" }, required = false,
      description = "The connection url.")
  private @Getter
  @Setter String                     url         = "jdbc:postgresql://localhost:5432/ops";

  @Option(names = { "-d", "-db" }, description = "The database name.")
  private @Getter @Setter String     db          = "test";

  @Option(names = { "-U", "-user" }, required = false,
      description = "The username.")
  private @Getter @Setter String     username    = "postgres";

  @Option(names = { "-s", "-schema" }, required = false,
      description = "The db schema.")
  private @Getter @Setter String     schema      = "local1";

  @Option(names = { "-p", "-password" }, required = false,
      description = "The password.")
  private @Getter @Setter String     password    = "local1";

  @Parameters(index = "0", arity = "1", description = "The database insertion.")
  private @Getter @Setter String     sql         = null;

  private @Getter @Setter Connection connection;

  public JdbcOp(String name)
  {
    super(name);
    defaultView("DEFAULT.JDBC");
  }

  public JdbcOp<T> initialize() throws OpsException
  {
    info("JDBC-CONFIG: ", config());
    return this;
  }

  public JdbcOp<T> open() throws OpsException
  {
    super.open();
    try
    {
      // Class.forName("org.hsqldb.jdbcDriver");
      // connection = DriverManager.getConnection(
      // "jdbc:hsqldb:file:C:/ws/ops4j-all/test/db/testdb;shutdown=true", "SA",
      // "");
      Class.forName(getDriverClass());
      connection = DriverManager.getConnection(getUrl(), getUsername(),
          getPassword());
      if (getSchema() != null && getSchema().trim().length() > 0)
      {
        connection.setSchema(getSchema());
      }
    }
    catch(SQLException | ClassNotFoundException ex)
    {
      throw new OpsException(ex);
    }
    return this;
  }

  public JdbcOp<T> close() throws OpsException
  {

    try
    {
      connection.close();
    }
    catch(SQLException ex)
    {
      throw new OpsException(ex);
    }
    super.close();
    return this;
  }
}
