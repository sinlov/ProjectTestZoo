package stringtest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Locale;

/**
 * MD5 encryption
 * <li> MD5Util.createPassword("want to encrypt", int type)
 * <li> MD5Util.authenticatePassword("password", "youInputSting", int type))
 * @author erZheng
 * @date Dec 11, 2014 2:51:21 PM
 */
public class MD5Coder {
//	
	public static final String TAG = MD5Coder.class.getName();
	public static final int TYPE_LOWER = 0;
	public static final int TYPE_UPPER = 1;
	
	private static MD5Coder mMDMd5Util;
	
	private MD5Coder() {
	}
	
	public static MD5Coder getInstance(){
		if (mMDMd5Util == null) {
			return new MD5Coder();
		}else
			return mMDMd5Util;
	}

	/**	十六进制下数字到字符的映射数组	*/
	private final static String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5", 
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }; 

	/**
	 * inputString encryption 
	 * @description 
	 * @author   sinlov
	 * @createDate Jul 7, 2015
	 * @param inputString
	 * @param type TYPE_LOWER or TYPE_UPPER
	 * @return
	 */
	public static String createPassword(String inputString, int type){ 
		return encodeByMD5(inputString, type); 
	} 
	/**
	 * authenticate password
	 * @description 
	 * @author   sinlov
	 * @createDate Jul 7, 2015
	 * @param cryptographicpassword
	 * @param authenticatePassword
	 * @param type TYPE_LOWER or TYPE_UPPER
	 * @return
	 */
	public static boolean authenticatePassword(String cryptographicpassword, String authenticatePassword, int type){ 
		if (cryptographicpassword.equals(encodeByMD5(authenticatePassword, type))){ 
			return true; 
		} else { 
			return false; 
		} 
	}
	
	public static String md5CheckFileByFilePath(String filepath){
		InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try{
            fis = new FileInputStream(filepath);
            md5 = MessageDigest.getInstance("MD5");
            while((numRead=fis.read(buffer)) > 0) {
                md5.update(buffer,0,numRead);
            }
            fis.close();
            return toHexString(md5.digest());  
        } catch (Exception e) {
            return null;
        }
	}
	
	public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

	/** 
	 * code sting to MD5 code
	 * @param originString 
	 * @return 
	 */ 
	private static String encodeByMD5(String originString, int type) { 
		if (originString != null){ 
			try { 
				MessageDigest md = MessageDigest.getInstance("MD5"); 
				byte[] results = md.digest(originString	.getBytes()); 
				String resultString = byteArrayToHexString(results);
				if (type == TYPE_LOWER) {
					return resultString.toLowerCase(Locale.getDefault());
				}else if (type == TYPE_UPPER) {
					return resultString.toUpperCase(Locale.getDefault());
				}
			} catch (Exception ex) { 
			} 
		} 
		return null; 
	}

	/** 
	 * change byte array to HexString
	 * 转换字节数组为16进制字串 
	 * @param b  字节数组 
	 * @return 十六进制字串 
	 */ 
	private static String byteArrayToHexString(byte[] b) { 
		StringBuffer resultSb = new StringBuffer(); 
		for (int i = 0; i < b.length; i++) { 
			resultSb.append(byteToHexString(b[i])); 
		} 
		return resultSb.toString(); 
	} 

	/** 
	 * become one byte to HexString
	 * 将一个字节转化成16进制形式的字符串 
	 * @param b 
	 * @return 
	 */ 
	private static String byteToHexString(byte b) { 
		int n = b; 
		if (n < 0) 
			n = 256 + n; 
		int d1 = n / 16; 
		int d2 = n % 16; 
		return HEX_DIGITS[d1] + HEX_DIGITS[d2]; 
	} 
}
