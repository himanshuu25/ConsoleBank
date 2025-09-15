package db.util;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	
	public static Connection connectOracle()throws Exception{
	    InputStream in=DBUtil.class.getClassLoader().getResourceAsStream("o.properties");
	    Properties p=new Properties();
	    p.load(in);
	    Class.forName(p.getProperty("driver"));
	    Connection con=DriverManager.getConnection(p.getProperty("url"), p);
	    return con;
	}
	public static void close(Connection con,Statement st,ResultSet rs)throws Exception{
		if(rs!=null)
			rs.close();
		if(con!=null)
			con.close();
		if(st!=null)
			st.close();
	}
	
}
