package core;

import java.sql.ResultSet;
import java.sql.SQLException;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Qna extends Database{
	String string;
	MakeJsonData mjd = null;
	
	public boolean isEmpty(String company, String number )throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_qna_infos WHERE company = ? AND number = ? order by id", company, number);
		rs.last();
		if(rs.getRow()>0){
			return false;			
		}
		return true;		
	}
	
	public void findText(){
		
	}
	
	public void findKeyword(){
		
	}
	
	public ResultSet findDB(String company, String number) throws SQLException{
		return makePstmtExecute("SELECT * FROM arsee_qna_infos WHERE company = ? AND number = ? order by id", company, number);
	}
		
	public String resultDB( String number, String company, String depth) throws SQLException{
		initializeDB();		
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_qna_infos WHERE company = ? AND number = ? order by id", company, number);
		rs.last();
		if(rs.getRow()>0){
			rs.beforeFirst();
			while(rs.next()){
				
			}
		}
		return "";
	}
	
	public String insertDB(String number, String company, String text, String ispoint, String istext, String isradio, String ischeck , String question1, String question2, String question3, String question4, String question5) throws SQLException{
		initializeDB();
		if(makePstmtUpdate("INSERT INTO arsee_qna_infos (`number`, `company`, `text`, `isPoint`, `isText`, `isRadio`, `isCheck`, `question1`, `question2`, `question3`, `question4`, `question5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", number,company,text,ispoint,istext,isradio,ischeck,question1,question2,question3,question4,question5) > 0){
			return "입력 성공";
		}				
		return "입력 실패";			
	}
	
	public String insertIsRadioDB(String number, String company, String text, String question1, String question2, String question3, String question4, String question5) throws SQLException{
		return insertDB(number, company, text, "0","0","1","0", question1, question2, question3, question4, question5);
	}
	public String insertIsCheckDB(String number, String company, String text, String question1, String question2, String question3, String question4, String question5) throws SQLException{
		return insertDB(number, company, text, "0","0","0","1", question1, question2, question3, question4, question5);		
	}
	public String insertIsPointDB(String number, String company,  String text, String question1, String question2, String question3, String question4, String question5) throws SQLException{
		return insertDB(number, company, text, "1","0","0","0", question1, question2, question3, question4, question5);
	}
	public String insertIsTextDB(String number, String company, String text) throws SQLException{
		return insertDB(number, company, text, "0","1","0","0", "NULL", "NULL", "NULL", "NULL", "NULL");		
	}
	
	
	public String deleteDB( String id ) throws SQLException{
		initializeDB();
		if(makePstmtUpdate("DELETE FROM arsee_qna_infos WHERE id = ?", id)>0){
			return id;			
		}				
		return "";
	}
	
	public String insertPeopleData( String company, String number ) throws SQLException {
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_qna_infos WHERE company = ? AND number=  ?", company, number);
		while(rs.next()){
			
			break;
		}
	}
	
	public String getQnaListJson( String company, String number ) throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_qna_infos WHERE company = ? AND number = ?",company, number);
		JSONObject result = new JSONObject();
		int i=0;
		while(rs.next()){
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			jo.put("text", rs.getObject("text"));
			if(rs.getString("isCheck").equals("1")){
				jo.put("type", "checkbox");
				jo.put("0", rs.getString("question1"));
				jo.put("1", rs.getString("question2"));
				jo.put("2", rs.getString("question3"));
				jo.put("3", rs.getString("question4"));
				jo.put("4", rs.getString("question5"));
				ja.add(jo);
			}
			else if(rs.getString("isRadio").equals("1")){
				jo.put("type", "radio");
				jo.put("0", rs.getString("question1"));
				jo.put("1", rs.getString("question2"));
				jo.put("2", rs.getString("question3"));
				jo.put("3", rs.getString("question4"));
				jo.put("4", rs.getString("question5"));
				ja.add(jo);
			}
			else if(rs.getString("isPoint").equals("1")){
				jo.put("type", "point");
				jo.put("0", rs.getString("question1"));
				jo.put("1", rs.getString("question2"));
				jo.put("2", rs.getString("question3"));
				jo.put("3", rs.getString("question4"));
				jo.put("4", rs.getString("question5"));
				ja.add(jo);				
			}
			else if(rs.getString("isText").equals("1")){
				jo.put("type", "input");
				ja.add(jo);
			}
			jo.put("id", rs.getString("id"));
			result.put(i++, ja);
		}
		System.out.println(result.toJSONString());
		return result.toJSONString();
	}
	
	
}
