package core;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;


public class Database {
	protected final static String AESKEY = "arseekeysetforsession";
	static boolean DEBUG_MODE = true;
	protected static String DB_DRIVER = "jdbc:mysql://localhost:3306/arsee";
	protected static String DB_USER= "root";
	protected static String DB_PWD= "arseejd";
	protected Connection conn = null;
	protected Statement stmt = null;
	protected PreparedStatement pstmt = null;
	protected String absolutepath = "C:/APM_Setup/htdocs/ARSee_Server/WebContent/ImageDatas/";	

	
	public void cout(Object obj){
		if(DEBUG_MODE){
			System.out.println(obj.toString());
		}
	}
	public void initializeDB() throws SQLException{
		conn = DriverManager.getConnection(DB_DRIVER, DB_USER, DB_PWD);
	}
		
	public ResultSet makePstmtExecute(String query, String ...setStr) throws SQLException{
		pstmt = conn.prepareStatement(query);
		int count = 1;
		for( int i=0 ; i < setStr.length ; i++ ){
			pstmt.setString(count++, setStr[i]);
		}
		cout(pstmt);		
		return pstmt.executeQuery();
	}
	
	public int makePstmtUpdate(String query, String ...setStr) throws SQLException{
		pstmt = conn.prepareStatement(query);
		int count = 1;
		for( int i=0 ; i < setStr.length ; i++ ){
			pstmt.setString(count++, setStr[i]);
		}
		cout(pstmt);		
		return pstmt.executeUpdate();
	}	
	
	
	
	// Time setting
	public int culTimeToSecond(String times){
		String time[] = times.split(":");
		return Integer.parseInt(time[0])*3600 + Integer.parseInt(time[1])*60 + Integer.parseInt(time[2]);
	}
	
	public int culTimeToSecond(String h,String m,String s){
		return Integer.parseInt(h)*3600 + Integer.parseInt(m)*60 + Integer.parseInt(s);		
	}
	
	public int culTimeToSecond(int h,int m,int s){
		return h*3600 + m*60 + s;		
	}
	
	public String culSecondToTime(int times){
		return culTimeZero(times/3600)+":"+culTimeZero(times%3600/60)+":"+culTimeZero(times%3600%60);
	}
	
	public String culTimeZero(int time){
		if(time == 0){
			return "00";
		}else if(0 < time && time < 10){
			return "0" + time;
		}else{
			return ""+time;
		}
	}
	
	
	
	// Find max resource
	public double findLCS(String oldval,String newval){
		oldval = oldval.replaceAll("\\s","");
		newval = newval.replaceAll("\\s","");
		int matchs[][] = new int[oldval.length()+1][newval.length()+1];
		int match = 0, unmatch = 0;
		// draw match table
		for(int i=0;i<oldval.length()+1;i++){
			for(int j=0;j<newval.length()+1;j++){
				if(i == 0 || j == 0){
					matchs[i][j] = 0;
				}else{
					if(oldval.charAt(i-1) == newval.charAt(j-1)){
						matchs[i][j] = findMax(matchs[i-1][j-1]+1,matchs[i][j-1],matchs[i-1][j]);
					}else{
						matchs[i][j] = findMax(matchs[i-1][j-1],matchs[i][j-1],matchs[i-1][j]);
					}
				}
			}
		}
		//
		int i = oldval.length();
		int j = newval.length();
		while(true){
			if(i==0&&j==0){
				break;
			}
			if(oldval.charAt(i-1) == newval.charAt(j-1)){
				i--;
				j--;
				match++;
			}else{
				unmatch++;
				if(matchs[i-1][j]>matchs[i][j-1]){
					i--;
				}else if(matchs[i-1][j]<matchs[i][j-1]){
					j--;
				}else{
					if(i==0){
						j--;
					}else if(j==0){
						i--;
					}else{
						i--;
					}
				}
				unmatch++;
			}
		}
		return match*10/newval.length();
	}
	
	public int findMax(int ...args){
		int max = args[0];
		for(int i=0;i<args.length;i++){
			if(max < args[i]){
				max = args[i];
			}
		}
		return max;
	}

}
