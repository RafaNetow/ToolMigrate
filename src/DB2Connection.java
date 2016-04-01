
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Rafael
 */
public class DB2Connection  {
    public static void main(String[] args) {
        
    
        
        String jdbcClassName="com.ibm.db2.jcc.DB2Driver";
        String url="jdbc:db2://localhost:50000/test1";
        String user="rafael";
        String password="0000";
 
        Connection connection = null;
        try {
            //Load class into memory
            Class.forName(jdbcClassName);
            //Establish connection
            connection = DriverManager.getConnection(url, user, password);
 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(connection!=null){
                System.out.println("Connected successfully.");
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
 
    }

                }
