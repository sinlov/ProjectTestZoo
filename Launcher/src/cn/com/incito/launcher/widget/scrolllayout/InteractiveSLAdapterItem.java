/*
 * Copyright (c) 2015, Incito Corporation, All Rights Reserved
 */
package cn.com.incito.launcher.widget.scrolllayout;

import android.view.View;

/**
 * @description 
 * @author   tianran
 * @createDate Apr 2, 2015
 * @version  1.0
 */
public interface InteractiveSLAdapterItem {
	void exchange(int oldPosition, int newPositon);
	int getCount();
	View getView(int position);
}
