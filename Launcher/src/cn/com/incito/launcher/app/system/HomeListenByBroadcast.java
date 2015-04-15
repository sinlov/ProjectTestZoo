/*
 * Copyright (c) 2015, Incito Corporation, All Rights Reserved
 */
package cn.com.incito.launcher.app.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @description 本类基于系统广播,无法做到拦截Home键
 * <p>如无法接收系统广播，或者因为ROM定制问题导致广播消失，则无法使用本工具类
 * <p>Home按键监听类 使用说明： 
 * <p>1、初始化<p>HomeListen HomeListen homeListen = new HomeListen(context); 
 * <p>2、设置Home键监听<p>homeListen.setOnHomeBtnPressListener( new setOnHomeBtnPressListener(){};
 * <p>3、设置Home键监听<p>在onResume方法中启动HomeListen广播： homeListen.start();
 * <p>4、一定要停止广播，比如在onPause方法中停止HomeListen广播： homeListen.stop();
 * @description public void onHomeBtnPress(){按下Home按键回调 };
 * @description public void onHomeBtnLongPress(){长按Home按键回调 };
 * @author tianran
 * @createDate Apr 2, 2015
 * @version 1.0
 */
public class HomeListenByBroadcast{
	
	public static final int  DEFAUTL_PRIORITY = 1000;
	/**
	 * 按Home按键,监听标识
	 */
	public static final String HOMEKEY = "homekey";
	/**
	 * 长按Home按键,监听标识
	 */
	public static final String RECENTAPPS = "recentapps";
	
	public HomeListenByBroadcast(Context context) {
		mContext = context;
		mHomeBtnReceiver = new HomeBtnReceiver();
		mHomeBtnIntentFilter = new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		mHomeBtnIntentFilter.setPriority(DEFAUTL_PRIORITY);
	}
	
	/**
	 * 设置Home键监听
	 * @description 
	 * @author   tianran
	 * @createDate Apr 2, 2015
	 * @param onHomeBtnPressListener
	 */
	public void setOnHomeBtnPressListener(
			OnHomeBtnPressLitener onHomeBtnPressListener) {
		mOnHomeBtnPressListener = onHomeBtnPressListener;
	}
	
	/**
	 * 开始监听Home按键
	 * @description 
	 * @author   tianran
	 * @createDate Apr 2, 2015
	 */
	public void start() {
		mContext.registerReceiver(mHomeBtnReceiver, mHomeBtnIntentFilter);
	}
	
	/**
	 * 停止监听Home按键
	 * @description 
	 * @author   tianran
	 * @createDate Apr 2, 2015
	 */
	public void stop() {
		mContext.unregisterReceiver(mHomeBtnReceiver);
	}

	private class HomeBtnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			receive(context, intent);
		}
	}

	private void receive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra("reason");
			if (reason != null) {
				if (null != mOnHomeBtnPressListener) {
					if (reason.equals(HOMEKEY)) {
						mOnHomeBtnPressListener.onHomeBtnPress();
					} else if (reason.equals(RECENTAPPS)) {
						mOnHomeBtnPressListener.onHomeBtnLongPress();
					}
				}
			}
		}
	}
	
	/**
	 * Home 键监听接口
	 * @description 
	 * @author   tianran
	 * @createDate Apr 2, 2015
	 * @version  1.0
	 */
	public interface OnHomeBtnPressLitener {
		/**
		 * Home键按下监听动作
		 * @description 
		 * @author   tianran
		 * @createDate Apr 2, 2015
		 */
		void onHomeBtnPress();
		/**
		 * Home键长按监听
		 * @description 
		 * @author   tianran
		 * @createDate Apr 2, 2015
		 */
		void onHomeBtnLongPress();
	}

	private Context mContext = null;
	private IntentFilter mHomeBtnIntentFilter = null;
	private OnHomeBtnPressLitener mOnHomeBtnPressListener = null;
	private HomeBtnReceiver mHomeBtnReceiver = null;
}
