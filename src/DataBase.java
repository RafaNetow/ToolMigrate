
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rafael
 */
public abstract class  DataBase {
    ConnectionString currentConnection = new ConnectionString();
    Connection conn;
    public DataBase(){
       
    }
    void setConnection(ConnectionString connection){
        this.currentConnection.dataBaseName = connection.dataBaseName;
       this.currentConnection.password = connection.password;
       this.currentConnection.portNumber = connection.portNumber;
       this.currentConnection.serverName = connection.serverName;
       this.currentConnection.userName = connection.userName;
    }
    
    abstract Result test();
    abstract Result migrateToOracle(Connection conn2);
    abstract Result migrateToDb2(Connection conn2);
    abstract List<String> getTables();
    abstract ResultSet getDataOfTable(String tableName);
    abstract  String getColumnInfo(String tableName);
    abstract boolean createTables(String columnInfo, String tableName,Connection connect);
    
    abstract boolean insertData(String table, Connection connect);
    abstract List<String> getColumnsNames(String table);
    
   
    
    
    

    
}
