/*
 * Copyright (c) 2012, Incito Corporation, All Rights Reserved
 */
package http;

/**
 * @description 
 * @author   tianran
 * @createDate Mar 23, 2015
 * @version  1.0
 */
public interface HttpProgressListener {
	public static final int PROGRESS_BEGIN = 1;
	public static final int PROGRESS_END = 99;
	public static final int PROGRESS_FINISH = 100;
	public static final int PROGRESS_ERROR = -1;
	boolean onHttpProgressListener(int progress);
}
