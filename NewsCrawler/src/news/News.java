 /**  
 *@Description:   ��������վ�������� 
 */ 
package news;  

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpException;

import cn.edu.hfut.dmic.htmlbot.contentextractor.ContentExtractor;
import crawl.CrawlBase;
import util.DoRegex;
  
public class News extends CrawlBase{
	private String url;
	private String content;
	private String title;
	private String type;
	
//	private static String contentRegex = "<p.*?>(.*?)</p>"; // ��Ϊp��ǩ�ڵ�����Ϊ��������
//	private static String contentRegex = "<p\b[^<>]*>.*?</p>"; // ��Ϊp��ǩ�ڵ�����Ϊ��������
	private static String contentRegex = "<p>(.*?)</p>"; // ��Ϊp��ǩ�ڵ�����Ϊ�������� TODO
	private static String titleRegex = "<title>(.*?)</title>";
	private static int maxLength = 3000; // ��ȡ���ŵ���󳤶�
	
	private static HashMap<String, String> params;
	/**
	 * ������ͷ��Ϣ�����������αװ
	 */
	static {
		params = new HashMap<String, String>();
		params.put("Referer", "http://www.baidu.com");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
	}
	
	/**
	 * @Description: Ĭ��p��ǩ�ڵ�����Ϊ���ģ�������ĳ��Ȳ�����õ���󳤶ȣ����ȡǰ�벿��
	 */
	private void setContent() {
		String content = null;
		try {
			content = ContentExtractor.getContentByHtml(getPageSourceCode());
			content = DoRegex.getClearString(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String content = DoRegex.getString(getPageSourceCode(), contentRegex, 1);
//		content = content.replaceAll("\n", "") // �򵥵�ȥ��
//									  .replaceAll("<script.*?/script>", "")
//									  .replaceAll("<style.*?/style>", "")
//									  .replaceAll("<.*?>", "");
		this.content = content.length() > maxLength ? content.substring(0, maxLength) : content;
	}
	
	/**
	 * @Description: Ĭ��title��ǩ�ڵ�����Ϊ����
	 */
	private void setTitle() {
		this.title = DoRegex.getString(getPageSourceCode(), titleRegex, 1);;
	}
	
	public News(String url) throws HttpException, IOException {
		this.url = url;
		readPageByGet(url, "utf-8", params);
		setContent();
		setTitle();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static void setMaxLength(int maxLength) {
		News.maxLength = maxLength;
	}

	/**
	 * @param args
	 * @throws HttpException
	 * @throws IOException
	 * @Description: test
	 */
	public static void main(String[] args) throws HttpException, IOException {
		News news = new News("http://sports.163.com/15/0923/14/B471KR3100052UUC.html#p=B471LE2A0AEV0005");
		System.out.println("-----start-----");
		System.out.println(news.getContent());
		System.out.println("-----end-----");
	}

}
