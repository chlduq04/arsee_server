package core;

import java.sql.ResultSet;
import java.sql.SQLException;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Management extends Database{
	String string;
	MakeJsonData mjd = null;
	
	public ResultSet showAllText() throws SQLException{
		initializeDB();
		return makePstmtExecute("SELECT company FROM arsee_table_update_check");
	}
	
	public ResultSet findByNumber(String company, String number) throws SQLException{
		initializeDB();
		return makePstmtExecute("SELECT * FROM arsee_ars_infos WHERE company = ? AND number = ?", company, number);
	}
	
	public boolean isEmpty(String company, String number )throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_ars_infos WHERE company = ? AND number = ?", company, number);
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

	
	public String findJson(String number, String company) throws SQLException{
		mjd = MakeJsonData.getInstance(company);
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_ars_infos WHERE number = ? AND company = ? order by depth , parent, indexs", number, company);
		while(rs.next()){
			if(Integer.parseInt(rs.getString("indexs")) >= 0){
				mjd.parsingData(rs.getString("id"),rs.getString("text"),rs.getString("depth"),rs.getString("parent"),rs.getString("indexs"),company);
			}				
		}
		return mjd.toJson().toJSONString();
	}
	
	public int findTopNavi( String number, String company) throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT MAX(depth) as depth FROM arsee_ars_infos WHERE number = ? AND company = ?", number, company);		
		rs.last();
		if(rs.getRow() > 0){
			rs.beforeFirst();
			rs.next();
			return Integer.parseInt(rs.getString("depth"));
		}
		return 0;
	}
	
	public String resultDB( String number, String company, String depth) throws SQLException{
		initializeDB();		
		ResultSet rs = makePstmtExecute("SELECT text FROM arsee_ars_infos WHERE number = ? AND company = ? AND dpeth = ?", number, company, depth);
		rs.last();
		if(rs.getRow()>0){
			rs.beforeFirst();
			while(rs.next()){
				
			}
		}
		return "";
	}
	
	public String insertDB(String number, String company, String parent_index, String parent, String depth, String index, String text, String starttime, String endtime) throws SQLException{
		initializeDB();
		ResultSet st = makePstmtExecute("SELECT id FROM arsee_ars_infos WHERE company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ?", company, parent+parent_index, index, starttime, endtime, depth);
		st.last();
		if(st.getRow()!=0){
			return "이미 같은 위치에 데이터가 있습니다.";
		}
		makePstmtUpdate("INSERT INTO arsee_ars_infos ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )", text, number, depth, index, parent+parent_index, company, starttime, endtime);							
		return "입력 성공";
	}
	
	public String deleteDB( String id ) throws SQLException{
		initializeDB();
		makePstmtUpdate("DELETE FROM arsee_ars_infos WHERE id = ?", id);							
		return "삭제성공";
	}
	
	public String modifyDB(String id, String text){
		try{
			initializeDB();
			makePstmtUpdate("UPDATE arsee_ars_infos SET text = ? WHERE id = ? LIMIT 1 ;", text, id);		
			return "수정 성공";
		}catch(Exception e){
			return "수정 실패";
		}
	}
	
}
