package core;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//make singleton
public class MakeJsonData {
	JSONObject result;
	JSONArray jarray[] = new JSONArray[10];
	MakeJsonDateTree structs;
	int parentCheck = 10000000;
	boolean checkfirst = false;
	
	private MakeJsonData(String company){
		structs = new MakeJsonDateTree("start", "-1", "-1", "-1", "0", company, "00:00:00","24:00:00", "1", "start");
	}
	
	private static class SingletonHolder { 
		static String company;
		SingletonHolder(String company){
			this.company = company;
		}
		private final static MakeJsonData instance = new MakeJsonData(company);
	}

	public static MakeJsonData getInstance(String company) {
		return SingletonHolder.instance;
	}
	
	public void initParsingData(String company){
		structs = new MakeJsonDateTree("start", "-1", "-1", "-1", "0", company, "00:00:00", "24:00:00", "1", "start");
	}
	
	public boolean parsingData( String id, String text, String depth, String parent, String index, String company, String starttime, String endtime, String count, String type ){
		int dp = Integer.parseInt(depth), max = 0;
		String pr = parent;
		MakeJsonDateTree data = structs;
		while(max < dp){
			data = data.goChild(""+pr.charAt(max));
			if(data == null){
				return false;
			}
			max++;
		}
		data.makeChild(text, depth, parent, id, index, company, starttime, endtime, count, type);
		return true;
	}	
	
	public JSONObject toJson(){
		makeJson(structs);
		return structs.obj;
	}
	
	public void makeJson(MakeJsonDateTree datas){
		while(!datas.isLastChild()){
			makeJson(datas.returnChild());			
		}
		datas.makeChildToJsonArray();
	}
}
