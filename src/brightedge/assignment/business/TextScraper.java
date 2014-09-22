package brightedge.assignment.business;

import java.io.IOException;

import org.jsoup.nodes.Document;

import brightedge.assignment.entity.Item;
import brightedge.assignment.util.DomParserUtil;
import brightedge.assignment.util.UrlUtil;

/**
 * The TextScraper class. 
 * It is the Main entry of the program which handles the input and control the business flow.
 * @author Qiuyu Ma
 * @version Sep 16, 2014
 */
public class TextScraper {
	
	private UrlUtil urlUtil;
	private DomParserUtil domParserUtil;
	
	/**
     * Constructor of TextScraper class.
     * Initialize the util instances
     */
	public TextScraper() {
		urlUtil = new UrlUtil();
		domParserUtil = new DomParserUtil();
	}
	
	/**
     * Handle the input and control the business flow to do scraping
     * @param args The arguments to run the program
     */
	public void scrape(String[] args) {
		
		// if there is no argument - error
		if(args == null || args.length == 0) {
			System.out.println("[ERROR] No argument provided!");
			printUsage();
			return;
		}else if(args.length > 2) { // if there are more than 2 arguments - error
			System.out.println("[ERROR] More than 2 arguments provided!");
			printUsage();
			return;
		}else if(args.length == 1) { // if there is only one argument
			if(args[0].isEmpty() || args[0].equals("")) { // if the first argument is empty - error
				System.out.println("[ERROR] The first argument is empty!");
				printUsage();
				return;
			}
			doQuery1(args); // only when the first argument contains something, do query 1
			return;			
		}else if(args.length == 2) { // if there are two arguments
			if(args[0].isEmpty() || args[0].equals("")) { // if the first argument is empty - error
				System.out.println("[ERROR] The first argument is empty!");
				printUsage();
				return;
			}
			if(isInteger(args[1])) { // if the second argument is an integer
				if(Integer.parseInt(args[1]) > 0) { // if the second argument is a positive integer, do query 2
					doQuery2(args);
					return;
				}else { // else (the second argument is not a positive integer) -error
					System.out.println("[ERROR] The second argument is a non-positive integer!");
					printUsage();
					return;
				}				
			}else { // else (the second argument is not an integer) - error
				System.out.println("[ERROR] The second argument is not an integer!");
				printUsage();
				return;
			}
		}
		
	}
	
	/**
     * Business flow of Query 1, will display the total number of results searched by keyword
     * @param args The arguments to run the Query 1
     * @return The total number of results
     */
	private int doQuery1(String[] args) {
		// print result title
		printResultTitle(1, args);
		
		// total number of results
		String numResults = "";
		// the content of the web page after search
		Document doc = null;
		try {
            doc = getDOM(args[0], 1); // get the Document
        }catch (IOException e) {
            System.out.println("[ERROR] Unable to connect to \"www.shopping.com\"!");
            return -1;
        }
		
		// something failed during the process of getting the Document
		if(doc == null) {
			return -1;
		}

		// set the util and get the total number of results by parsing the Document
        domParserUtil.setDocument(doc);
        numResults = domParserUtil.getTotalNumResults();
		
        // handle and display the result
		if(numResults.equals("-1") || numResults.equals("0") ) {
			System.out.println("No result found for keyword \"" + args[0] + "\"!");
			return -1;
		}else if(numResults.equals("-2")) {
			System.out.println("[WARNING] HTML structure has been changed! Please contact developers for update!");
			return -1;
		}else {
			System.out.println("The total number of results found: " + numResults);
			if(numResults.contains("+")) {
				System.out.println("(The exceeded part of items/products won't be counted and displayed!)");
			}
			
			return Integer.parseInt(numResults.replace(",", "").replace("+", "").trim());
		}
		
		
	}
	
	/**
     * Business flow of Query 1, will display the detailed product info 
     * of the results searched by keyword of a given page number
     * @param args The arguments to run the Query 2
     * @return The Item Array
     */
	private Item[] doQuery2(String[] args) {
		// print result title
		printResultTitle(2, args);
		
		// number of results on a given page
		int numByPage = 0;
		// the given page number
		int pageNum = Integer.parseInt(args[1]);
		// the content of the web page after search
		Document doc = null;
		try {			
            doc = getDOM(args[0], pageNum); // get the Document           
        }catch (IOException e) {
            System.out.println("[ERROR] Unable to connect to \"www.shopping.com\"!");
            return null;
        }
		
		// something failed during the process of getting the Document
		if(doc == null) {
			return null;
		}
		
		// set the util and get the number of results on given page by parsing the Document
		domParserUtil.setDocument(doc);
        numByPage = domParserUtil.getNumResultsByPage();
        
        // handle and display the result
		if(numByPage == -1 || numByPage == 0) {
			if(pageNum == 1) {
				System.out.println("No result found for keyword \"" + args[0] + "\"!");
			}else {
				System.out.println("[WARNING]: The page number is larger than the max page number of this keyword!");
			}				
			return null;
		}else if(numByPage == -2) {
			System.out.println("[WARNING] HTML structure has been changed! Please contact developers for update!");
			return null;
		}else {
			System.out.println("The number of results in this page: " + numByPage);
			// get the product info and create the Item List by parsing the Document
			Item[] itemList = domParserUtil.getAllItems(numByPage);	
			// print the product info
			printResultsByPage(itemList);
			
			return itemList;
		}
	}
	
	/**
     * Get a DOM Document root from the given web page with the keyword and page number
     * @param keyword The keyword to be found
     * @param pageNum The page number of the result set
     * @return A DOM Document of the web page.
     * @throws IOException if connection to www.shopping.com failed.
     */
	private Document getDOM(String keyword, int pageNum) throws IOException {

		// pre-process of the keyword
		keyword = keyword.trim();
		if(keyword.equals("")) {
			System.out.println("[ERROR] Invalid keyword!");
			return null;
		}
				
		// build the URL
		String url = urlUtil.buildUrl(keyword, pageNum);		
		if(url == null) { // if there is no URL built - error
			System.out.println("[ERROR] Failed when building the URL!"); 
			return null;
		}
		
		// display the URL
		System.out.println("URL: " + url);
		
		// get the DOM
		Document document = domParserUtil.getDocument(url);		
		if(document == null){ // if there is no DOM Document got - error
			System.out.println("[ERROR] Failed when creating the DOM!"); 
			return null;
		}
		
		return document;
			
	}
	
	/**
     * Print the result title of Query
     * @param type The Query type - 1 or 2
     * @param args The arguments to do the Query
     */
	private void printResultTitle(int type, String[] args) {

		System.out.println("======================================================================");
		System.out.println("Query Type: Query " + type);
		System.out.println("Keyword: " + args[0]);
		if(type == 2) { // for Query 2, display page number
			System.out.println("Page Number: " + args[1]);
		}
		System.out.println();
		System.out.println("Results:");
		
	}
	
	/**
     * Print the detailed product info of the given page
     * @param itemList The items with detailed product info
     */
	private void printResultsByPage(Item[] itemList) {
		if(itemList != null && itemList.length != 0) {
			System.out.println("----------------------------------------------------------------------");
			for(int i = 0; i < itemList.length; i ++) {
				// for each Item, display the number, title, price, shipping price and vendor info
				Item item = itemList[i];
				System.out.println("Product #"+ (i + 1) + ":");
				System.out.println("Title/Product Name: " + item.getTitle());
				System.out.println("Price of the Product: " + item.getPrice());
				System.out.println("Shipping Price: " + item.getShippingPrice());
				System.out.print("Vendor: " + item.getVendor());
				if(!item.getNumStores().equals("")) { // if there is "number of stores" info
					System.out.println(" , " + item.getNumStores());
				}else {
					System.out.println();
				}
				System.out.println("----------------------------------------------------------------------");
			}
		}else {
			System.out.println("No result found in this page!");
		}
	}
	
	/**
     * Check whether a String can be parsed to Integer
     * @param string The string to be checked
     * @return boolean result of the check
     */
	private boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		}catch(NumberFormatException e) { 
			// if there is exception for number format
			// it can't be parsed to Integer
	         return false; 
	    }		
		return true;
	}
	
	/**
     * Print the usage of the Text Scraper
     */
	private void printUsage() {
		System.out.println("======================================================================");
		System.out.println("Usage: java -jar TextScraper.jar <keyword> [<page number>]");
		System.out.println("<keyword>: The keyword which will be searched on \"www.shopping.com\".");
		System.out.println("           If keyword contains any white spaces, it should be");
		System.out.println("           surrounded by double quotation marks (\"). Please follow the");
		System.out.println("           syntax of Command Line to input the arguments!");
		System.out.println("<page number>: It is an OPTIONAL argument to customize the result of");
		System.out.println("               TextScraper.jar. It should be a positive integer");
		System.out.println("               indicating the page number of the result set. Detailed");
		System.out.println("               information of the result items will be displayed.");
		System.out.println("======================================================================");
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		// do scraping after the main method is triggered
		TextScraper scraper = new TextScraper();
		scraper.scrape(args);
	}

}
