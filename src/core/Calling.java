package core;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Calling extends Database{
	String string;
	MakeJsonData mjd = null;

	public String findDB(String company, String number, String start, String length) throws SQLException{
		JSONObject result = new JSONObject();
		if(start == null || length == null || start == "" || length == ""){
			ResultSet rs = makePstmtExecute("SELECT * FROM arsee_calling_infos WHERE company = ? AND who = ? order by date desc limit 0, 10", company, number);
			while(rs.next()){
				result.put(rs.getString("id"), rs.getString("date")+ARS_DATA_SPLIT_KEY+rs.getString("where")+ARS_DATA_SPLIT_KEY+rs.getString("number"));
			}
			return result.toJSONString();
		}
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_calling_infos WHERE company = ? AND who = ? order by date desc limit ?, ?", company, number, start, length);
		while(rs.next()){
			result.put(rs.getString("id"), rs.getString("date")+ARS_DATA_SPLIT_KEY+rs.getString("where")+ARS_DATA_SPLIT_KEY+rs.getString("number"));
		}
		return result.toJSONString();
	}

	public String findDB(String company, String number, String order) throws SQLException{
		JSONObject result = new JSONObject();
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_calling_infos WHERE company = ? AND who = ? AND date(date) = date(subdate(now(), INTERVAL ? DAY))  order by date desc", company, number, order);
		while(rs.next()){
			result.put(rs.getString("id"), rs.getString("date")+ARS_DATA_SPLIT_KEY+rs.getString("where")+ARS_DATA_SPLIT_KEY+rs.getString("number"));
		}
		return result.toJSONString();
	}
		
	public String insertDB(String company, String number, String where, String who) throws SQLException{
		initializeDB();
		if(makePstmtUpdate("INSERT INTO arsee_calling_infos (`date`, `who`, `where`, `company`, `number`) VALUES ( NOW(), ?, ?, ?, ?)", who, where, company, number ) > 0){
			return "입력 성공";
		}				
		return "입력 실패";			
	}
	
	public String deleteDB( String id ) throws SQLException{
		initializeDB();
		if(makePstmtUpdate("DELETE FROM arsee_calling_infos WHERE id = ?", id)>0){
			return id;			
		}				
		return "";
	}
		
}
