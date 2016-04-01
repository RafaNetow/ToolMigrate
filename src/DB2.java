/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;/*
/**
 *
 * @author Rafael
 */
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
public class DB2 extends DataBase {
    Connection conn;
    private Object ListTableModel;
    public DB2() {
        
    }

    @Override
    Result test() {
       /*
        String jdbcClassName="com.ibm.db2.jcc.DB2Driver";
        String url="jdbc:db2://localhost:50000/test1";
        String user="rafael";
        String password="0000";
 
       */
        String jdbcClassName="com.ibm.db2.jcc.DB2Driver";
        String url="jdbc:db2://"+currentConnection.serverName+":"+currentConnection.portNumber+"/"+currentConnection.dataBaseName;
        String user= this.currentConnection.userName;
        String password= this.currentConnection.password;
 
     
        
        try {
            //Load class into memory
            Class.forName(jdbcClassName);
            //Establish connection
            this.conn = DriverManager.getConnection(url, user, password);
            
 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(conn!=null){
                System.out.println("Connected successfully.");
                JOptionPane.showMessageDialog(null, "Connected succesfully");
                System.out.println("Connected successfully.");
               
            ResultSet rs;
            
            }
        }
        return new Result();
    }

    @Override
    List<String> getTables() {
     Statement st = null;
        try {
               st = conn.createStatement();
              String query = "SELECT table_name FROM user_tables where TABLE_SCHEMA = '"+this.currentConnection.userName.toUpperCase()+"'"; 
               ResultSet rs = st.executeQuery(query);
              List<String> tables = new ArrayList(); 
              while(rs.next()){
                 tables.add( rs.getString(1));
              }
              return tables;
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
            return new ArrayList();
    }

    @Override
    Result migrateToOracle(Connection conn2) {
            List<String>tablesName =  this.getTables();
         List<String>ColumnsInfoToQuery = new ArrayList();
         List<ResultSet>tablesData = new ArrayList();
         for(int i = 0; i<tablesName.size();i++){
                   ColumnsInfoToQuery.add(getColumnInfo(tablesName.get(i)));   
         }
         for(int i = 0; i<tablesName.size();i++){
             createTables(ColumnsInfoToQuery.get(i),tablesName.get(i),conn2);
         }
         for(int i = 0; i<tablesName.size();i++){
             this.insertData(tablesName.get(i), conn2);
         }
         
   
    
    return new Result();
    }

   

   
    @Override
    ResultSet getDataOfTable(String tableName) {
       PreparedStatement stmt;
      ResultSet rs=null;
        try {
             stmt= conn.prepareStatement("SELECT * FROM "+ tableName);
             stmt.executeQuery();
        
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs; //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    Result migrateToDb2(Connection conn2) {
        List<String>tablesName =  this.getTables();
         List<String>ColumnsInfoToQuery = new ArrayList();
         List<ResultSet>tablesData = new ArrayList();
         for(int i = 0; i<tablesName.size();i++){
                   ColumnsInfoToQuery.add(getColumnInfo(tablesName.get(i)));   
         }for(int i = 0; i<tablesName.size();i++){
             createTables(ColumnsInfoToQuery.get(i),tablesName.get(i),conn2);
         } 
        
//To change body of generated methods, choose Tools | Templates.
       return new Result();
    }

    @Override
    boolean createTables(String columnInfo, String tableName, Connection connect) {
        try {
            Statement st = connect.createStatement();
            String query = "CREATE TABLE " + tableName +" ("+columnInfo+")";
             st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
         return true;
    }

   

    @Override
    String getColumnInfo(String tableName) {
        ResultSet ss = null;
        Statement st = null;
        try {
            st = this.conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DB2.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String query = "select*from "+"user_tables";
          ss =  st.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement ps = null;
            String query = "select TYPENAME,COLNAME,LENGTH from SYSCAT.COLUMNS where TABNAME = '" + tableName.toUpperCase() + "'";
            String CF = "";
            ArrayList<String> data = new ArrayList<String>();
            try{
                
            ps=conn.createStatement();
            ResultSet rs=ps.executeQuery(query);
            
            while (rs.next())
            {
                String table = "i";
                                                                     
                if(rs.getString("TYPENAME").equals("CHAR") || rs.getString("TYPENAME").equals("CHARACTER"))
                          table =rs.getString("COLNAME")+" "+rs.getString("TYPENAME")+"("+rs.getString("LENGTH")+")";
                else{
                        table =rs.getString("COLNAME")+" "+rs.getString("TYPENAME");
                                 }
                data.add(table);
            }

            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            for(int i =0; i<data.size();i++){
                CF = CF+data.get(i).toString()+",";
            }
            return CF.substring(0, CF.length()-1);
              
}

    @Override
    boolean insertData(String table, Connection connect) {
       
            
        Statement st=null;
            Statement st2 = null;
        try {
            st2 = connect.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            st = this.conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
            ResultSet rs = null;
         
            List<String> ColumnsNames = new ArrayList();
            ColumnsNames = this.getColumnsNames(table);
        /*
            try {
            st = this.conn.createStatement();
             String query = "SELECT COUNT(*) FROM " +table;
         rs =  st.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       
        try {
          rs =  st.executeQuery("Select*From "+"user_tables");
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        try {
            while(rs.next()){
              String Values = "i";
          
                for(int i = 0; i<ColumnsNames.size();i++){
       
                    Values = Values+",'"+rs.getString(ColumnsNames.get(i))+"'";
                }
               String query = "INSERT INTO "+table+" VALUES "+"("+Values.substring(2, Values.length()-1)+"\'"+ ")";
               st2.executeUpdate(query);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    List<String> getColumnsNames(String tableName) {
          ArrayList<String> columnsNames = new ArrayList<String>();
        try {
            String query = "select TYPENAME,COLNAME,LENGTH from SYSCAT.COLUMNS where TABNAME = '" + tableName + "'";
          
            
            
            Statement ps = conn.createStatement();
            ResultSet rs=ps.executeQuery(query);
            
            while (rs.next())
            {
                columnsNames.add(rs.getString("COLNAME"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
          
          
        return columnsNames; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
