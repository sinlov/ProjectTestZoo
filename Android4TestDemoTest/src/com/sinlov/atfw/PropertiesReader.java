/**
 * All rights Reserved, Designed By Android Robot thanks coder!
 * @Title:   PropertiesReader.java
 * @Package  cn.com.incito.eti.main.test.afw
 * @Copyright:  Incito Co., Ltd. Copyright 2013-2015,  All rights reserved
 * @Description: 
 * @author:  sinlov
 * @data:    Aug 17, 2015 11:50:49 AM
 * @version:  V1.0
 */
package com.sinlov.atfw;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

/**   
 * @ClassName:  PropertiesReader   
 * @Description: properties reader   
 * @author: sinlov
 * @date:   Aug 17, 2015 11:50:49 AM   
 */
public class PropertiesReader {
	
	/**
	 * 验证字符串是否为正确路径名正则表达式
	 * <p>通过 path.matches(matches) 方法的返回值判断是否正确
	 * <p>path 为路径字符串
	 */
	private static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";
	
	/**
	 * 使用存储路径，获取对于key的配置值，路径请使用可用的绝对路径
	 * <br><li> 如果不存在则默认返回空串。
	 * @Title:  getDataByKey
	 * @Description: get data by key
	 * @param:  @param savePath
	 * @param:  @param key
	 * @param:  @return 
	 * @return: String or ""
	 * @author: sinlov
	 * @date:   Aug 18, 2015 11:23:31 AM
	 */
	public static String getDataByKey(String savePath, String key){
		List<String> keys = new ArrayList<String>();
		keys.add(key);
		Map<String, String> datas = readPropertiesByABSPath(savePath, keys);
		for (Entry<String, String> data : datas.entrySet()) {
			if (data.getKey().equals(key)) {
				return data.getValue();
			}
		}
		return "";
	}
	
	/**
	 * 使用对应数据比较，路径请使用绝对路径
	 * @Title:  compareData
	 * @Description: compare data by key
	 * @param:  @param savePath
	 * @param:  @param key
	 * @param:  @param data
	 * @param:  @return 
	 * @return: boolean
	 * @author: sinlov
	 * @date:   Aug 18, 2015 11:28:28 AM
	 */
	public static boolean compareData(String savePath, String key, String data){
		return getDataByKey(savePath, key).equals(data);
	}
	
	/**
	 * 
	 * @Title:  readPropertiesByABSPath
	 * @Description: read properties file by abs paths
	 * @param:  @param savePath
	 * @param:  @param keys
	 * @param:  @return 
	 * @return: Map<String,String>
	 * @author: sinlov
	 * @date:   Aug 18, 2015 11:20:38 AM
	 */
	public static Map<String, String> readPropertiesByABSPath (String savePath, List<String> keys){
		if (savePath.matches(matches)) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(savePath));
			Properties p = new Properties();
			p.load(fis);
			for (String string : keys) {
				result.put(string, p.getProperty(string));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private PropertiesReader() {
	}
}
