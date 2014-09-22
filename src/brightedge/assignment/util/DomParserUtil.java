package brightedge.assignment.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import brightedge.assignment.entity.Item;

/**
 * The Utility class to parse DOM Document
 * @author Qiuyu Ma
 * @version Sep 16, 2014
 */
public class DomParserUtil {
	private Document doc;
	
	/**
     * Constructor of DomParserUtil
     */
	public DomParserUtil() {
		doc = null;
	}
	
	/**
	 * The setter for doc
	 * @param doc The Document that will be used
	 */
	public void setDocument(Document doc) {
		this.doc = doc;
	}
	
	/**
     * Get the DOM Document of a web page whose URL is urlStr
     * @param urlStr The string format of URL
     * @return The DOM Document
     * @throws IOException if connection to www.shopping.com failed.
     */
	public  Document getDocument(String urlStr) throws IOException {
		Document doc = null;
		UrlUtil urlUtil = new UrlUtil();
		// get the HTML content
		String html = urlUtil.getHtmlByUrl(urlStr);
		
		if(html.length() == 0) {
			return null;
		}else {
			// parse the HTML content to DOM Document
			doc = Jsoup.parse(html);
			return doc;		
		}
	}	
	
	/**
     * Get the results info of the web page
     * @return The String array which contains the results info
     */
	private String[] getNumbers() {
		//Total number of results retrieved
		Elements results = doc.getElementsByClass("numTotalResults");
		if(results == null || results.size() == 0) {
			return null;
		}
		
		String numberStr = results.text().toString();
		String[] numbers = numberStr.split(" ");
		
		return numbers;
	}

	/**
     * Get the total number of results
     * @return The String format of total number of results
     */
	public String getTotalNumResults() {

		String[] numbers = getNumbers();
		if(numbers == null) { // if there is no result info
			return "-1";
		}else if(numbers.length != 6) { // if the format of result info is wrong
			return "-2";
		}
		
		try {
			// if the total number can be parsed to Integer after cleaned
			String tempNum = cleanNumberStr(numbers[5]);
			Integer.parseInt(tempNum);
			return numbers[5].trim();
		}catch(NumberFormatException e) { 
	         return "-2"; 
	    }
		
	}
	
	/**
     * Get the number of results on a given page
     * @return The number of results on a given page
     */
	public int getNumResultsByPage() {
		String[] numbers = getNumbers();
		if(numbers == null) { // if there is no result info
			return -1;
		}else if(numbers.length != 6) { // if the format of result info is wrong
			return -2;
		}
		
		int maxNumber = 0;
		int minNumber = 0;
		try {
			// if the numbers can be parsed to Integer after cleaned
			maxNumber = Integer.parseInt(cleanNumberStr(numbers[3]));
			minNumber = Integer.parseInt(cleanNumberStr(numbers[1]));
		}catch(NumberFormatException e) { 
	         return -2; 
	    }
		
		return maxNumber - minNumber + 1;
	}
	
	/**
     * Get all the product info by parsing the content of the web page
     * @param number The number of products on this page
     * @return The Item (product) array
     */
	public Item[] getAllItems(int number) {
		Item[] itemList = new Item[number];
		for(int i = 1; i <= number; i ++) {
			// get the element of each product
			Element itemElement = doc.getElementById("quickLookItem-" + i);
			if(itemElement == null) {
				continue;
			}
			
			String title = null;
			String price = null;
			String shippingPrice = null;
			String vendor = null;
			String numStores = null;
			
			// get the elements of title, price, shipping info, vendor and number of stores info
			Element titleElement = itemElement.getElementById("nameQA" + i);
			Elements priceElement = itemElement.getElementsByClass("productPrice");
			Elements freeShipElement = itemElement.getElementsByClass("freeShip");	
			Elements shipElement = itemElement.getElementsByClass("calc");	
			Elements vendorElement = itemElement.getElementsByClass("newMerchantName");
			Element numStoresElement = itemElement.getElementById("numStoresQA" + i);
			
			// parse the elements to String
			if(titleElement != null) {
				title = titleElement.attr("title").trim();
			}
			
			if(priceElement != null && priceElement.size() != 0) {
				price = priceElement.get(0).text().trim();
			}
			
			if(freeShipElement != null && freeShipElement.size() != 0) {
				shippingPrice = freeShipElement.get(0).text().trim();
			}
			
			if(shipElement != null && shipElement.size() != 0) {
				shippingPrice = shipElement.get(0).text();
				shippingPrice = shippingPrice.replace("+", "").trim();
				
			}
			
			if(vendorElement != null && vendorElement.size() != 0) {
				vendor = vendorElement.get(0).text().trim();
			}
			
			if(numStoresElement != null) {
				numStores = numStoresElement.text().trim();
			}
			
			// initialize an Item instance using the info retrieved
			Item item = new Item();
			item.setTitle(title);
			item.setPrice(price);
			item.setShippingPrice(shippingPrice);
			item.setVendor(vendor);
			item.setNumStores(numStores);
			// add the Item to the array
			itemList[i - 1] = item;
		}
		
		return itemList;
	}
	
	/**
     * Clean the String format of Integer
     * Remove comma (1,000) and plus sign (1500+)
     * @param numStr The string format of Integer
     * @return The cleaned format of Integer (only numbers)
     */
	private String cleanNumberStr(String numStr) {
		return numStr.trim().replaceAll(",", "").replace("+", "");
	}
}
