package core;

public class PageInfoBean {
	private String templete;	
	private String cssUrl;
	private String htmlUrl;
	private String jsUrl;
	private String imageUrl;
	private String basicUrl;
	
	public PageInfoBean(){
		basicUrl = "./layout.jsp";
		templete = "layout.jsp";
		cssUrl = "../css/";
		htmlUrl = "../html/";
		jsUrl = "../js/";
		imageUrl = "../image/";
		
	}

	public String test(){
		return "this is test";
	}
	
	public String getBaseUrl(String ...params){
		String url = templete + "?";
		for(int i=0;i<params.length;i++){
			if(i!=0){
				url+="&";
			}
			url+=params[i];
		}
		return url;
	}
	
	public String getToUrl(String ...params){
		String url = templete + "?";
		for(int i=0;i<params.length;i++){
			if(i!=0){
				url+="&";
			}
			url+=params[i];
		}
		return url;		
	}
	
	
	public void setCssUrl(String url){
		this.cssUrl = url;
	}
	public void setHtmlUrl(String url){
		this.htmlUrl = url;	
	}
	public void setJsUrl(String url){
		this.jsUrl = url;	
	}
	public void setImageUrl(String url){		
		this.imageUrl = url;
	}
	
	public String getCssUrl(){
		return cssUrl;
	}
	public String getHtmlUrl(){
		return htmlUrl;
	}
	public String getJsUrl(){
		return jsUrl;
	}
	public String getImageUrl(){
		return imageUrl;
	}
}
