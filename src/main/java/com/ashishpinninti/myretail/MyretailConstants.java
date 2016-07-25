package com.ashishpinninti.myretail;

public class MyretailConstants {

	public static final String PRODUCT_CONTROLLER_PATH = "/products/v1";
	
	// DB Configuration Constants
	public static final int MONGO_PORT = 27017;
	public static final String LOCALHOST_IP = "127.0.0.1";
	public static final String DB_NAME = "target";
	
	// Exception constants
	public static final String EX_REASON_CALL_TO_EXTERNAL_API_FAILED = "Call to external API failed!";
	public static final String EX_REASON_PRODUCT_ALREADY_EXIST = "Product already exist";
	public static final String EX_REASON_PRODUCT_DOESN_T_EXIST = "Product doesn't exist";

	
}
