/*
 * Copyright (c) 2012, Incito Corporation, All Rights Reserved
 */
package http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @description 
 * @author   tianran
 * @createDate Feb 9, 2015
 * @version  1.0
 */
public class HttpURLFilePost {

	private static int DEFAULT_OFT = 10;
	private static int DEFAULT_RFT = 10;

	public static final int RESPONSECODE_SUCCESS = 200;
	public static final int RESPONSECODE_NOT_FOUND = 404;
	public static final int RESPONSECODE_SERVER_ERROR = 500;

	private static final int LASTLENGTH = 360;
	private static final int TIMERATIO = 1000;
	private static final int PROGRESSRANGE = HttpProgressListener.PROGRESS_END;
	private static String BOUNDARY;
	private static String PREFIX = "--";
	private static String LINEND = "\r\n";
	private static String MULTIPART_FROM_DATA = "multipart/form-data";
	private static String CHARSET = "UTF-8";

	private static boolean isDone = false;
	private static float readProgress;
	private static float total;

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
	 * 
	 * 通过拼接的方式构造请求内容，实现传输参数以及文件单个传输
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param actionUrl 请求URL
	 * @param params 发送参数 Map<\String, String>\ 参数名 参数字符串
	 * @param file 发送的文件对象
	 * @param httpProgressListener 发送过程监听
	 * @param requestRight 正确的回调包含的字符串
	 * @return
	 */
	public static boolean postFile(String actionUrl, Map<String, String> params,
			File file, HttpProgressListener httpProgressListener, String requestRight){
		httpProgressListener.onHttpProgressListener(HttpProgressListener.PROGRESS_BEGIN);
		HttpURLConnection conn = null;
		OutputStream outStream = null;
		InputStream connIn = null;
		try {
			conn = refreshDataAndConn(actionUrl);
			if (null == conn) {
				return isDone;
			}
			total = getUploadFileLength(params, file.getAbsolutePath());
			setPostHead(conn, BOUNDARY, DEFAULT_OFT, DEFAULT_RFT); 
			StringBuilder sb = new StringBuilder();
			appendTextParam(sb, BOUNDARY, params);
			outStream = new DataOutputStream(
					conn.getOutputStream());
			byte[] buffer = sb.toString().getBytes();
			int headLength = buffer.length;
			isDone = writeAppend(outStream, buffer, headLength, httpProgressListener);
			if (!isDone) {
				return isDone;
			}
			readProgress = headLength;
			isDone = writeFileOutputStream(outStream, BOUNDARY, file, httpProgressListener);
			if (!isDone) {
				return isDone;
			}
			isDone = writeEndData(outStream, BOUNDARY, httpProgressListener);
			if (!isDone) {
				return isDone;
			}
			if (RESPONSECODE_SUCCESS == conn.getResponseCode()) {
				connIn = conn.getInputStream();
				isDone = checkResponse(connIn, requestRight, httpProgressListener);
				if (!isDone) {
					return isDone;
				}
			}else {
				isDone = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isDone = false;
		}finally{
			try {
				outStream.close();
				conn.disconnect();
			} catch (IOException e2) {
				e2.printStackTrace();

				isDone = false;
			}finally{
				if (isDone) {
					httpProgressListener.onHttpProgressListener(HttpProgressListener.PROGRESS_FINISH);
				}else {
					httpProgressListener.onHttpProgressListener(HttpProgressListener.PROGRESS_ERROR);
				}
			}
		}
		return isDone;
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件整体传输
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param actionUrl 请求URL
	 * @param params 发送参数 Map<\String, String>\ 参数名 参数字符串
	 * @param files 文件列表  Map<\String, String>\ 文件名 文件绝对路径
	 * @param httpProgressListener 发送过程监听
	 * @param requestRight 正确的回调包含的字符串
	 * @return
	 */
	public static boolean postFiles(String actionUrl, Map<String, String> params,
			Map<String, String> files, HttpProgressListener httpProgressListener, String requestRight) {
		httpProgressListener.onHttpProgressListener(HttpProgressListener.PROGRESS_BEGIN);
		HttpURLConnection conn = null;
		OutputStream outStream = null;
		InputStream connIn = null;
		try {
			conn = refreshDataAndConn(actionUrl);
			if (null == conn) {
				return isDone;
			}
			total = getUploadFilesLength(params, files);
			setPostHead(conn, BOUNDARY, DEFAULT_OFT, DEFAULT_RFT); 
			StringBuilder sb = new StringBuilder();
			appendTextParam(sb, BOUNDARY, params);
			outStream = new DataOutputStream(
					conn.getOutputStream());
			byte[] buffer = sb.toString().getBytes();
			int headLength = buffer.length;
			isDone = writeAppend(outStream, buffer, headLength, httpProgressListener);
			if (!isDone) {
				return isDone;
			}
			readProgress = headLength;
			isDone = writeFilesOutputStream(outStream, BOUNDARY, files, httpProgressListener);
			if (!isDone) {
				return isDone;
			}
			isDone = writeEndData(outStream, BOUNDARY, httpProgressListener);
			if (!isDone) {
				return isDone;
			}
			connIn = conn.getInputStream();
			int requesCode = conn.getResponseCode();
			switch (requesCode) {
			case RESPONSECODE_SUCCESS:
				isDone = checkResponse(connIn, requestRight, httpProgressListener);
				if (!isDone) {
					return isDone;
				}
				break;
			case RESPONSECODE_NOT_FOUND:
				return isDone = false;
			case RESPONSECODE_SERVER_ERROR:
				return isDone = false;
			default:
				return isDone = false;
			}
		} catch (Exception e) {
			isDone = false;
		}finally{
			try {
				outStream.close();
				conn.disconnect();
			} catch (IOException e2) {
				isDone = false;
			}finally{
				if (isDone) {
					httpProgressListener.onHttpProgressListener(HttpProgressListener.PROGRESS_FINISH);
				}else {
					httpProgressListener.onHttpProgressListener(HttpProgressListener.PROGRESS_ERROR);
				}
			}
		}
		return isDone;
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
		isDone = false;
		total = 0;
		readProgress = 0;
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
	 * 获取参数和传输的单个文件长度
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param params 发送参数
	 * @param filePath 文件的绝对路径
	 * @return
	 */
	private static long getUploadFileLength(Map<String, String> params, String filePath) {
		long length = 0;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			length = length + entry.getKey().length() + entry.getValue().length();
		}
		File fcl = new File(filePath);
		if (fcl.exists()) {
			length = length + fcl.length();
		}
		return length + LASTLENGTH;
	}

	/**
	 * 获取参数和传输文件列表文件的长度
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param params
	 * @param filePath 
	 * @return
	 */
	private static long getUploadFilesLength(Map<String, String> params, Map<String, String> files) {
		long length = 0;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			length = length + entry.getKey().length() + entry.getValue().length();
		}
		for (Map.Entry<String, String> entryFile : files.entrySet()) {
			File fcl = new File(entryFile.getValue());
			if (fcl.exists()) {
				length = length + fcl.length();
			}
		}
		return length + LASTLENGTH;
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
	private static void setPostHead(HttpURLConnection conn, String BOUNDARY, int connOFT, int connOFR)
			throws IOException {
		conn.setConnectTimeout(connOFT * TIMERATIO);
		conn.setReadTimeout(connOFR * TIMERATIO);
		conn.setDoInput(true);
		conn.setDoOutput(true);
//		conn.setFixedLengthStreamingMode((int) total);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
//		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("connection", "close");
		System.setProperty("http.keepAlive", "false");
		conn.setRequestMethod("POST");
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
	private static boolean writeAppend(OutputStream outStream, byte[] buffer, int headLength, HttpProgressListener httpProgressListener){
		try {
			outStream.write(buffer);
			readProgress = readProgress + headLength;
			int progress = (int) (((float) readProgress / total) * PROGRESSRANGE) + HttpProgressListener.PROGRESS_BEGIN;
			httpProgressListener.onHttpProgressListener(progress);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 输出文件数据流 
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param outStream 输出数据流
	 * @param BOUNDARY 随机标识符
	 * @param file 文件对象
	 * @param httpProgressListener 网络进度监听
	 * @return
	 */
	private static boolean writeFileOutputStream(OutputStream outStream, String BOUNDARY, 
			File file,
			HttpProgressListener httpProgressListener){
		try {
			StringBuilder sb2 = new StringBuilder();
			sb2.append(PREFIX);
			sb2.append(BOUNDARY);
			sb2.append(LINEND);
			sb2.append("Content-Disposition: form-data; name=\"file[]\"; filename=\""
					+ file.getPath() + "\"" + LINEND);
			sb2.append("Content-Type: application/octet-stream; charset="
					+ CHARSET + LINEND);
			sb2.append(LINEND);
			outStream.write(sb2.toString().getBytes());
			InputStream is = new FileInputStream(file);
			int progress = 0;
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
				readProgress = readProgress + len;
				progress = (int) (((float) readProgress / total) * PROGRESSRANGE) + HttpProgressListener.PROGRESS_BEGIN;
				httpProgressListener.onHttpProgressListener(progress);
			}
			is.close();
			outStream.write(LINEND.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 输出文件列表数据流
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param outStream 输出数据流
	 * @param BOUNDARY 随机标识符
	 * @param files 文件列表
	 * @param httpProgressListener 进度监听
	 * @return
	 */
	private static boolean writeFilesOutputStream(OutputStream outStream, String BOUNDARY, 
			Map<String, String> files,
			HttpProgressListener httpProgressListener){
		try {
			for (Map.Entry<String, String> file : files.entrySet()) {
				File catchFile = new File(file.getValue());
				StringBuilder sb2 = new StringBuilder();
				sb2.append(PREFIX);
				sb2.append(BOUNDARY);
				sb2.append(LINEND);
				sb2.append("Content-Disposition: form-data; name=\"file[]\"; filename=\""
						+ catchFile.getPath() + "\"" + LINEND);
				sb2.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb2.append(LINEND);
				outStream.write(sb2.toString().getBytes());
				
				InputStream is = new FileInputStream(catchFile);
				
				int progress = 0;
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
					readProgress = readProgress + len;
					progress = (int) (((float) readProgress / total) * PROGRESSRANGE) + HttpProgressListener.PROGRESS_BEGIN;
					httpProgressListener.onHttpProgressListener(progress);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}
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
	private static boolean writeEndData(OutputStream outStream, String BOUNDARY, HttpProgressListener httpProgressListener){
		try {
			byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(endData);
			readProgress = readProgress + endData.length;
			httpProgressListener.onHttpProgressListener(HttpProgressListener.PROGRESS_END);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Check Response by String
	 * <p>通过返回字符串检查是否传送成功
	 * @description 
	 * @author   tianran
	 * @createDate Mar 23, 2015
	 * @param connIn 返回的输入流
	 * @param requestRight 正确的返回所包含的字符串
	 * @param httpProgressListener 进度监听
	 * @return
	 */
	private static boolean checkResponse(InputStream connIn, String requestRight, HttpProgressListener httpProgressListener){
		try {
			int ch;
			StringBuilder sb2 = new StringBuilder();
			while ((ch = connIn.read()) != -1) {
				sb2.append((char) ch);
			}
			String response = sb2.toString();
			if (!response.contains(requestRight)) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}