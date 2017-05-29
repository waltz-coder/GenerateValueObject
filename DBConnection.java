/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerateValueObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zengmh
 */
public class DBConnect
{

    private Connection conn;
    private final String driver;
    private final String user;
    private final String password;
    private final String url;
    private String databasename;
    private final List<String> table_list;
    private final List<String> table_type_list;
    private final List<String> column_list;
    private final List<String> comment_list;
    private final List<String> column_type_list;
    private final List<String> is_nullable_list;

    public DBConnect()
    {
        driver = "com.mysql.jdbc.Driver";
        user = "root";
        password = "password";
        url = "jdbc:mysql://localhost:3306/information_schema?characterEncoding=utf8&useSSL=true";
        table_list = new ArrayList();
        table_type_list = new ArrayList();
        column_list = new ArrayList();
        comment_list = new ArrayList();
        column_type_list = new ArrayList();
        is_nullable_list = new ArrayList();
    }

    private void Open() throws ClassNotFoundException, SQLException
    {
        Class.forName(driver);
        conn = DriverManager.getConnection(url, user, password);
    }

    private void Close()
    {
        try
        {
            if (!conn.isClosed())
            {
                conn.close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getTableList() throws SQLException
    {
        PreparedStatement pstm = conn.prepareStatement("SELECT table_name ,table_type FROM TABLES WHERE table_SCHEMA = ?");
        pstm.setString(1, databasename);
        ResultSet rs = pstm.executeQuery();
        while (rs.next())
        {
            table_list.add(rs.getString("table_name"));
            table_type_list.add(rs.getString("table_type"));
        }
        rs.close();
        pstm.close();
    }
    private void getColumnsInfo(String tablename) throws SQLException
    {
        PreparedStatement pstm = conn.prepareStatement("SELECT column_name,data_type,column_comment,is_nullable FROM COLUMNS WHERE table_schema= ? AND table_name= ?");
        pstm.setString(1, databasename);
        pstm.setString(2, tablename);
        ResultSet rs = pstm.executeQuery();
        while (rs.next())
        {
            column_list.add(rs.getString("column_name"));
            column_type_list.add(rs.getString("data_type"));
            comment_list.add(rs.getString("column_comment"));
            is_nullable_list.add("is_nullable");
        }
        column_list.add("#");
        column_type_list.add("#");
        comment_list.add("#");
        is_nullable_list.add("#");
        rs.close();
        pstm.close();
    }
    public void getDataBaseInfo()
    {
        try
        {
            Open();
            getTableList();
            for(String tablename:table_list)
            {
                getColumnsInfo(tablename);
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage()+"&"+ex.getSQLState());
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            Close();
        }
    }
   
    public void setDatabasename(String databasename)
    {
        table_list.clear();
        table_type_list.clear();
        column_list.clear();
        column_type_list.clear();
        comment_list.clear();
        is_nullable_list.clear();
        this.databasename = databasename;
    }

    public List<String> getTable_list()
    {
        return table_list;
    }

    public List<String> getTable_type_list()
    {
        return table_type_list;
    }

    public List<String> getColumn_list()
    {
        return column_list;
    }

    public List<String> getComment_list()
    {
        return comment_list;
    }

    public List<String> getColumn_type_list()
    {
        return column_type_list;
    }

    public List<String> getIs_nullable_list()
    {
        return is_nullable_list;
    }
}
