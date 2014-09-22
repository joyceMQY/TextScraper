package brightedge.assignment.entity;

/**
 * The entity class for each item/product
 * @author Qiuyu Ma
 * @version Sep 16, 2014
 */
public class Item {
	
	// use String to represent title, price, shipping price, 
	// vendor and number of stores of each item/product
	String title;
	String price;
	String shippingPrice;
	String vendor;
	String numStores;
	
	/**
	 * Constructor of Item
	 * Initialize each member
	 */
	public Item() {
		title = "No Title/Product Name Info";
		price = "No Price Info";
		shippingPrice = "No Shipping Info";
		vendor = "No Vendor Info";
		numStores = "";
	}
	
	/** Getters and Setters
	 *  For Setters, if the input is not null, do set.
	 */
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		if(title != null) {
			this.title = title;
		}	
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		if(price != null) {
			this.price = price;
		}
	}
	
	public String getShippingPrice() {
		return shippingPrice;
	}
	
	public void setShippingPrice(String shippingPrice) {
		if(shippingPrice != null) {
			this.shippingPrice = shippingPrice;
		}
	}
	
	public String getVendor() {
		return vendor;
	}
	
	public void setVendor(String vendor) {
		if(vendor != null) {
			this.vendor = vendor;
		}
	}

	public String getNumStores() {
		return numStores;
	}

	public void setNumStores(String numStores) {
		if(numStores != null) {
			this.numStores = numStores;
		}
	}
}
