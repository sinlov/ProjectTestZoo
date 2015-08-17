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
	public static boolean writeProperties(String savePath, Map<String, String> datas){
		if (null == datas || datas.isEmpty()) {
			return false;
		}
		File saveFile = new File(savePath);
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Properties properties = new Properties();
		try {
			InputStream fis = new FileInputStream(savePath);
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			OutputStream fos = new FileOutputStream(savePath);
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

	private PropertiesWriter() {
	}
}
