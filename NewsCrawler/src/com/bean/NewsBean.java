 /**   
 *@Description:      
 */   
package com.bean;    
    
public class NewsBean {  
	private static final int SPORT = 0; // 体育类
	private static final int MILITARY = 1; // 军事类
	private static final int FINANCE = 2; // 财经类
	private static final int INTERNET = 3; // 互联网类
	private static final int REALSTATE = 4; // 房产类
	private static final int GAME = 5; // 游戏类
	
    private int id; // 唯一的标识符
    private String title; // 新闻的标题
    private String content; // 新闻正文  
    private String url; // url  
    private int type; // 新闻类型  
      
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {  
        return title;  
    }  
    public void setTitle(String title) {  
        this.title = title;  
    }  
    public String getContent() {  
        return content;  
    }  
    public void setContent(String content) {  
        this.content = content;  
    }  
    public String getUrl() {  
        return url;  
    }  
    public void setUrl(String url) {  
        this.url = url;  
    }  
    public int getType() {  
        return type;  
    }  
    public void setType(int type) {  
        this.type = type;  
    }  
}  

