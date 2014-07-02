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
		initializeDB();
		return makePstmtExecute("SELECT * FROM arsee_qna_infos WHERE company = ? AND number = ? order by id", company, number);
	}

	public String resultDB( String number, String company) throws SQLException{
		initializeDB();		
		JSONObject jobj = new JSONObject();
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_qna_infos WHERE company = ? AND number = ? order by id", company, number);
		rs.last();
		if(rs.getRow()>0){
			rs.beforeFirst();
			int values[] = new int[]{0,0,0,0,0};
			while(rs.next()){
				JSONArray ja = new JSONArray();
				ResultSet rss = makePstmtExecute("SELECT * FROM arsee_qna_datas WHERE info_id = ? AND isText is NULL", rs.getString("id"));
				while(rss.next()){
					if(rss.getString("question1")!= null && rss.getString("question1").equals("1")){
						values[0]++;
					}else if(rss.getString("question2")!= null && rss.getString("question2").equals("1")){
						values[1]++;
					}else if(rss.getString("question3")!= null && rss.getString("question3").equals("1")){
						values[2]++;
					}else if(rss.getString("question4")!= null && rss.getString("question4").equals("1")){
						values[3]++;
					}else if(rss.getString("question5")!= null && rss.getString("question5").equals("1")){
						values[4]++;
					}
				}
				for(int i=0 ; i < values.length ; i++ ){
					JSONObject jo = new JSONObject();
					jo.put("label", i+1);
					jo.put("value", values[i]);
					ja.add(jo);
				}
				jobj.put(rs.getString("id"), ja);
			}
		}
		return jobj.toJSONString();
	}

	public String resultDB( String number, String company, String id) throws SQLException{
		cout(number);
		initializeDB();		
		JSONObject jobj = new JSONObject();
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_qna_infos WHERE company = ? AND number = ? AND id = ?", company, number, id);
		while(rs.next()){
			JSONArray ja = new JSONArray();
			ResultSet rss = makePstmtExecute("SELECT * FROM arsee_qna_datas WHERE info_id = ? AND isText = '0'", rs.getString("id"));
			float values[] = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
			while(rss.next()){
				if(rss.getString("question1")!= null && Float.parseFloat(rss.getString("question1")) > 0){
					values[0]+=Float.parseFloat(rss.getString("question1"));
				}else if(rss.getString("question2")!= null && Float.parseFloat(rss.getString("question2")) > 0){
					values[1]+=Float.parseFloat(rss.getString("question2"));
				}else if(rss.getString("question3")!= null && Float.parseFloat(rss.getString("question3")) > 0){
					values[2]+=Float.parseFloat(rss.getString("question3"));
				}else if(rss.getString("question4")!= null && Float.parseFloat(rss.getString("question4")) > 0){
					values[3]+=Float.parseFloat(rss.getString("question4"));
				}else if(rss.getString("question5")!= null && Float.parseFloat(rss.getString("question5")) > 0){
					values[4]+=Float.parseFloat(rss.getString("question5"));
				}
			}
			
			for(int i=0 ; i < values.length ; i++ ){
				JSONObject jo = new JSONObject();
				jo.put("label", i+1);
				jo.put("value", values[i]);
				ja.add(jo);
			}
			jobj.put("items", ja);
		}
		return jobj.toJSONString();
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

	public boolean insertPeopleData(String id, String text, String question1, String question2, String question3, String question4, String question5 ) throws SQLException {
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_qna_infos WHERE id = ?", id );
		while(rs.next()){
			if(makePstmtUpdate("INSERT INTO arsee_qna_datas ( `info_id`, `text`, `isPoint`, `isText`, `isCheck`, `isRadio`, `question1`, `question2`, `question3`, `question4`, `question5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", id, text, rs.getString("isPoint"), rs.getString("isText"), rs.getString("isCheck"), rs.getString("isRadio"), question1, question2, question3, question4, question5) > 0){
				return true;
			}
			break;
		}
		return false;
	}

	public String getQnaListJson( String company, String number ) throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_qna_infos WHERE company = ? AND number = ?",company, number);
		JSONObject result = new JSONObject();
		int i=0;
		while(rs.next()){
			JSONObject jo = new JSONObject();
			jo.put("text", rs.getObject("text"));
			if(rs.getString("isCheck").equals("1")){
				jo.put("type", "check");
				jo.put("0", rs.getString("question1"));
				jo.put("1", rs.getString("question2"));
				jo.put("2", rs.getString("question3"));
				jo.put("3", rs.getString("question4"));
				jo.put("4", rs.getString("question5"));
			}
			else if(rs.getString("isRadio").equals("1")){
				jo.put("type", "radio");
				jo.put("0", rs.getString("question1"));
				jo.put("1", rs.getString("question2"));
				jo.put("2", rs.getString("question3"));
				jo.put("3", rs.getString("question4"));
				jo.put("4", rs.getString("question5"));
			}
			else if(rs.getString("isPoint").equals("1")){
				jo.put("type", "point");
				jo.put("0", rs.getString("question1"));
				jo.put("1", rs.getString("question2"));
				jo.put("2", rs.getString("question3"));
				jo.put("3", rs.getString("question4"));
				jo.put("4", rs.getString("question5"));
			}
			else if(rs.getString("isText").equals("1")){
				jo.put("type", "input");
			}
			jo.put("id", rs.getString("id"));
			result.put(i++, jo);
		}
		return result.toJSONString();
	}


}
