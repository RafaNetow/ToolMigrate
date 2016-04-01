/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rafael
 */
public class ConnectionString {
    public String serverName;
    public String portNumber;
    public String userName;
    public String password;

    public ConnectionString(){}
    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }
     public String dataBaseName; 

    public String getServerName() {
        return serverName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }
    

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }
    
}
