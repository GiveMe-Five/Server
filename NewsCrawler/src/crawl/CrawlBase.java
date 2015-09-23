 /**   
 *@Description: ģ��http���󣬻�ȡ��ҳ��Ϣ���� 
 */   
package crawl;    
      
import java.io.BufferedReader;  
import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.UnsupportedEncodingException;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Map;  
import java.util.Map.Entry;  
      


import org.apache.commons.httpclient.Header;  
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.HttpException;  
import org.apache.commons.httpclient.HttpMethod;  
import org.apache.commons.httpclient.HttpStatus;  
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;  
import org.apache.commons.httpclient.methods.GetMethod;  
import org.apache.commons.httpclient.methods.PostMethod;  
import org.apache.commons.httpclient.methods.StringRequestEntity;  
import org.apache.log4j.Logger;  
      


import util.CharsetUtil;  
import util.DoRegex;  
      
public abstract class CrawlBase {    
	private static Logger log = Logger.getLogger(CrawlBase.class);  
	
    //������ҳ��Դ����  
    private String pageSourceCode = "";  
    //����ͷ��Ϣ  
    private Header[] responseHeaders = null;  
    //���ӳ�ʱʱ��  
    private static int connectTimeout = 10000;  
    //���Ӷ�ȡʱ��  
    private static int readTimeout = 10000;  
    //Ĭ�������ʴ���  
    private static int maxConnectTimes = 3;  
    //��ҳĬ�ϱ��뷽ʽ  
    private static String charsetName = "iso-8859-1";  
    //��HttpClientί�и�MultiThreadedHttpConnectionManager��֧�ֶ��߳�  
    private static MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();  
    private static HttpClient httpClient = new HttpClient(httpConnectionManager);  
      
    static {  
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);  
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);  
        //��������ı����ʽ  
        httpClient.getParams().setContentCharset("utf-8");  
    }  
    /** 
     * @param urlStr 
     * @param charsetName �����ʽ
     * @param method 
     * @param params 
     * @return    
     * @Description: method��ʽ����ҳ�� 
     */  
    public boolean readPage(String urlStr, String charsetName, String method, HashMap<String, String> params) {  
        if ("post".equals(method) || "POST".equals(method)) {  
            return readPageByPost(urlStr, charsetName, params);  
        } else {  
            return readPageByGet(urlStr, charsetName, params);    
        }  
    }  
      
    /** 
     * @param urlStr 
     * @param charsetName 
     * @param params 
     * @return �����Ƿ�ɹ� 
     * @Description: Get��ʽ����ҳ�� 
     */  
    public boolean readPageByGet(String urlStr, String charsetName, HashMap<String, String> params) {  
        GetMethod getMethod = createGetMethod(urlStr, params);  
        return readPage(getMethod, charsetName, urlStr);  
    }  
      
    /** 
         * @param urlStr 
         * @param charsetName 
         * @param params 
         * @return �����Ƿ�ɹ� 
         * @throws HttpException 
         * @throws IOException 
   
         * @Description: Post��ʽ����ҳ�� 
         */  
    public boolean readPageByPost(String urlStr, String charsetName, HashMap<String, String> params){  
        PostMethod postMethod = createPostMethod(urlStr, params);  
        return readPage(postMethod, charsetName, urlStr);  
    }  
      
    /** 
     * @param urlStr 
     * @param charsetName 
     * @param xmlString 
     * @return 
     * @throws UnsupportedEncodingException  
     * @throws HttpException 
     * @throws IOException 
     * @Description: �ύxml������ 
     */  
    public boolean readPageByPostXml(String urlStr, String charsetName, String xmlString) throws UnsupportedEncodingException {  
        PostMethod postMethod = createPostMethodXml(urlStr, xmlString);  
        return readPage(postMethod, charsetName, urlStr);  
    }  
      
    /** 
     * @param urlStr 
     * @param charsetName 
     * @param jsonString 
     * @return 
     * @throws UnsupportedEncodingException  
     * @throws HttpException 
     * @throws IOException 
     * @Description: �ύjson������ 
     */  
    public boolean readPageByPostJson(String urlStr, String charsetName, String jsonString) throws UnsupportedEncodingException {  
        PostMethod postMethod = createPostMethodJson(urlStr, jsonString);  
        return readPage(postMethod, charsetName, urlStr);  
    }  
      
      
    /** 
     * @param method 
     * @param defaultCharset 
     * @param urlStr 
     * @return �����Ƿ�ɹ� 
     * @throws HttpException 
     * @throws IOException
     * @Description: ��ȡҳ����Ϣ��ͷ��Ϣ 
     */  
    private boolean readPage(HttpMethod method, String defaultCharset, String urlStr){  
        int n = maxConnectTimes;  
        while (n > 0) {  
            try {  
                if (httpClient.executeMethod(method) != HttpStatus.SC_OK){
                	log.error("can not connect " + urlStr + "\t" + (maxConnectTimes - n + 1) + "\t" + httpClient.executeMethod(method));  
                	System.out.println("can not connect " + urlStr + "\t" + (maxConnectTimes - n + 1) + "\t" + httpClient.executeMethod(method));                    
                    n--;  
                } else {                      	
                    //��ȡͷ��Ϣ  
                    responseHeaders = method.getResponseHeaders();
                    //��ȡҳ��Դ����  
                    InputStream inputStream = method.getResponseBodyAsStream();  
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));  
                    StringBuffer stringBuffer = new StringBuffer();  
                    String lineString = null;  
                    while ((lineString = bufferedReader.readLine()) != null){  
                        stringBuffer.append(lineString);  
                        stringBuffer.append("\n");  
                    }  
                    pageSourceCode = stringBuffer.toString();  
                    InputStream in =new  ByteArrayInputStream(pageSourceCode.getBytes(charsetName));  
                    String charset = CharsetUtil.getStreamCharset(in, defaultCharset);  
                    //��������ж���Ϊ��IP�����ز�ѯ�������ȥ��  
                    if ("Big5".equals(charset)) {  
                        charset = "gbk";  
                    }  
                    if (!charsetName.toLowerCase().equals(charset.toLowerCase())) {  
                        pageSourceCode = new String(pageSourceCode.getBytes(charsetName), charset);  
                    }  
                    return true;  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
                System.out.println(urlStr + " -- can't connect  " + (maxConnectTimes - n + 1));  
                n--;  
            }  
        }  
        return false;  
    }  
      
    /** 
     * @param url 
     * @return 
     * @Description: ��URL�е�������Ԥ���� 
     */  
    private String encodeUrlCh(String url) {  
        try {  
            return DoRegex.encodeUrlCh(url);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return url;  
        }  
    }  
      
    /** 
     * @param urlStr 
     * @param params 
     * @return GetMethod
     * @Description: ����get���������ģ��get 
     */  
    private GetMethod createGetMethod(String urlStr, HashMap<String, String> params){  
        urlStr = encodeUrlCh(urlStr);  
        GetMethod getMethod = new GetMethod(urlStr);  
        if (params == null){  
            return getMethod;  
        }  
        Iterator<Entry<String, String>> iter = params.entrySet().iterator();  
        while (iter.hasNext()) {  
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();  
            String key = (String) entry.getKey();  
            String val = (String) entry.getValue();  
            getMethod.setRequestHeader(key, val);  
        }  
        return getMethod;  
    }  
      
    /** 
     * @param urlStr 
     * @param params 
     * @return PostMethod
     * @Description: ����post���������ģ��post 
     */  
    private PostMethod createPostMethod(String urlStr, HashMap<String, String> params){  
        urlStr = encodeUrlCh(urlStr);  
        PostMethod postMethod = new PostMethod(urlStr);  
        if (params == null){  
            return postMethod;  
        }  
        Iterator<Entry<String, String>> iter = params.entrySet().iterator();  
        while (iter.hasNext()) {  
            Map.Entry<String, String> entry =  iter.next();  
            String key = (String) entry.getKey();  
            String val = (String) entry.getValue();  
            postMethod.setParameter(key, val);  
        }  
        return postMethod;  
    }  
      
    /** 
     * @param urlStr 
     * @param jsonString 
     * @return 
     * @throws UnsupportedEncodingException 
     * @Description: ����json��ʽ������ 
     */  
    private PostMethod createPostMethodJson(String urlStr, String jsonString) throws UnsupportedEncodingException{  
        urlStr = encodeUrlCh(urlStr);  
        PostMethod postMethod = new PostMethod(urlStr);  
        StringRequestEntity entity = new StringRequestEntity(jsonString, "text/json", "utf-8");  
        postMethod.setRequestEntity(entity);  
        return postMethod;  
    }  
      
    /** 
     * @param urlStr 
     * @param xmlString 
     * @return 
     * @throws UnsupportedEncodingException 
     * @Description: ����xml��ʽ������ 
     */  
    private PostMethod createPostMethodXml(String urlStr, String xmlString) throws UnsupportedEncodingException{  
        urlStr = encodeUrlCh(urlStr);  
        PostMethod postMethod = new PostMethod(urlStr);  
        StringRequestEntity entity = new StringRequestEntity(xmlString, "text/xml", "utf-8");  
        postMethod.setRequestEntity(entity);  
        return postMethod;  
    }  
      
    /** 
         * @param urlStr 
         * @param charsetName 
         * @return �����Ƿ�ɹ� 
   
         * @Description: �������κ�ͷ��Ϣֱ�ӷ�����ҳ 
         */  
    public boolean readPageByGet(String urlStr, String charsetName){  
        return this.readPageByGet(urlStr, charsetName, null);  
    }  
      
    /** 
         * @return String 
   
         * @Description: ��ȡ��ҳԴ���� 
         */  
    public String getPageSourceCode(){  
        return pageSourceCode;  
    }  
      
    /** 
         * @return Header[] 
   
         * @Description: ��ȡ��ҳ����ͷ��Ϣ 
         */  
    public Header[] getHeader(){  
        return responseHeaders;  
    }  
      
    /** 
         * @param timeout 
   
         * @Description: �������ӳ�ʱʱ�� 
         */  
    public void setConnectTimeout(int timeout){  
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);  
        CrawlBase.connectTimeout = timeout;  
    }  
      
    /** 
         * @param timeout 
   
         * @Description: ���ö�ȡ��ʱʱ�� 
         */  
    public void setReadTimeout(int timeout){  
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);  
        CrawlBase.readTimeout = timeout;  
    }  
      
    /** 
         * @param maxConnectTimes 
   
         * @Description: ���������ʴ���������ʧ�ܵ������ʹ�� 
         */  
    public static void setMaxConnectTimes(int maxConnectTimes) {  
        CrawlBase.maxConnectTimes = maxConnectTimes;  
    }  
  
    /** 
         * @param connectTimeout 
         * @param readTimeout 
   
         * @Description: �������ӳ�ʱʱ��Ͷ�ȡ��ʱʱ�� 
         */  
    public void setTimeout(int connectTimeout, int readTimeout){  
        setConnectTimeout(connectTimeout);  
        setReadTimeout(readTimeout);  
    }  
  
    /** 
         * @param charsetName 
   
         * @Description: ����Ĭ�ϱ��뷽ʽ 
         */  
    public static void setCharsetName(String charsetName) {  
        CrawlBase.charsetName = charsetName;  
    }  
}  