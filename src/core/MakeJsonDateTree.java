package core;

import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MakeJsonDateTree {
	public HashMap<String, MakeJsonDateTree> children = new HashMap<>();
	public JSONObject obj = new JSONObject();
	public JSONArray arr = null;

	public int indexCount = 0;
	public String checkIndex[] = new String[20];
	
	public MakeJsonDateTree( String text, String depth, String parent, String id, String indexs, String company, String starttime, String endtime, String count, String type ){
		obj.put("name", text);
		obj.put("depth", depth);
		obj.put("id", id);
		obj.put("indexs", indexs);		
		obj.put("parentc", parent);		
		obj.put("company", company);		
		obj.put("starttime", starttime);		
		obj.put("endtime", endtime);		
		obj.put("count", count);
		obj.put("type", type);
	}
	
	public void makeChild( String text, String depth, String parent, String id, String indexs, String company, String starttime, String endtime, String count, String type ){
		children.put(indexs, new MakeJsonDateTree(text, depth, parent, id, indexs, company, starttime, endtime, count, type));
		checkIndex[indexCount++] = indexs;
	}

	public MakeJsonDateTree goChild(String index){
		return children.get(index);
	}
	
	public MakeJsonDateTree returnChild(){
		MakeJsonDateTree ch = children.get(checkIndex[--indexCount]);
		return ch;
	}
	
	
	public boolean isLastChild(){
		if(indexCount <= 0){
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void makeChildToJsonArray(){
		if(children.size() > 0){
			arr = new JSONArray();
			System.out.println(obj.toJSONString());
			for(String key : children.keySet()){
				arr.add(children.get(key).obj);
			}
			obj.put("children", arr);						
		}
	}
}
