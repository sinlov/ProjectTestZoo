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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**   
 * @ClassName:  PropertiesReader   
 * @Description:TODO(what to do)   
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
	
	public static Map<String, String> readerPropertiesByPath (String savePath, List<String> keys){
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
	
}
