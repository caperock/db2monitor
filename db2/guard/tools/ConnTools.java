package db2.guard.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import db2.guard.tools.DatabaseBean;

public class ConnTools { 
    private static String dirverClassName ;//= "com.ibm.db2.jcc.DB2Driver"; 
    private static String url;// = "jdbc:db2://9.125.40.165:50000/sample"; 
    private static String user ;//= "db2v97"; 
    private static String password;// = "db2v97"; 
    
    
    public static Connection makeConnection(DatabaseBean oDB) { 
    	dirverClassName = "com.ibm.db2.jcc.DB2Driver";
    	url = "jdbc:db2://" + oDB.getIPAddr()+":" +oDB.getPort()+ "/" + oDB.getDBName();
    	user = oDB.getUser();
    	password = oDB.getPassword();
    	Properties properties = new Properties(); // Create Properties object
    	properties.put("user", user);         // Set user ID for connection
    	properties.put("password", password);     // Set password for connection
    	properties.put("clientProgramName", "DataGuard");    	

    	
    	
        Connection conn = null; 
        
        try { 
                Class.forName(dirverClassName); 
        } catch (ClassNotFoundException e) { 
                e.printStackTrace(); 
        } 
        try { 
        	   // conn = DriverManager.getConnection(url, user, password);
        	 conn = DriverManager.getConnection(url, properties);
        	     	 



        } catch (SQLException e) { 
                e.printStackTrace(); 
        } 
        return conn; 
} 

    public static Connection makeConnection() { 
            Connection conn = null; 
            try { 
                    Class.forName(dirverClassName); 
            } catch (ClassNotFoundException e) { 
                    e.printStackTrace(); 
            } 
            try { 
                    conn = DriverManager.getConnection(url, user, password); 
            } catch (SQLException e) { 
                    e.printStackTrace(); 
            } 
            return conn; 
    } 
}
