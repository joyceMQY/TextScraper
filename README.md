TextScraper
===========

Design and build a robust text scraper that will connect to a page on www.shopping.com and return results about a given keyword. There are two queries that will be performed:

Query 1: Total number of results
<br>Given a keyword, such as "digital camera", return the total number of results found.
<br>Query 2: Result Object
<br>Given a keyword (e.g. "digital cameras") and page number (e.g. "1"), return the results in a result object and then print results on screen. For each result, return the following information:
<br>Title/Product Name (e.g. "Samsung TL100 Digital Camera")
<br>Price of the product
<br>Shipping Price (e.g. "Free Shipping", "$3.50")
<br>Vendor (e.g. "Amazon", "5 stores")

For "digital cameras", there should be either 40 or 80 results that return for page 1.

How to run/execute the program:
<br>Encapsulate your assignment inside an executable jar (e.g. java -jar Assignment.jar ...)
<br>Handle the two queries above:
<br>Query 1: (requires a single argument)
<br>java -jar Assignment.jar <keyword> (e.g. java -jar Assignment.jar "baby strollers")
<br>Query 2: (requires two arguments)
<br>java -jar Assignment.jar <keyword> <page number> (e.g. java -jar Assignment.jar "baby strollers" 2)
