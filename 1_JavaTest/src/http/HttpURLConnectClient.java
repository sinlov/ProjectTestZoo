/**
 * All rights Reserved, Designed By Android Robot thanks coder!
 * @Title:   HttpURLConnectClient.java
 * @Package  http
 * @Copyright:  Incito Co., Ltd. Copyright 2013-2015,  All rights reserved
 * @Description: 
 * @author:  sinlov
 * @data:    Aug 20, 2015 11:36:28 AM
 * @version:  V1.0
 */
package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**   
 * @ClassName:  HttpURLConnectClient   
 * @Description:TODO(what to do)   
 * @author: sinlov
 * @date:   Aug 20, 2015 11:36:28 AM   
 */
public class HttpURLConnectClient {
	private static int DEFAULT_OFT = 10;
	private static int DEFAULT_RFT = 10;

	public static final int GET = 01;
	public static final int POST = 11;
	public static final int RESPONSECODE_SUCCESS = 200;
	public static final int RESPONSECODE_NOT_FOUND = 404;
	public static final int RESPONSECODE_SERVER_ERROR = 500;
	
	private static final String EXC_BAD_REQUEST = "bad request ";
	
	private static final int TIMERATIO = 1000;
	private static String BOUNDARY;
	private static String PREFIX = "--";
	private static String LINEND = "\r\n";
	private static String MULTIPART_FROM_DATA = "multipart/form-data";
	private static String CHARSET = "UTF-8";
	
	/**
	 * 设置连接超时
	 * @param dEFAULT_OFT
	 */
	public static void setDEFAULT_OFT(int dEFAULT_OFT) {
		DEFAULT_OFT = dEFAULT_OFT;
	}
	/**
	 * 设置读取超时
	 * @param dEFAULT_RFT
	 */
	public static void setDEFAULT_RFT(int dEFAULT_RFT) {
		DEFAULT_RFT = dEFAULT_RFT;
	}
	
	/**  
	 * <pre> 用法 HttpURLConnectClient.push(HttpURLConnectClient.POST, url, Map<String, String>)</pre>
	 * <br><li>如果参数出现问题则返回 "" ,正常传输后，返回码如果不是200 则会输出 返回码，和返回内容
	 * @Title:  push
	 * @Description: push Http request
	 * @param:  @param Type
	 * @param:  @param actionUrl
	 * @param:  @param params
	 * @param:  @return Str or ""
	 * @return: String
	 * @author: sinlov
	 * @date:   Aug 20, 2015 12:44:06 PM
	 */ 
	public static String push(int Type, String actionUrl, Map<String, String> params){
		HttpURLConnection conn = null;
		OutputStream outStream = null;
		InputStream connIn = null;
		String requestStr = "";
		try {
			conn = refreshDataAndConn(actionUrl);
			if (null == conn) {
				return "";
			}
			setPostHead(conn, Type, BOUNDARY, DEFAULT_OFT, DEFAULT_RFT); 
			StringBuilder sb = new StringBuilder();
			appendTextParam(sb, BOUNDARY, params);
			outStream = new DataOutputStream(
					conn.getOutputStream());
			byte[] buffer = sb.toString().getBytes();
			if (!writeAppend(outStream, buffer)) {
				return "";
			}
			if (!writeEndData(outStream, BOUNDARY)) {
				return "";
			}
			int reCode = conn.getResponseCode();
			connIn = conn.getInputStream();
			if (RESPONSECODE_SUCCESS == reCode) {
				requestStr = callbackResponse(connIn);
			}else {
				requestStr = EXC_BAD_REQUEST + reCode + " " + callbackResponse(connIn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (null != outStream) {
					outStream.close();
				}
				conn.disconnect();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		return requestStr;
	}
	
	/**
	 * 刷新并初始化连接
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param actionUrl
	 * @return
	 */
	private static HttpURLConnection refreshDataAndConn(String actionUrl){
		BOUNDARY = java.util.UUID.randomUUID().toString();
		HttpURLConnection cn = null;
		try {
			URL uri = new URL(actionUrl);
			cn = (HttpURLConnection) uri.openConnection();
			uri.openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cn;
	}
	
	/**
	 * Set PostHead
	 * <p>设置连接头
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param conn 设置的连接
	 * @param BOUNDARY 随机标识符
	 * @param connOFT 连接超时设置
	 * @param connOFR 读取超时设置
	 * @throws IOException
	 */
	private static void setPostHead(HttpURLConnection conn, int type, String BOUNDARY, int connOFT, int connOFR)
			throws IOException {
		conn.setConnectTimeout(connOFT * TIMERATIO);
		conn.setReadTimeout(connOFR * TIMERATIO);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
//		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("connection", "close");
		System.setProperty("http.keepAlive", "false");
		switch (type) {
		case POST:
			conn.setRequestMethod("POST");
			break;
		case GET:
			conn.setRequestMethod("GET");
			break;
		default:
			conn.setRequestMethod("GET");
			break;
		}
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		conn.connect();
	}
	
	/**
	 * <p>拼接文本参数内容
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param sb 被设置的文本缓存
	 * @param BOUNDARY 随机标识
	 * @param params 参数
	 * @throws IOException
	 */
	private static void appendTextParam(StringBuilder sb, String BOUNDARY,
			Map<String, String> params) throws IOException {
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
	}
	
	/**
	 * 输出文本数据
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param outStream 输出数据流
	 * @param buffer 缓存字节数组
	 * @param progress 进度
	 * @param httpProgressListener 网络进度监听
	 * @return
	 */
	private static boolean writeAppend(OutputStream outStream, byte[] buffer){
		try {
			outStream.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * write end data
	 * <p>输出请求末尾数据
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param outStream 输入流
	 * @param BOUNDARY 随机标识符
	 * @return
	 */
	private static boolean writeEndData(OutputStream outStream, String BOUNDARY){
		try {
			byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(endData);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 获取返回数据
	 * <p>返回字符串
	 * @Title:  callResponse
	 * @Description: Get response String
	 * @param:  @param connIn
	 * @param:  @param requestRight
	 * @param:  @param httpProgressListener
	 * @param:  @return 
	 * @return: String
	 * @author: sinlov
	 * @date:   Aug 20, 2015 11:44:51 AM
	 */
	private static String callbackResponse(InputStream connIn){
		try {
			int ch;
			StringBuilder sb2 = new StringBuilder();
			while ((ch = connIn.read()) != -1) {
				sb2.append((char) ch);
			}
			return sb2.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
