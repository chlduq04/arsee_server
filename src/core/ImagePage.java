package core;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

public class ImagePage extends Database{
	protected String absolutepath = "C:/APM_Setup/htdocs/ARSee_Server/WebContent/ImageDatas/";	
	protected String imagePath = "../ImageDatas/";
	private String inputDate;
	protected String getPathQuery = "Select path From arsee_image_paths Where phone = ?";
	protected String getCountQuery = "SELECT count( * ) FROM arsee_image_paths WHERE phone = ?";
	public ImagePage(){
	}

	public String getImagePath(){
		return this.imagePath;		
	}
	
	public ResultSet checkImage(String phonenum) throws SQLException{
		initializeDB();
		return makePstmtExecute(getPathQuery, phonenum);
	}
	
}
