package core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.util.Timer;

public class ManagementHoliday extends Database{
	String string;
	MakeJsonData mjd = null;

	public ResultSet showAllText() throws SQLException{
		initializeDB();
		return makePstmtExecute("SELECT company FROM arsee_table_update_check");
	}

	public ResultSet findByNumber(String company, String number) throws SQLException{
		initializeDB();
		return makePstmtExecute("SELECT * FROM arsee_ars_infos_holiday WHERE company = ? AND number = ?", company, number);
	}

	public boolean isEmpty(String company, String number )throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE company = ? AND number = ?", company, number);
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
	
	public String tagsFromDatabase(String id) throws SQLException{
		initializeDB();
		JSONObject result = new JSONObject();
		ResultSet res = makePstmtExecute("SELECT * FROM arsee_ars_other_infos_holiday WHERE name_id = ?", id);
		while(res.next()){
			result.put(res.getString("id"), res.getString("text"));
		}
		return result.toJSONString();
	}
	
	public String tagsToDatabase(String id, String mod_val, String new_val) throws SQLException, ParseException{
		initializeDB();
		JSONParser parser=new JSONParser();
		Object obj = parser.parse(new_val);
		JSONArray array = (JSONArray)obj;
        for(int i=0 ; i < array.size() ; i++){
        	makePstmtUpdate("INSERT INTO arsee_ars_other_infos_holiday (`name_id`, `text`) VALUES (?, ?)", id, array.get(i).toString());
        }
        
		parser=new JSONParser();
		obj = parser.parse(mod_val);
        array = (JSONArray)obj;
        for(int i=0;i<array.size();i++){
        	JSONObject json = (JSONObject)array.get(i);
        	makePstmtUpdate("UPDATE arsee_ars_other_infos_holiday SET text = ? WHERE id = ?", json.get("text").toString(), json.get("id").toString());
        }
		return "수정 성공";
	}
	
	public String jsonToDatabase(String data) throws ParseException, SQLException{
		initializeDB();
		JSONParser parser=new JSONParser();
		Object obj = parser.parse(data);
        JSONArray array = (JSONArray)obj;
        for(int i=0;i<array.size();i++){
        	JSONObject json = (JSONObject)array.get(i);
        	makePstmtUpdate("UPDATE arsee_ars_infos_holiday SET depth = ?, parent = ? WHERE id = ?", json.get("depth").toString(), json.get("parent").toString(), json.get("id").toString());
        }
        return array.toJSONString();
	}

	public String findJson(String number, String company, String starttime) throws SQLException{
		mjd = MakeJsonData.getInstance(company);
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT * FROM arsee_ars_infos_holiday WHERE number = ? AND company = ? AND starttime <= ? AND endtime > ? order by depth , parent, indexs", number, company, starttime, starttime);
		mjd.initParsingData(company);
		while(rs.next()){
//			if(Integer.parseInt(rs.getString("indexs")) >= 0 && Integer.parseInt(rs.getString("indexs")) != 100){
				String value="none";
				if(rs.getString(ARS_DATA_TYPE_INFO) != null){
					value = (ARS_DATA_TYPE_INFO);
				}
				else if(rs.getString(ARS_DATA_TYPE_ERROR) != null){
					value = (ARS_DATA_TYPE_ERROR);
				} 
				else if(rs.getString(ARS_DATA_TYPE_SHARP) != null){
					value = (ARS_DATA_TYPE_SHARP);					
				} 
				else if(rs.getString(ARS_DATA_TYPE_STAR) != null){
					value = (ARS_DATA_TYPE_STAR);					
				}
				else if(rs.getString(ARS_DATA_TYPE_NORMAL) != null){
					value = (ARS_DATA_TYPE_NORMAL);					
				}
				mjd.parsingData(rs.getString("id"),rs.getString("text"),rs.getString("depth"),rs.getString("parent"),rs.getString("indexs"), company, rs.getString("starttime"), rs.getString("endtime"), rs.getString("count"), value);
//			}				
		}
		return mjd.toJson().toJSONString();
	}

	public int findTopNavi( String number, String company) throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT MAX(depth) as depth FROM arsee_ars_infos_holiday WHERE number = ? AND company = ?", number, company);		
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
		ResultSet rs = makePstmtExecute("SELECT text FROM arsee_ars_infos_holiday WHERE number = ? AND company = ? AND dpeth = ?", number, company, depth);
		rs.last();
		if(rs.getRow()>0){
			rs.beforeFirst();
			while(rs.next()){

			}
		}
		return "";
	}
	public String insertDB(String number, String company, String parent_index, String parent, String depth, String text, String starttime, String endtime) throws Exception{
		initializeDB();
		//조회하실 전화번호를 지역번호와 함께 누른후 우물정자를 눌러주십시오 전화번호를 모르시는 경우 우물정자를 눌러주십시오
		HashMap<String, String> parsingResult = parseNumbers(number, text);		
		int i=0;
		for( ; i<10 ; i++){
			if(parsingResult.containsKey(""+i)){
				ResultSet st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ? AND "+ARS_DATA_TYPE_NORMAL+" = '1'", number, company, parent+parent_index, ""+i, starttime, endtime, depth);
				st.last();
				if(st.getRow() > 0){
					makePstmtUpdate("UPDATE SET text = ?, count = 1 WHERE indexs = ? AND starttime = ? AND endtime = ? AND company = ? AND number = ? AND depth = ? AND parent = ? ", parsingResult.get(""+i), ""+i, starttime, endtime, company, number, depth, parent+parent_index);
				}else{
					makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`, `normal` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", parsingResult.get(""+i), number, depth, ""+i, parent+parent_index, company, starttime, endtime, "1", "1");										
				}
			}
		}
		i=100;
		if(parsingResult.containsKey(""+i)){
			ResultSet st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ? AND "+ARS_DATA_TYPE_NORMAL+" = '1'", number, company, parent+parent_index, ""+i, starttime, endtime, depth);
			st.last();
			if(st.getRow() > 0){
				makePstmtUpdate("UPDATE SET text = ?, count = 1 WHERE indexs = ? AND starttime = ? AND endtime = ? AND company = ? AND number = ? AND depth = ? AND parent = ? ", parsingResult.get(""+i), ""+i, starttime, endtime, company, number, depth, parent+parent_index);
			}else{
				makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`, `normal` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", parsingResult.get(""+i), number, depth, ""+i, parent+parent_index, company, starttime, endtime, "1", "1");										
			}
		}
	
		return "입력 성공";
	}

	public String insertDB(String number, String company, String parent_index, String parent, String depth, String index, String text, String starttime, String endtime, String type) throws SQLException{
		initializeDB();
		ResultSet st;
		if(type.equals(ARS_DATA_TYPE_INFO)){
			st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ? AND "+ARS_DATA_TYPE_INFO+" = '1'", number, company, parent+parent_index, index, starttime, endtime, depth);
		}else if(type.equals(ARS_DATA_TYPE_ERROR)){
			st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ? AND "+ARS_DATA_TYPE_ERROR+" = '1'", number, company, parent+parent_index, index, starttime, endtime, depth);
		}else if(type.equals(ARS_DATA_TYPE_SHARP)){
			st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ? AND "+ARS_DATA_TYPE_SHARP+" = '1'", number, company, parent+parent_index, index, starttime, endtime, depth);
		}else if(type.equals(ARS_DATA_TYPE_STAR)){
			st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ? AND "+ARS_DATA_TYPE_STAR+" = '1'", number, company, parent+parent_index, index, starttime, endtime, depth);
		}else if(type.equals(ARS_DATA_TYPE_NORMAL)){
			st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ? AND "+ARS_DATA_TYPE_NORMAL+" = '1'", number, company, parent+parent_index, index, starttime, endtime, depth);
		}else{
			st = makePstmtExecute("SELECT id FROM arsee_ars_infos_holiday WHERE number=? AND company=? AND parent=? AND indexs=? AND starttime =? AND endtime = ? AND depth = ?", company, parent+parent_index, index, starttime, endtime, depth);
		}
		st.last();
		if(st.getRow()!=0){
			return "이미 같은 위치에 데이터가 있습니다.";
		}
		if(type.equals(ARS_DATA_TYPE_INFO)){
			makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`, `info` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", text, number, depth, index, parent+parent_index, company, starttime, endtime, "1", "1");							
		}else if(type.equals(ARS_DATA_TYPE_ERROR)){
			makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`, `error` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", text, number, depth, index, parent+parent_index, company, starttime, endtime, "1", "1");							
		}else if(type.equals(ARS_DATA_TYPE_SHARP)){
			makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`, `sharp` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", text, number, depth, index, parent+parent_index, company, starttime, endtime, "1", "1");										
		}else if(type.equals(ARS_DATA_TYPE_STAR)){
			makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`, `star` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", text, number, depth, index, parent+parent_index, company, starttime, endtime, "1", "1");										
		}else if(type.equals(ARS_DATA_TYPE_NORMAL)){
			makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count`, `normal` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", text, number, depth, index, parent+parent_index, company, starttime, endtime, "1", "1");										
		}else{
			makePstmtUpdate("INSERT INTO arsee_ars_infos_holiday ( `text`, `number`, `depth`, `indexs`, `parent`, `company`, `starttime`, `endtime`, `count` ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )", text, number, depth, index, parent+parent_index, company, starttime, endtime, "1" );										
		}
		return "입력 성공";
	}

	public String deleteDB( String data ) throws SQLException, ParseException{
		initializeDB();
		JSONParser parser=new JSONParser();
		Object obj = parser.parse(data);
        JSONArray array = (JSONArray)obj;
        for(int i=0;i<array.size();i++){
        	JSONObject json = (JSONObject)array.get(i);
    		makePstmtUpdate("DELETE FROM arsee_ars_infos_holiday WHERE id = ?", json.get("id").toString());							
        }
		return "삭제성공";
	}

	public String modifyDB(String id, String text, String starttime, String endtime, String indexs, String type ) throws SQLException{
		initializeDB();
		int count = 0;
		if(type.equals(ARS_DATA_TYPE_INFO)){
			count = makePstmtUpdate("UPDATE arsee_ars_infos_holiday SET text = ?, starttime = ?, endtime = ?, indexs = ?, "+ARS_DATA_TYPE_INFO+" = '1', "+ARS_DATA_TYPE_ERROR+" = NULL, "+ARS_DATA_TYPE_SHARP+" = NULL, "+ARS_DATA_TYPE_STAR+" = NULL, "+ARS_DATA_TYPE_NORMAL+" = NULL  WHERE id = ? LIMIT 1", text, starttime, endtime, indexs, id);		
		}else if(type.equals(ARS_DATA_TYPE_ERROR)){
			count = makePstmtUpdate("UPDATE arsee_ars_infos_holiday SET text = ?, starttime = ?, endtime = ?, indexs = ?, "+ARS_DATA_TYPE_INFO+" = NULL, "+ARS_DATA_TYPE_ERROR+" = '1', "+ARS_DATA_TYPE_SHARP+" = NULL, "+ARS_DATA_TYPE_STAR+" = NULL, "+ARS_DATA_TYPE_NORMAL+" = NULL  WHERE id = ? LIMIT 1", text, starttime, endtime, indexs, id);		
		}else if(type.equals(ARS_DATA_TYPE_SHARP)){
			count = makePstmtUpdate("UPDATE arsee_ars_infos_holiday SET text = ?, starttime = ?, endtime = ?, indexs = ?, "+ARS_DATA_TYPE_INFO+" = NULL, "+ARS_DATA_TYPE_ERROR+" = NULL, "+ARS_DATA_TYPE_SHARP+" = '1', "+ARS_DATA_TYPE_STAR+" = NULL, "+ARS_DATA_TYPE_NORMAL+" = NULL  WHERE id = ? LIMIT 1", text, starttime, endtime, indexs, id);					
		}else if(type.equals(ARS_DATA_TYPE_STAR)){
			count = makePstmtUpdate("UPDATE arsee_ars_infos_holiday SET text = ?, starttime = ?, endtime = ?, indexs = ?, "+ARS_DATA_TYPE_INFO+" = NULL, "+ARS_DATA_TYPE_ERROR+" = NULL, "+ARS_DATA_TYPE_SHARP+" = NULL, "+ARS_DATA_TYPE_STAR+" = '1', "+ARS_DATA_TYPE_NORMAL+" = NULL  WHERE id = ? LIMIT 1", text, starttime, endtime, indexs, id);		
		}else if(type.equals(ARS_DATA_TYPE_NORMAL)){
			count = makePstmtUpdate("UPDATE arsee_ars_infos_holiday SET text = ?, starttime = ?, endtime = ?, indexs = ?, "+ARS_DATA_TYPE_INFO+" = NULL, "+ARS_DATA_TYPE_ERROR+" = NULL, "+ARS_DATA_TYPE_SHARP+" = NULL, "+ARS_DATA_TYPE_STAR+" = NULL, "+ARS_DATA_TYPE_NORMAL+" = '1'  WHERE id = ? LIMIT 1", text, starttime, endtime, indexs, id);		
		}else{
			count = makePstmtUpdate("UPDATE arsee_ars_infos_holiday SET text = ?, starttime = ?, endtime = ?, indexs = ? WHERE id = ? LIMIT 1", text, starttime, endtime, indexs, id);		
		}
		if(count > 0){
			return "수정 성공";
		}
		return "수정 실패";
	}
	
	public int returnIndex(String argu){
		for(int i=0;i<Indexing.length;i++){
			for(int j=0;j<Indexing[0].length;j++){
				if(Indexing[i][j].equals(argu)){
					return i;
				}
			}
		}
		return -1;
	}

	public String indexingNumber(HashMap<String, String> map, String text){		
		while (true){
			boolean ch = false;
			for(int i=0;i<Indexing.length;i++){
				for(int j=0;j<Indexing[0].length;j++){
					if(text.indexOf(Indexing[i][j]) != -1){
						map.put(""+i, indexingNumber(map, text.split(Indexing[i][j])[0].trim()));
						text = text.split(Indexing[i][j])[1].trim();
						ch = true;
					}
				}
			}
			if(!ch){
				break;
			}
		}		
		return text;
	}
	
	public HashMap<String, String> parseNumbers(String ars, String string) throws Exception{
		HashMap<String, String> arsDivide = new HashMap<String, String>();
		String elses = "";
		String tempstring = string;
		MorphemeAnalyzer ma = new MorphemeAnalyzer();
		Timer timer = new Timer();
		timer.start();
		List<MExpression> ret = ma.analyze(string);
		timer.stop();
		timer.printMsg("Time");

		ret = ma.postProcess(ret);
		ret = ma.leaveJustBest(ret);
		List<Sentence> stl = ma.divideToSentences(ret);

		String newst = "";
		for( int i = 0; i < stl.size(); i++ ){
			Sentence st = stl.get(i);
			newst += st.getSentence();
			newst += " ";
		}

		List<MExpression> rett = ma.analyze(newst);
		ret = ma.postProcess(rett);
		ret = ma.leaveJustBest(rett);
		stl = ma.divideToSentences(rett);

		for( int i = 0; i < stl.size(); i++ ) {
			Sentence st = stl.get(i);		
			System.out.println("=============================================:" + st.getSentence());
			if(indexingNumber(arsDivide, st.getSentence()).equals(st.getSentence())){
				elses += st.getSentence()+" ";
			}
		}
		arsDivide.put("100", elses);
		return arsDivide;
	}
	
}
