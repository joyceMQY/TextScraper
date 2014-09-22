package brightedge.assignment.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * The Utility class for URL
 * @author Qiuyu Ma
 * @version Sep 16, 2014
 */
public class UrlUtil {
	
	/** main body of www.shopping.com */
	private final static String HOST = "http://www.shopping.com/";
	/** content before page number in URL */
	private final static String PAGE = "products~PG-";
	/** content before keyword in URL */
	private final static String KEYWORD = "?CLT=SCH&KW=";
	
	/**
     * Build URL for www.shopping.com based on keyword and page number
     * @param keyword The keyword to be found
     * @param pageNum The page number of the result set
     * @return The String format of URL built based on the keyword and the page number
     */
	public String buildUrl(String keyword, int pageNum) {
		//pre-process of keyword
		keyword = keyword.replace(" ", "%20");
		URL url = null;
		try {
			//build URL
			url = new URL(HOST + keyword + "/" + keyword +"/" 
						+ PAGE + pageNum + KEYWORD + keyword);
		} catch (MalformedURLException e) {
			return null;
		}
		
		return url.toString();
		
	}
	
	/**
     * Get the HTML content of a web page whose URL is urlStr
     * @param urlStr The string format of URL
     * @return The String format of HTML content
     * @throws IOException if connection to www.shopping.com failed.
     */
	public String getHtmlByUrl(String urlStr) throws IOException {

		// convert from String format to URL
		URL url = new URL(urlStr.trim());

		// open the web page of the url
		URLConnection connection = url.openConnection();

		// read the content line by line and make it URF-8-encoded
		// use StringBuilder to store the content
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		StringBuilder content = new StringBuilder();
		String line = "";
		while((line = reader.readLine()) != null) {
			content.append(line + "\n");
		}
		reader.close();
		
		return content.toString();
	}
}
