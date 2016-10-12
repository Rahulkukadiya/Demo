package com.sellnews.demo;

import java.util.HashMap;
import java.util.Map;

public class ResponseMap {
	static Map<String, String> hashMap = new HashMap<String, String>();

	public static Map<String, String> generatMap(String message) {
		hashMap.clear();
		hashMap.put("message", message);
		return hashMap;

	}
	public static Map<String,String> generateCodeMessage(String message)
	{
		hashMap.clear();
		hashMap.put("your token is",message);
		return hashMap;
	}
}
