package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Properties;
import java.util.Map;
import org.postgresql.ds.PGPoolingDataSource;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

import javax.servlet.http.HttpServletRequest;

import hello.DBUtil;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
		return new Greeting(counter.incrementAndGet(),String.format(template, name));
    }
	@RequestMapping("/contextlist")
    public List contextlist(@RequestParam(value="filterBy", defaultValue="all") String filterBy) {
		List toRet = new ArrayList();
		try{
            Connection conn = DBUtil.getInstance().getDBConn();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM CONTEXT;";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
				Properties contextProps = new Properties();	
            	contextProps.put("ID",rs.getInt("ID"));
            	contextProps.put("contextName",rs.getString("CONTEXT_NAME"));
            	contextProps.put("year",rs.getInt("YEAR"));
            	contextProps.put("descr",rs.getString("DESCRIPTION"));
            	toRet.add(contextProps);
            }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return toRet;
       
    }
    @RequestMapping("/contentList")
    public List contentList(@RequestParam(value="dirName", defaultValue="all") String dirName) {
        List toRet = new ArrayList();
        try{
            String directoryName = "E:\\AMaven\\gs-maven-master\\complete\\src\\main\\webapp\\"+dirName;//nagalapuram";
            File directory = new File(directoryName);
            //get all the files from a directory
            File[] fList = directory.listFiles();
            if(fList != null){
                for (File file : fList){
                    String fileName = file.getName();
                    System.out.println(fileName);
                    toRet.add(fileName);
                }
            }
            System.out.println("printing the complete output::::"+toRet);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return toRet;
       
    }
    @RequestMapping(value="/exp/add", method=RequestMethod.POST)
    public String addExperience(HttpServletRequest request){
        String toRet = "aa";
        String place = request.getParameter("place");
        String expText = request.getParameter("expText");
        String athr = request.getParameter("athr");
        Connection conn = DBUtil.getInstance().getDBConn();
        try{
            if(place != null && expText != null && athr != null)
            {
                if(conn == null)
                {
                    return "Unable to Connect to DB";
                }
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO CONTEXTEXPERIENCE(CONTEXT_NAME,EXPERIENCE,AUTHOR) Values('"+place+"','"+expText+"','"+athr+"');";
                stmt.execute(sql);
                toRet = "Successfully inserted values in to DB";
            }
        }
        catch(Exception e){
            e.printStackTrace();
            toRet = "Failed inserting values in to DB";
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
        return toRet;
    }
    @RequestMapping(value="/exp/get", method=RequestMethod.GET)
    public List getExperiences(HttpServletRequest request){
        List toRet = new ArrayList();
        String place = request.getParameter("place");
        Connection conn = DBUtil.getInstance().getDBConn();
        try{
            if(place != null)
            {
                if(conn == null)
                {
                    System.out.println("No DB Connection");
                    return toRet;
                }
                Statement stmt = conn.createStatement();
                String sql = "SELECT * FROM CONTEXTEXPERIENCE WHERE CONTEXT_NAME = '"+place+"';";
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    Properties props = new Properties();
                    props.put("exp",rs.getString("EXPERIENCE"));
                    props.put("athr",rs.getString("AUTHOR"));
                    toRet.add(props);
                }
            }
        }
        catch(Exception e){
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
        return toRet;
    }
}