import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DrillJDBCCon {

	private String driverName;
	private String url;
	private Statement st;
	private ResultSet rs;

	public DrillJDBCCon(){

		driverName="org.apache.drill.jdbc.Driver";
		url="jdbc:drill:drillbit=localhost";
	}

	public Connection getConnection() throws SQLException, ClassNotFoundException{
		Class.forName(driverName);
		Connection connection=DriverManager.getConnection(url);
		return connection;
		}
	
	public void executeQuery(String sql) throws ClassNotFoundException, SQLException{
		st=getConnection().createStatement();
		rs=st.executeQuery(sql);
		while(rs.next()){
			System.out.println(rs.getString("text")+"\n");
			}
	}
	
}


