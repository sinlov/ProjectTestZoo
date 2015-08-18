/*
 * Copyright (c) 2015, Incito Corporation, All Rights Reserved
 */
package stringtest;

/**
 * @description 
 * @author   tianran
 * @createDate Apr 20, 2015
 * @version  1.0
 */
public class ConstanstTest {
	public final static int DEFUAULT_TOAST = 0;
	public final static int MIDDLE_TOAST = 1;
	public final static int dE_TOAST = 2;
	/**
	 * @description 
	 * @author   tianran
	 * @createDate Apr 20, 2015
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("DEFUAULT_TOAST: " + DEFUAULT_TOAST);
		System.out.println("MIDDLE_TOAST: " + MIDDLE_TOAST);
		System.out.println("MIDDLE_TOAST: " + MIDDLE_TOAST);
		System.out.println(MD5Coder.createPassword("111111", MD5Coder.TYPE_LOWER));
	}

}
