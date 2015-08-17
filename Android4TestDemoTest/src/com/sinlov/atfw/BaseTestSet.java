/*
 * Copyright (c) 2015, Incito Corporation, All Rights Reserved
 */
package com.sinlov.atfw;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Toast;

import com.robotium.solo.Solo;

/**
 * @description 
 * @author   sinlov
 * @createDate May 22, 2015
 * @version  1.0
 */
@SuppressLint("NewApi")
@SuppressWarnings("rawtypes")
public class BaseTestSet extends ActivityInstrumentationTestCase2{

	private static String EXCEPTION_HEAD = " exception ";
	private static String EXCEPTION_END = BaseTestSet.class.getName();
	private static String EXCEPTION_SET_ERROR = "Set error" + EXCEPTION_HEAD + EXCEPTION_END;
	private static String EXCEPTION_UI_THREAD_ERROR = "Test UI Thread errror" + EXCEPTION_HEAD + EXCEPTION_END;
	public static final int DEFAULT_SLEEP_TIME = 1000;
	public static final int DEFAULT_RESULT_TIME = 10000;
	public static final String EN_MSG_TEST_FINISH = "Test finish";
	public static final String EN_MSG_TEST_RUN_TIMES = "Run times: ";
	public static final String EN_MSG_TEST_TIME_FULL = "Time full: ";
	public static final String EN_MSG_TEST_TIME_PRE = "Time pre.: ";
	public static final String EN_MSG_TEST_USE_TIME = " |Use time: ";
	public static final String EN_MSG_TEST_TIME_UTIL = " s.";
	
	/**
	 * 在被测试的APP的主线程运行一个线程
	 * @description 
	 * @author   sinlov
	 * @createDate May 22, 2015
	 * @param activity
	 * @param runable
	 */
	protected void runSimgleThreadInMain(Activity activity, Runnable runable){
		activity.runOnUiThread(runable);
	}
	
	/**
	 * UI主线程建立一个线程，呼出一个弹出窗口
	 * @description 
	 * @author   sinlov
	 * @createDate May 22, 2015
	 * @param context
	 * @param showWord
	 * @return
	 */
	protected Runnable uiThreadMakeToast(final Context context ,final String showWord){
		return new Runnable() {

			@Override
			public void run() {  
				try {  
					Toast.makeText(context, showWord , Toast.LENGTH_LONG).show();  
				} catch (Exception e) {  
					e.printStackTrace();
					Toast.makeText(context, EXCEPTION_UI_THREAD_ERROR, Toast.LENGTH_LONG).show();
				}  
			}
		};
    }
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		BaseTestSet.myActivity = this.getActivity();
		BaseTestSet.mySolo = new Solo(getInstrumentation(), getActivity());
	}

	private static IBaseTestJob testJob;
	/**
	 * 测试项目默认循环次数
	 */
	private int testTimes = 1;

	public void setTestTimes(int testTimes) {
		this.testTimes = testTimes;
	}

	/**
	 * 测试应用是否可以自动关闭
	 */
	private static boolean isAutoClose;
	/**
	 * 设置测试应用是否可以自动关闭
	 * @description 
	 * @author   sinlov
	 * @createDate May 22, 2015
	 * @param isAutoClose
	 */
	public static void setAutoClose(boolean isAutoClose) {
		BaseTestSet.isAutoClose = isAutoClose;
	}


	@Override
	public void tearDown() throws Exception {
		try {
			if (null != BaseTestSet.mySolo && null != BaseTestSet.myActivity) {
				BaseTestSet.mySolo.finishOpenedActivities();
				BaseTestSet.myActivity.finish();
			}
			super.tearDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自动关闭测试应用
	 * @description 
	 * @author   sinlov
	 * @createDate May 22, 2015
	 * @throws Exception
	 */
	protected void autoCloes() throws Exception{
		if (isAutoClose) {
			tearDown();
		}else {
			mySolo.sleep(Integer.MAX_VALUE);
		}
	}
	
	

	/**
	 * 自动测试，并附加计时
	 * @description 
	 * @author   sinlov
	 * @createDate May 22, 2015
	 * @throws Exception
	 */
	public void autoTestAndCountTime(IBaseTestJob testJob) throws Exception{
		if (null == testJob) {
			new Throwable(EXCEPTION_SET_ERROR).printStackTrace();
		}else {
			BaseTestSet.testJob = testJob;
		}
		int testTimesCount = 0;
		float totalTime = 0;
		for (int i = 0; i < this.testTimes; i++) {
			testTimesCount++;
			setUp();
			long runTime = System.currentTimeMillis();
			if (BaseTestSet.testJob.childTaskBefore()) {
				return;
			}else {
				if (BaseTestSet.testJob.childTaskRunning()) {
					return;
				}else {
					if (BaseTestSet.testJob.childTaskAfter()) {
						return;
					}
				}
			}
			float useTime = (System.currentTimeMillis() - runTime) / 1000.0f;
			totalTime = totalTime + useTime;
			System.out.println( EN_MSG_TEST_RUN_TIMES + testTimesCount + EN_MSG_TEST_USE_TIME + useTime + EN_MSG_TEST_TIME_UTIL);
			autoCloes();
		}
		setUp();
		float preTime = totalTime/(float)testTimesCount;
		String word = EN_MSG_TEST_FINISH + 
				EN_MSG_TEST_RUN_TIMES + testTimesCount + EN_MSG_TEST_TIME_UTIL + "\n" +
				EN_MSG_TEST_TIME_FULL + totalTime + EN_MSG_TEST_TIME_UTIL + "\n" +
				EN_MSG_TEST_TIME_PRE + preTime + EN_MSG_TEST_TIME_UTIL;
		System.err.println(word);
		runSimgleThreadInMain(myActivity, uiThreadMakeToast(myActivity, word));
		mySolo.sleep(DEFAULT_RESULT_TIME);
		tearDown();
	}


	private static Solo mySolo;
	private static Activity myActivity;
	public static Solo mySolo() {
		return mySolo;
	}
	public static Activity myActivity() {
		return myActivity;
	}

	private static Class<?> launchActivityClass;
	static {
		try {
			launchActivityClass = Class.forName(BaseConstants.LANUCH_ACTIVITY);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getLocaleLanguage() {  
	    Locale l = Locale.getDefault();  
	    return String.format("%s-%s", l.getLanguage(), l.getCountry());  
	} 
	
	/**
	 * 测试前一定设置，测试项目的包 + 启动主类名
	 * super();内设置
	 */
	@SuppressWarnings("unchecked")
	public BaseTestSet() {
		super(launchActivityClass);
		testJob = null;
	}
}
