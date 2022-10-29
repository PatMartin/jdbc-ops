package org.ops4j.op.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.ops4j.OpData;
import org.ops4j.cli.OpCLI;
import org.ops4j.exception.OpsException;
import org.ops4j.inf.Op;

import com.google.auto.service.AutoService;

import lombok.Getter;
import lombok.Setter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@AutoService(Op.class) @Command(name = "jdbc:insert",
    description = "Insert recoreds into a JDBC table.")
public class JdbcInsert extends JdbcOp<JdbcInsert>
{
  @Parameters(index = "0", arity = "1", description = "The query.")
  private @Getter @Setter String query;

  private Statement              statement;
  private ResultSet              rs;

  public JdbcInsert()
  {
    super("jdbc:insert");
  }

  public JdbcInsert open() throws OpsException
  {
    super.open();
    try
    {
      statement = getConnection().createStatement();
    }
    catch(SQLException ex)
    {
      throw new OpsException(ex);
    }
    return this;
  }

  public List<OpData> execute(OpData input)
  {
    return input.asList();
  }

  public JdbcInsert close() throws OpsException
  {
    try
    {
      statement.close();
    }
    catch(SQLException ex)
    {
      throw new OpsException(ex);
    }
    super.close();
    return this;
  }

  public static void main(String args[]) throws OpsException
  {
    OpCLI.cli(new JdbcInsert(), args);
  }
}
