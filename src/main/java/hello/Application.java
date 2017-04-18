package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import hello.DBUtil;

import org.postgresql.ds.PGPoolingDataSource;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

@SpringBootApplication

//@Configuration
//@EnableAutoConfiguration
//@EntityScan(basePackageClasses=User.class)

public class Application {

    public static void main(String[] args) {
		System.setProperty("server.port","8060");
		/*PGPoolingDataSource source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerName("localhost");
		source.setDatabaseName("springbootdb");
		source.setUser("postgres");
		source.setPassword("akhildb");
		source.setMaxConnections(10);*/
		Connection conn = null;
		try
		{
			conn = DBUtil.getInstance().getDBConn();
			if(conn != null)
			{
		    	Statement stmt = conn.createStatement();
				/*DatabaseMetaData md = conn.getMetaData();
				ResultSet rs = md.getTables(null, null, "CONTEXT", new String[] {"TABLE"});*/
				
				//Need to check if cold start or not in a better way below check for time being
				String sqlCreate = "SELECT * FROM CONTEXT";
		    	ResultSet rs = stmt.executeQuery(sqlCreate);
				if(rs.next()){
					System.out.println("%%%%%%%%%DB population already done so should skip DB configuration%%%%%%%%%%");
				}
				else{
					System.out.println("################### Should write DB Configuration here #########################");
					/*String sqlCreate = "CREATE TABLE IF NOT EXISTS CONTEXT (ID INTEGER PRIMARY KEY,YEAR INTEGER, CONTEXT_NAME VARCHAR(100) NOT NULL,DESCRIPTION VARCHAR(250))";
			    	stmt.execute(sqlCreate);
			    	stmt = conn.createStatement();
			        String sql = "INSERT INTO CONTEXT (ID,YEAR,CONTEXT_NAME,DESCRIPTION) VALUES (1, '2016', 'Pichavaram', 'Mangrove forest road trip');";
			        stmt.executeUpdate(sql);
			        String sql = "INSERT INTO CONTEXT (ID,YEAR,CONTEXT_NAME,DESCRIPTION) VALUES (2, '2017', 'NewYear', 'Mahabhalipuram bike trip');";
			        stmt.executeUpdate(sql);
			        sql = "INSERT INTO CONTEXT (ID,YEAR,CONTEXT_NAME,DESCRIPTION) VALUES (3, '2017', 'Goa', 'First trip with roomies');";
			        stmt.executeUpdate(sql);
			        sqlCreate = "CREATE TABLE IF NOT EXISTS CONTEXTEXPERIENCE (ID INTEGER PRIMARY KEY,CONTEXT_NAME VARCHAR(100) references CONTEXT(CONTEXT_NAME),AUTHOR VARCHAR(60))";
			    	stmt.execute(sqlCreate);*/
				}

			}
			else{
				System.out.println("no Exception no Connection");
			}
		}
		catch (Exception e)
		{
			System.out.println("#######DB connection ae dobbindi#########");
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
			{
				try { conn.close(); } catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        SpringApplication.run(Application.class, args);
    }
}