package core;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Calendar;

public class Database {
	
	protected final static String ARS_DATA_TYPE_INFO = "info";
	protected final static String ARS_DATA_TYPE_NORMAL = "normal";
	protected final static String ARS_DATA_TYPE_ERROR = "error";
	protected final static String ARS_DATA_TYPE_STAR = "star";
	protected final static String ARS_DATA_TYPE_SHARP = "sharp";
	protected final static String ARS_DATA_SPLIT_KEY = "--";
	
	private final static String ARS_DBNAME_DAY_INFO = "arsee_ars_infos";
	private final static String ARS_DBNAME_DAY_INFO_OTHER = "arsee_ars_other_infos";
	private final static String ARS_DBNAME_DAY_INFO_UPDATE = "arsee_ars_infos_update";
	private final static String ARS_DBNAME_DAY_INFO_KEYWORD = "arsee_kwrds";

	private final static String ARS_DBNAME_HOLIDAY_INFO = "arsee_ars_infos_holiday";
	private final static String ARS_DBNAME_HOLIDAY_INFO_OTHER = "arsee_ars_other_infos_holiday";
	private final static String ARS_DBNAME_HOLIDAY_INFO_UPDATE = "arsee_ars_infos_update_holiday";
	private final static String ARS_DBNAME_HOLIDAY_INFO_KEYWORD = "arsee_kwrds_holiday";
	
	private static int ARS_CHECK_HOLIDAY = 0;
	
	protected static String ARS_DBNAME_NOW_INFO = ARS_DBNAME_DAY_INFO;
	protected static String ARS_DBNAME_NOW_INFO_OTHER = ARS_DBNAME_DAY_INFO_OTHER;
	protected static String ARS_DBNAME_NOW_INFO_UPDATE = ARS_DBNAME_DAY_INFO_UPDATE;
	protected static String ARS_DBNAME_NOW_INFO_KEYWORD = ARS_DBNAME_DAY_INFO_KEYWORD;
	
	protected final static String AESKEY = "arseekeysetforsession";
	protected final static boolean DEBUG_MODE = true;
	protected final static String DB_DRIVER = "jdbc:mysql://localhost:3306/arsee";
	protected final static String DB_USER= "root";
	protected final static String DB_PWD= "arseejd";
	
	protected Connection conn = null;
	protected Statement stmt = null;
	protected PreparedStatement pstmt = null;
	protected String absolutepath = "C:/APM_Setup/htdocs/ARSee_Server/WebContent/ImageDatas/";	
	
	protected String[][] Indexing = {
			{"영번", "영 번", "0 번", "0번"},
			{"일번", "일 번", "1 번", "1번"},
			{"이번", "이 번", "2 번", "2번"},
			{"삼번", "삼 번", "3 번", "3번"},
			{"사번", "사 번", "4 번", "4번"},
			{"오번", "오 번", "5 번", "5번"},
			{"육번", "육 번", "6 번", "6번"},
			{"칠번", "칠 번", "7 번", "7번"},
			{"팔번", "팔 번", "8 번", "8번"},
			{"구번", "구 번", "9 번", "9번"}
	};


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

	
	public void setIsDayOrIsHoliday(){
		Calendar cal = Calendar.getInstance ();
		int now_check = cal.get(Calendar.DAY_OF_WEEK);
		if(ARS_CHECK_HOLIDAY != now_check){
			if( now_check >= 2 && now_check <= 6 ){
				ARS_DBNAME_NOW_INFO = ARS_DBNAME_DAY_INFO;
				ARS_DBNAME_NOW_INFO_OTHER = ARS_DBNAME_DAY_INFO_OTHER;
				ARS_DBNAME_NOW_INFO_KEYWORD = ARS_DBNAME_DAY_INFO_KEYWORD;
				ARS_DBNAME_NOW_INFO_UPDATE = ARS_DBNAME_DAY_INFO_UPDATE;
			}else{
				ARS_DBNAME_NOW_INFO = ARS_DBNAME_HOLIDAY_INFO;
				ARS_DBNAME_NOW_INFO_OTHER = ARS_DBNAME_HOLIDAY_INFO_OTHER;
				ARS_DBNAME_NOW_INFO_KEYWORD = ARS_DBNAME_HOLIDAY_INFO_KEYWORD;
				ARS_DBNAME_NOW_INFO_UPDATE = ARS_DBNAME_HOLIDAY_INFO_UPDATE;
			}
			ARS_CHECK_HOLIDAY = now_check;
		}
	}
}
