/*
 * Copyright (c) 2012, Incito Corporation, All Rights Reserved
 */
package com.example.android4testdemo.test;

import com.robotium.solo.Solo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Toast;

/**
 * @description 
 * @author   tianran
 * @createDate Feb 27, 2015
 * @version  1.0
 */
@SuppressLint("NewApi")
@SuppressWarnings("rawtypes")
public class Test_App_Base extends ActivityInstrumentationTestCase2 {
    
// 对应re-sign.jar生成出来的信息框里的两个值
    private static final int testTimes = 1; //重复测试次数
    private static String mainActiviy = "com.example.android4testdemo.PersonListActivity"; //本测试APP入口
//    private static String packageName = "cn.com.incito.netrade";
    
    
    private static Class<?> launchActivityClass;
    static {
	try {
	    launchActivityClass = Class.forName(mainActiviy);
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException(e);
	}
    }
    @SuppressWarnings("unchecked")
    public Test_App_Base() {
	super(launchActivityClass);
    }

    public Solo solo;
    public Activity activity;

    // 初始化Solo实例
    @Override
    protected void setUp() throws Exception {
	super.setUp();
	this.activity = this.getActivity();// 与被测应用通讯
	this.solo = new Solo(getInstrumentation(), getActivity());
    }

    // 清除实例，回收
    @Override
    public void tearDown() throws Exception {
	try {
	    this.solo.finishOpenedActivities();
	    this.activity.finish();
	    super.tearDown();
	} catch (Throwable e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * 开启应用，逐个点击文字测试，到判断是否有文字 Item 3 new 出现，未出现则中断测试，重复设置次数后停止测试
     * @description 
     * @author   tianran
     * @createDate Feb 27, 2015
     * @throws Throwable
     */
    public void test_Open_Close() throws Throwable {
	int iTime = 0;
	float totalTimes = 0;
	for (int i = 0; i < testTimes; i++) {
	    iTime++;
	    setUp();
	    long runTime = System.currentTimeMillis();
// ************************************************
// ********************预置条件****************************
	    solo.sleep(1000);
	    solo.clickOnText("Item 1");
	    solo.sleep(1000);
	    solo.clickOnText("Item 2");
	    solo.sleep(1000);
	    solo.clickOnText("Item 3");
	    solo.sleep(1000);
	    assertTrue("没有出现文字", solo.waitForText("Item 3 new"));// 可能的错误判断
	    float useTime = (System.currentTimeMillis() - runTime) / 1000.0f;
	    totalTimes = totalTimes + useTime;
	    System.out.println("RunTimes: " + iTime + " Use Time: " + useTime + "s.");
	    tearDown();
	}
	
//********************统计时间********************
	setUp();
	float preTime = totalTimes/(float)iTime;
	String word = "测试完成 \n" + 
		      "总共次数: " + iTime + "\n" +
		      "总共耗时: " + totalTimes + "\n" +
		      "平均时间: " + preTime + "s.";
	System.out.println(word);
	runSimgleThreadInMain(activity, uiThreadMakeToast(activity, word));
	solo.sleep(7000);
	tearDown();
    }

    //所有测试方法以 test 开始，下方为各种范例，请自行摸索。
    //    public void test_变更保存成功() {
    //	// ************************************************
    //	assertTrue("没有出现", solo.waitForText("用户名"));// 进入系统的判断
    //	// ********************预置条件****************************
    //	solo.enterText(0, "aaa");
    //	solo.enterText(1, "111");
    //	solo.clickOnButton(1);// 点击按钮
    //	solo.sleep(2000);
    //	// **********************操控主体***********************************
    //	solo.clickOnImageButton(0);// 点击图片按钮
    //	solo.clickOnText("变更管理");// 客户管理
    //	solo.enterText(0, "13679002209");//
    //	solo.clickOnText("换名");// 等级
    //	solo.clickOnText("换房");// 定位容器
    //	solo.clickOnView(solo.getView("id/title_img_right"));// 点击view 包括各种ListView Gridview 需要id
    //	solo.sleep(2000); //测试的主线程等待时间
    //	solo.clickOnButton(0);// 办理
    //	solo.sleep(1000);
    //	assertTrue(solo.waitForText("换房"));
    //	solo.clickOnScreen(995, 310);// 点击坐标
    //	assertTrue(solo.waitForText("变更管理"));
    //	solo.clickOnView(solo.getView("id/title_img_right"));// 保存按钮
    //	solo.sleep(500);
    //	assertTrue(solo.waitForText("交换"));
    //	solo.clickOnView(solo.getView("id/title_img_right"));// 保存按钮
    //	solo.sleep(2000);
    //	assertTrue(solo.waitForText("变更类型"));
    //	solo.goBack(); //模拟返回键
    //	//如果需要中断，设置断点
    //    }
    
    /**
     * 在被测试的APP的主线程运行一个线程
     * @description 
     * @author   tianran
     * @createDate Feb 27, 2015
     * @param activity
     * @param runable
     */
    private void runSimgleThreadInMain(Activity activity, Runnable runable){
	activity.runOnUiThread(runable);
    }
    
    /**
     * UI主线程建立一个线程，呼出一个弹出窗口口
     * @description 
     * @author   tianran
     * @createDate Feb 27, 2015
     * @param context
     * @param showWord
     * @return
     */
    private Thread uiThreadMakeToast(final Context context ,final String showWord){
	Thread t = new Thread(){  
	    @Override  
	    public void run() {  
		try {  
		    Toast.makeText(context, showWord , Toast.LENGTH_LONG).show();  
		} catch (Exception e) {  
		    e.printStackTrace();
		    Toast.makeText(context, "测试出现问题！", Toast.LENGTH_LONG).show();
		}  
	    }  
	};
	return t;
    }
}
