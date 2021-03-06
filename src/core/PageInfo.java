package core;

import java.util.Vector;

public class PageInfo {
	public String jspUrl;
	public Vector<String> cssName;
	public Vector<String> jsName;
	public int pageNum;
	
		
	public PageInfo(String name, int num, String... names){
		this.jspUrl = name+".jsp";
		this.pageNum = num;
		this.cssName = new Vector<String>(1,1);
		this.jsName = new Vector<String>(1,1);

		for(String n : names){
			setUrls(n);
		}
		setJsNames("common.js");
		setCssNames((name+".css"));
		setJsNames((name+".js"));
	
		
	}
	
	public String toString(){
		return this.jspUrl;
	}
	
	public Vector<String> cssNames(){
		return this.cssName;
	}
	public Vector<String> jsNames(){
		return this.jsName;
	}
	
	public void setUrls(String name){
		if(name.matches(".*css")){
			setCssNames(name);
		}else if(name.matches(".*js")){
			setJsNames(name);			
		}
	}

	public void setCssNames(String name){
		this.cssName.add(name);
	}
	public void setJsNames(String name){
		this.jsName.add(name);		
	}
	
	public Vector<String> getCssNames(){
		return this.cssName;		
	}
	public Vector<String> getJsNames(){
		return this.jsName;				
	}
}
