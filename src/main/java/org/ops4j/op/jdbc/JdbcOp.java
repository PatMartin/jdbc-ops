package org.ops4j.op.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.ops4j.base.BaseOp;
import org.ops4j.exception.OpsException;
import org.ops4j.util.ResultSetIterator;

import lombok.Getter;
import lombok.Setter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "jdbc:op",
    description = "Insert documents into a mongo collection.")
public abstract class JdbcOp<T extends JdbcOp<?>> extends BaseOp<JdbcOp<T>>
{
  @Option(names = { "-u", "-url" }, required = false,
      description = "The connection string.")
  private @Getter @Setter String     url = null;

  @Option(names = { "-d", "-db" }, description = "The database name.")
  private @Getter @Setter String     db  = "test";

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
      Class.forName("org.hsqldb.jdbcDriver");
      connection = DriverManager.getConnection(
          "jdbc:hsqldb:file:C:/ws/ops4j-all/test/db/testdb;shutdown=true", "SA",
          "");
    }
    catch(SQLException | ClassNotFoundException ex)
    {
      throw new OpsException(ex);
    }
    return this;
  }

  public JdbcOp<T> close() throws OpsException
  {    
    return this;
  }
}
