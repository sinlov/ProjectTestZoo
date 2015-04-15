/*
 * Copyright (c) 2015, Incito Corporation, All Rights Reserved
 */
package cn.com.incito.launcher.app.system;

import cn.com.incito.launcher.app.anim.AnimationCommon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import java.util.List;

/**
 * @description 
 * @author   tianran
 * @createDate Apr 3, 2015
 * @version  1.0
 */
public class ApplicationConnector {
	
	private ApplicationConnector() {
	}

	/**
	 * Is Local Installed by Package Manager
	 * @description 
	 * @author   tianran
	 * @createDate Apr 3, 2015
	 * @param context
	 * @param appPackageName
	 * @return
	 */
	public static boolean isLocalInstalledAPPByPM(Context context, String appPackageName) {
		boolean isInstallAppCenter = false;
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		for (PackageInfo packageInfo : packages) {
			if (packageInfo.packageName.equals(appPackageName)) {
				isInstallAppCenter = true;
			}
		}
		return isInstallAppCenter;
	}
	
	/**
	 * open app by packegeName and intent.action.MAIN
	 * @description 
	 * @author   tianran
	 * @createDate Apr 3, 2015
	 * @param context
	 * @param activityPackageName
	 * @param className
	 */
	public static void openAppByAppInfo(Context context, String activityPackageName, String className) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName componentName = new ComponentName(
				activityPackageName, className);
		intent.setComponent(componentName);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		context.startActivity(intent);
	}
	
	/**
	 * open app by packegeName and intent.action.MAIN at In animation Out animation
	 * @description 
	 * @author   tianran
	 * @createDate Apr 3, 2015
	 * @param context
	 * @param activityPackageName
	 * @param className
	 * @param animIn
	 * @param animOut
	 * @param flag Intent.FLAG_ACTIVITY_NEW_TASK or Other
	 */
	public static void openAppByAppInfoAtAnimation(Context context, String activityPackageName, String className, int animIn, int animOut ,int flag) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName componentName = new ComponentName(
				activityPackageName, className);
		intent.setComponent(componentName);
		intent.addFlags(flag);
		AnimationCommon.setAnim(animIn, animOut);
		context.startActivity(intent);
	}
}
