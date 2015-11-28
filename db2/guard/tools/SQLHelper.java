package db2.guard.tools;

import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

public class SQLHelper
{
	
	static DatabaseBean oDB = new DatabaseBean();
	static Properties properties = new Properties(); // Create Properties object
	
    /**
     * ����
     */
  //  public static String driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static String driver = "com.ibm.db2.jcc.DB2Driver";
	
    /**
     * �����ַ���
     */
    //public static String url = "jdbc:microsoft:sqlserver://192.168.24.246:1433;DatabaseName=LiNing";
	public static String url;
    /**
     * �û���
     */
    public static String user = "sa";
    /**
     * ����
     */
    public static String password = "123456";
    /**
     * ������ʵ��������
     */
       
    private SQLHelper()
    {
    	
    	
    }
    /**
     * ��ȡһ�����ݿ�����
     * ͨ���������  driver / url / user / password ���ĸ���̬������ �������ݿ���������
     * @return ���ݿ�����
     */
    public static Connection getConnection(
    		 DatabaseBean oDB ){
    	driver = "com.ibm.db2.jcc.DB2Driver";
    	url = "jdbc:db2://" + oDB.getIPAddr()+":" +oDB.getPort()+ "/" + oDB.getDBName();
    	user = oDB.getUser();
    	password = oDB.getPassword();

    	properties.put("user", user);         // Set user ID for connection
    	properties.put("password", password);     // Set password for connection
    	properties.put("clientProgramName", "DataGuard");   
    	
        try
        {
            // ��ȡ����,����ʹ�õ��� sqljdbc_1.2.2828.100_chs.exe,��ͬ�汾������,���������ͬ
            Class.forName(driver);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            //return DriverManager.getConnection(url, user, password);
            
            return DriverManager.getConnection(url, properties);
            
        } catch (Exception ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Connection getConnection(){
    	 try
         {
             // ��ȡ����,����ʹ�õ��� sqljdbc_1.2.2828.100_chs.exe,��ͬ�汾������,���������ͬ
             Class.forName(driver);
         } catch (ClassNotFoundException ex)
         {
             Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
         }
         try
         {
             return DriverManager.getConnection(url, user, password);
             
            // return DriverManager.getConnection(url, properties);
             
         } catch (Exception ex)
         {
             Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
             return null;
         }
     
    }
    /**
     * ��ȡһ�� Statement
     * �� Statement �Ѿ��������ݼ� ���Թ���,���Ը���
     * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
     */
    public static Statement getStatement()
    {
        Connection conn = getConnection();
        if (conn == null)
        {
            return null;
        }
        try
        {
            return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        // �������ݼ����Թ���,���Ը���
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            close(conn);
        }
        return null;
    }
    /**
     * ��ȡһ�� Statement
     * �� Statement �Ѿ��������ݼ� ���Թ���,���Ը���
     * @param conn ���ݿ�����
     * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
     */
    public static Statement getStatement(Connection conn)
    {
        if (conn == null)
        {
            return null;
        }
        try
        {
        	return conn.createStatement();
            //return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        // �������ݼ����Թ���,���Ը���
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    /**
     * ��ȡһ���������� PreparedStatement
     * �� PreparedStatement �Ѿ��������ݼ� ���Թ���,���Ը���
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
     */
    public static PreparedStatement getPreparedStatement(String cmdText, Object... cmdParams)
    {
        Connection conn = getConnection();
        if (conn == null)
        {
            return null;
        }
        PreparedStatement pstmt = null;
        try
        {
            pstmt = conn.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            int i = 1;
            for (Object item : cmdParams)
            {
                pstmt.setObject(i, item);
                i++;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            close(conn);
        }
        return pstmt;
    }
    /**
     *  ��ȡһ���������� PreparedStatement
     * �� PreparedStatement �Ѿ��������ݼ� ���Թ���,���Ը���
     * @param conn ���ݿ�����
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
     */
    public static PreparedStatement getPreparedStatement(Connection conn, String cmdText, Object... cmdParams)
    {
        if (conn == null)
        {
            return null;
        }
        PreparedStatement pstmt = null;
        try
        {
            pstmt = conn.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            int i = 1;
            for (Object item : cmdParams)
            {
                pstmt.setObject(i, item);
                i++;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            close(pstmt);
        }
        return pstmt;
    }
    /**
     * ִ�� SQL ���,���ؽ��Ϊ����
     * ��Ҫ����ִ�зǲ�ѯ���
     * @param cmdText SQL ���
     * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
     */
    public static int ExecSql(String cmdText)
    {
        Statement stmt = getStatement();
        if (stmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = stmt.executeUpdate(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            i = -1;
        }
        closeConnection(stmt);
        return i;
    }
    /**
     * ִ�� SQL ���,���ؽ��Ϊ����
     * ��Ҫ����ִ�зǲ�ѯ���
     * @param cmdText SQL ���
     * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
     */
    public static int ExecSql(Connection conn, String cmdText)
    {
        Statement stmt = getStatement(conn);
        if (stmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = stmt.executeUpdate(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            i = -1;
        }
        close(stmt);
        return i;
    }
    /**
     * ִ�� SQL ���,���ؽ��Ϊ����
     * ��Ҫ����ִ�зǲ�ѯ���
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
     */
    public static int ExecSql(String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
        if (pstmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = pstmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            i = -1;
        }
        closeConnection(pstmt);
        return i;
    }
    /**
     * ִ�� SQL ���,���ؽ��Ϊ����
     * ��Ҫ����ִ�зǲ�ѯ���
     * @param conn ���ݿ�����
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
     */
    public static int ExecSql(Connection conn, String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
        if (pstmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = pstmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            i = -1;
        }
        close(pstmt);
        return i;
    }
    /**
     * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
     * @param cmdText SQL ���
     * @return
     */
    public static Object ExecScalar(String cmdText)
    {
        ResultSet rs = getResultSet(cmdText);
        Object obj = buildScalar(rs);
        closeConnection(rs);
        return obj;
    }
    /**
     * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
     * @param conn ���ݿ�����
     * @param cmdText SQL ���
     * @return
     */
    public static Object ExecScalar(Connection conn, String cmdText)
    {
        ResultSet rs = getResultSet(conn, cmdText);
        Object obj = buildScalar(rs);
        closeEx(rs);
        return obj;
    }
    /**
     * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return
     */
    public static Object ExecScalar(String cmdText, Object... cmdParams)
    {
        ResultSet rs = getResultSet(cmdText, cmdParams);
        Object obj = buildScalar(rs);
        closeConnection(rs);
        return obj;
    }
    /**
     * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
     * @param conn ���ݿ�����
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return
     */
    public static Object ExecScalar(Connection conn, String cmdText, Object... cmdParams)
    {
        ResultSet rs = getResultSet(conn, cmdText, cmdParams);
        Object obj = buildScalar(rs);
        closeEx(rs);
        return obj;
    }
    /**
     * ����һ�� ResultSet
     * @param cmdText SQL ���
     * @return
     */
    public static ResultSet getResultSet(String cmdText)
    {
        Statement stmt = getStatement();
        if (stmt == null)
        {
            return null;
        }
        try
        {
            return stmt.executeQuery(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection(stmt);
        }
        return null;
    }
    /**
     * ����һ�� ResultSet
     * @param conn
     * @param cmdText SQL ���
     * @return
     */
    public static ResultSet getResultSet(Connection conn, String cmdText)
    { 
        Statement stmt = getStatement(conn);
        if (stmt == null)
        {
            return null;
        }
        try
        {
        	
            return stmt.executeQuery(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            close(stmt);
        }
        return null;
    }
    /**
     * ����һ�� ResultSet
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return
     */
    public static ResultSet getResultSet(String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
        if (pstmt == null)
        {
            return null;
        }
        try
        {
            return pstmt.executeQuery();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection(pstmt);
        }
        return null;
    }
    /**
     * ����һ�� ResultSet
     * @param conn ���ݿ�����
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return
     */
    public static ResultSet getResultSet(Connection conn, String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
        if (pstmt == null)
        {
            return null;
        }
        try
        {
            return pstmt.executeQuery();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            close(pstmt);
        }
        return null;
    }
    public static Object buildScalar(ResultSet rs)
    {
        if (rs == null)
        {
            return null;
        }
        Object obj = null;
        try
        {
            if (rs.next())
            {
                obj = rs.getObject(1);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

   
  
 
    /**
     * ��ȡһ�����и��¹��ܵ�����ģ�� ���ֻҪ��ȡ���ݣ��Ͳ�Ҫ������
     * @param cmdText �ܷ���һ�����ݼ��Ĳ�ѯSQL ���
     * @return �������ģ��
     * 
     * 
     * DataSet û���ҵ����ĸ�����,��Ϊ��ʱ�ò�������ʡ�Դ˷���
     
    public static DataSet getDataSet(String cmdText)
    {
        Statement stmt = getStatement();
        DataSet dbc = new DataSet();
        if (stmt == null)
        {
            dbc.code = -2;
            return dbc;
        }
        try
        {
            // ��ѯ���
            dbc.rs = stmt.executeQuery(cmdText);
            dbc.model = buildTableModel(dbc.rs);
            dbc.code = dbc.model.getRowCount();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            dbc.code = -1;
        }
        return dbc;
    }
    */
    
    /**
     * ��ȡһ�����и��¹��ܵ�����ģ�� ���ֻҪ��ȡ���ݣ��Ͳ�Ҫ������
     * @param conn ���ݿ�����
     * @param cmdText �ܷ���һ�����ݼ��Ĳ�ѯSQL ���
     * @return �������ģ��
     * 
     * ͬ��һ������
     
    public static DataSet getDataSet(Connection conn, String cmdText)
    {
        Statement stmt = getStatement(conn);
        DataSet dbc = new DataSet();
        if (stmt == null)
        {
            dbc.code = -2;
            return dbc;
        }
        try
        {
            // ��ѯ���
            dbc.rs = stmt.executeQuery(cmdText);
            dbc.model = buildTableModel(dbc.rs);
            dbc.code = dbc.model.getRowCount();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            dbc.code = -1;
        }
        return dbc;
    }
    */
    /**
     * ��ȡһ�����и��¹��ܵ�����ģ�� ���ֻҪ��ȡ���ݣ��Ͳ�Ҫ������
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return �������ģ��
     * 
     * 
     * ͬ��һ������     * 
     * 
    
    public static DataSet getDataSet(String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
        DataSet dbc = new DataSet();
        if (pstmt == null)
        {
            dbc.code = -2;
            return dbc;
        }
        try
        {
            // ��ѯ���
            dbc.rs = pstmt.executeQuery();
            dbc.model = buildTableModel(dbc.rs);
            dbc.code = dbc.model.getRowCount();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            dbc.code = -1;
        }
        return dbc;
    }
    
     */
    /**
     * ��ȡһ�����и��¹��ܵ�����ģ�� ���ֻҪ��ȡ���ݣ��Ͳ�Ҫ������
     * @param conn ���ݿ�����
     * @param cmdText ��Ҫ ? ������ SQL ���
     * @param cmdParams SQL ���Ĳ�����
     * @return �������ģ��
     * 
     * 
     * ͬ��
     * 
     
    public static DataSet getDataSet(Connection conn, String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
        DataSet dbc = new DataSet();
        if (pstmt == null)
        {
            dbc.code = -2;
            return dbc;
        }
        try
        {
            // ��ѯ���
            dbc.rs = pstmt.executeQuery();
            dbc.model = buildTableModel(dbc.rs);
            dbc.code = dbc.model.getRowCount();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
            dbc.code = -1;
        }
        return dbc;
    }
    */
    private static void close(Object obj)
    {
        if (obj == null)
        {
            return;
        }
        try
        {
            if (obj instanceof Statement)
            {
                ((Statement) obj).close();
            } else if (obj instanceof PreparedStatement)
            {
                ((PreparedStatement) obj).close();
            } else if (obj instanceof ResultSet)
            {
                ((ResultSet) obj).close();
            } else if (obj instanceof Connection)
            {
                ((Connection) obj).close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void closeEx(Object obj)
    {
        if (obj == null)
        {
            return;
        }
        try
        {
            if (obj instanceof Statement)
            {
                ((Statement) obj).close();
            } else if (obj instanceof PreparedStatement)
            {
                ((PreparedStatement) obj).close();
            } else if (obj instanceof ResultSet)
            {
                ((ResultSet) obj).getStatement().close();
            } else if (obj instanceof Connection)
            {
                ((Connection) obj).close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void closeConnection(Object obj)
    {
        if (obj == null)
        {
            return;
        }
        try
        {
            if (obj instanceof Statement)
            {
                ((Statement) obj).getConnection().close();
            } else if (obj instanceof PreparedStatement)
            {
                ((PreparedStatement) obj).getConnection().close();
            } else if (obj instanceof ResultSet)
            {
                ((ResultSet) obj).getStatement().getConnection().close();
            } else if (obj instanceof Connection)
            {
                ((Connection) obj).close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

