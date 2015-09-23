 /**  
  *@Description: ��ȡҳ�����ӵ�ַ��Ϣ����  
 */ 
package com.crawl;  

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class CrawlListPageBase extends CrawlBase {
	private String pageurl;
	
	/**
	* @param urlStr
	* @param charsetName
	* @throws IOException
	 */
	public CrawlListPageBase(String urlStr, String charsetName) throws IOException{
		readPageByGet(urlStr, charsetName);
		pageurl = urlStr;
	}
	
	/**
	* @param urlStr
	* @param charsetName
	* @param method
	* @param params
	* @throws IOException
	 */
	public CrawlListPageBase(String urlStr, String charsetName, String method, HashMap<String, String> params) throws IOException{
		readPage(urlStr, charsetName, method, params);	
		pageurl = urlStr;
	}
	
	/**
	 * @return List<String>
  
	 * @Description: ����ҳ������������ӵ�ַ
	 */
	public List<String> getPageUrls(){
		List<String> pageUrls = new ArrayList<String>();		
		pageUrls = DoRegex.getArrayList(getPageSourceCode(), getUrlRegexString(), pageurl, getUrlRegexStringNum());		
		return pageUrls;
	}
	
	/**
	 * @return String
  
	 * @Description: ����ҳ�����������ַ���ӵ�������ʽ
	 */
	public abstract String getUrlRegexString();
	
	/**
	 * @return int
  
	 * @Description: ������ʽ��Ҫȥ���ֶ�λ��
	 */
	public abstract int getUrlRegexStringNum();	
}
