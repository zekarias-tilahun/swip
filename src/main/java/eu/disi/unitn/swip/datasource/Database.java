package eu.disi.unitn.swip.datasource;
import eu.disi.unitn.swip.util.Utility;

import java.io.File;
import java.sql.*;

public class Database {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = Utility.getProperties().get(Utility.HOST).toString();
	static final String USER = Utility.getProperties().get(Utility.USER_NAME).toString();
	static final String PASS = Utility.getProperties().get(Utility.PASSWORD).toString();

	static Connection conn = null;
	static Statement stmt = null;
	public static int verifyDatabaseExistence() {
        System.out.println("[Info]: Verifying Database settings ...");
		try {
			Class.forName(JDBC_DRIVER);
            Connection cn = null;
            try {
                cn = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (SQLException e) {
                System.out.println("[Info]: Incomplete Database settings, trying new setup!");
                return Utility.NO_DATABASE_FOUND;
            }

            ResultSet rs = null;
            try {
                Statement st = cn.createStatement();
                rs = st.executeQuery("SELECT * FROM echocardiogram");
                rs.first();
            } catch (SQLException e) {
                System.out.println("[Info]: Incomplete Database settings, trying new setup!");
                return Utility.NO_TABLE_FOUND;
            }

		} catch (ClassNotFoundException e) {
            System.out.println("[Info]: Incomplete Database settings, trying new setup!");
            e.printStackTrace();
			return Utility.MYSQL_DRIVER_ERROR;
		}
        System.out.println("[Info]: Database verifications completed successfully!");
		return Utility.DATABASE_CONNECTION_COMPLETED;
	}
	public static void connect(){
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void close(){
		if(conn!=null){
			try {
				conn.close();
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static int insert(String sql) throws SQLException {
        System.out.println("[Info]: Populating the table!");
		return stmt.executeUpdate(sql);
	}
    public static int createDatabase() throws SQLException {
        int result = -1;
        try {
            Class.forName(JDBC_DRIVER);
            String url = DB_URL.substring(0, DB_URL.lastIndexOf(File.separator));
            Connection cn = DriverManager.getConnection(url, USER, PASS);
            Statement st = cn.createStatement();
            result = st.executeUpdate("CREATE DATABASE IF NOT EXISTS `bigdata_profiling` /*!40100 DEFAULT CHARACTER SET utf8 */;");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("[Info]: Trying to create the database, if it doesn't exist!");
        return result;
    }
    public static int createTable() throws SQLException {
        System.out.println("[Info]: Trying to create the table, if it doesn't exist!");
        return stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `echocardiogram` (" +
                "`A` int(11) DEFAULT NULL," +
                "`B` int(11) DEFAULT NULL," +
                "`C` int(11) DEFAULT NULL," +
                "`D` int(11) DEFAULT NULL," +
                "`E` double DEFAULT NULL," +
                "`F` double DEFAULT NULL," +
                "`G` double DEFAULT NULL," +
                "`H` int(11) DEFAULT NULL," +
                "`I` double DEFAULT NULL," +
                "`J` double DEFAULT NULL," +
                "`K` varchar(45) DEFAULT NULL," +
                "`L` int(11) DEFAULT NULL," +
                "`M` int(11) DEFAULT NULL " +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
    }
	public static ResultSet execute(String sql){
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
}
