
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.UIManager.getString;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rafael
 */
public class Oracle extends DataBase {
    public String controller;
    String link;
    
    public Oracle() {
        conn = null;
        this.controller = "oracle.jdbc.OracleDriver";
        this.link = "";
        
    }

    @Override
    Result test() {
        this.link = "jdbc:oracle:thin:@"+this.currentConnection.serverName+":"+this.currentConnection.portNumber+":xe";
        try{
            Class.forName(this.controller).newInstance();
            this.conn = DriverManager.getConnection(this.link,this.currentConnection.userName,this.currentConnection.password);
           
            JOptionPane.showMessageDialog(null, "Connected succesfully");
             
              this.getTables();
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Fallo al connectar");
            System.out.println(ex);
        }
        
    
        return new Result();
    }
       
       public void closeConnection(Connection conn){
        try{
            conn.close();
        }catch(Exception e){
            System.out.println("error al cerrar ");
        }
       }

    @Override
     List<String> getTables() {
           Statement st = null;
        try {
               st = conn.createStatement();
              ResultSet rs = st.executeQuery(" select * from all_tables where owner = "+"'"+this.currentConnection.userName.toUpperCase()+ "'");
              List<String> tables = new ArrayList();
              while(rs.next()){
                 tables.add( rs.getString("TABLE_NAME"));
              }
              return tables;
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
            return new ArrayList();
    }
     
     
     


    @Override
    Result migrateToDb2(Connection conn2) {
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
         
//To change 
         
         
   
    
    return new Result();
    }
    

    @Override
    boolean createTables(String columnInfo, String tableName,Connection connect) {
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
    ResultSet getDataOfTable(String tableName) {
      PreparedStatement stmt;
      ResultSet rs=null;
        try {
             stmt= conn.prepareStatement("SELECT * FROM "+ tableName);
             stmt.executeQuery();
        
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
     
    
}   @Override
    public String getColumnInfo(String tableName)
        {
            Statement ps = null;
            String query = "select DATA_TYPE,COLUMN_NAME,DATA_LENGTH from ALL_TAB_COLUMNS where TABLE_NAME = '" + tableName + "'";
            String CF = "";
            ArrayList<String> data = new ArrayList<String>();
            try{
                
            ps=conn.createStatement();
            ResultSet rs=ps.executeQuery(query);
            
            while (rs.next())
            {   String columInfo = null;
                                                                     
                if(rs.getString("DATA_TYPE").equals("CHAR"))
                          columInfo =rs.getString("COLUMN_NAME")+" "+rs.getString("DATA_TYPE")+"("+rs.getString("DATA_LENGTH")+")";
                
                else if(rs.getString("DATA_TYPE").equals("NUMBER")){
                          columInfo =rs.getString("COLUMN_NAME")+" "+"INTEGER";
                }
                else{
                        columInfo =rs.getString("COLUMN_NAME")+" "+rs.getString("DATA_TYPE");
                                 }
                data.add(columInfo);
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
    Result migrateToOracle(Connection conn2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    boolean insertData(String table,Connection conne) {
            Statement st=null;
            Statement st2 = null;
        try {
            st2 = conne.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            st = this.conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
            ResultSet rs = null;
            int cantidadDeColumnas;
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
          rs =  st.executeQuery("Select* From "+table);
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        try {
            while(rs.next()){
              String Values = "i";
          
                for(int i = 0; i<ColumnsNames.size();i++){
       
                    Values = Values+",\'"+rs.getString(ColumnsNames.get(i))+"\'";
                }
               String query = "INSERT INTO "+table+" VALUES "+"("+Values.substring(2, Values.length()-1)+"\'"+ ")";
               st2.executeUpdate(query);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
        return false;
    }

    @Override
    List<String> getColumnsNames(String tableName) {
          ArrayList<String> columnsNames = new ArrayList<String>();
        try {
            String query = "select DATA_TYPE,COLUMN_NAME,DATA_LENGTH from ALL_TAB_COLUMNS where TABLE_NAME = '" + tableName + "'";
          
            
            
            Statement ps = conn.createStatement();
            ResultSet rs=ps.executeQuery(query);
            
            while (rs.next())
            {
                columnsNames.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Oracle.class.getName()).log(Level.SEVERE, null, ex);
        }
          
          
        return columnsNames;

    }
  



}
       
       
    

