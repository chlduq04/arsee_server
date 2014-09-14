package core;

import java.util.HashMap;
import java.util.Vector;

public class PageInfoResource {
	private HashMap<String, PageInfo> PAGES;
	private int page_count = 0;

	public PageInfoResource(){
		PAGES = new HashMap<>();
		setPages();
	}

	public void setPages(){
		PAGES.put("main", new PageInfo("main",page_count++));
		PAGES.put("login", new PageInfo("login",page_count++,"d3d.js"));
		PAGES.put("logout", new PageInfo("logout",page_count++));
		PAGES.put("index", new PageInfo("index",page_count++));
		PAGES.put("qna", new PageInfo("qna",page_count++,"d3.js","d3.layout.js"));
		PAGES.put("pics", new PageInfo("pics",page_count++));
		PAGES.put("companys", new PageInfo("companys",page_count++));
		PAGES.put("error", new PageInfo("error",page_count++));
		PAGES.put("management", new PageInfo("management",page_count++,"d3.js","d3.layout.js"));
		PAGES.put("update_view", new PageInfo("update_view",page_count++));
	}
	
	public Vector<String> getPageCss(String pagename){
		return PAGES.get(pagename).getCssNames();
	}
	public Vector<String> getPageJs(String pagename){
		return PAGES.get(pagename).getJsNames();
	}

	public boolean nowPage(String url){
		if(PAGES.containsKey(url)){
			return true;
		}
		return false;
	}
}
