/**
 * All rights Reserved, Designed By Android Robot thanks coder!
 * @Title:   PropertiesWriter.java
 * @Package  cn.com.incito.eti.main.test.afw
 * @Copyright:  Incito Co., Ltd. Copyright 2013-2015,  All rights reserved
 * @Description: 
 * @author:  sinlov
 * @data:    Aug 17, 2015 5:26:52 PM
 * @version:  V1.0
 */
package com.sinlov.atfw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**   
 * @ClassName:  PropertiesWriter   
 * @Description:配置文件写录  
 * @author: sinlov
 * @date:   Aug 17, 2015 5:26:52 PM   
 */
public class PropertiesWriter {
	
	/**
	 * 验证字符串是否为正确路径名正则表达式
	 * <p>通过 path.matches(matches) 方法的返回值判断是否正确
	 * <p>path 为路径字符串
	 */
	private static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";
	
	/**
	 * 根据路径和数据写配置文件
	 * @Title:  writeProperties
	 * @Description: write properties by path and data
	 * @param:  @param savePath
	 * @param:  @param datas
	 * @param:  @return 
	 * @return: boolean
	 * @author: sinlov
	 * @date:   Aug 17, 2015 6:22:19 PM
	 */
	public static boolean writeProperties(String saveABSPath, Map<String, String> datas){
		if (null == datas || datas.isEmpty() || saveABSPath.matches(matches)) {
			return false;
		}
		File saveFile = new File(saveABSPath);
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Properties properties = new Properties();
		try {
			InputStream fis = new FileInputStream(saveABSPath);
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			OutputStream fos = new FileOutputStream(saveABSPath);
			for (Entry<String, String> data : datas.entrySet()) {
				properties.setProperty(data.getKey(), data.getValue());
			}
			properties.store(fos, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 存在问题，因为实际运行路径不一样，此种获取可能出现 '/'这样的返回
	 * @Title:  getProjectPath
	 * @Description: get project path 
	 * @param:  @return 
	 * @return: String
	 * @author: sinlov
	 * @date:   Aug 18, 2015 11:13:29 AM
	 */
	public static String getProjectPath(){
		return System.getProperty("user.dir");
	}

	private PropertiesWriter() {
	}
}
