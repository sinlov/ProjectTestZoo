/*
 * Copyright (c) 2012, Incito Corporation, All Rights Reserved
 */
package cn.com.incito.launcher.app.anim;

/**
 * @description 
 * @author   tianran
 * @createDate Apr 1, 2015
 * @version  1.0
 */
public class AnimationCommon {
	 
    /**
     * anim中的进入布局ID
     */
    public static int anim_in = 0;
    /**
     * anim中的出布局ID
     */
    public static int anim_out = 0;
 
    private AnimationCommon() {
    }
    
    /**
     * 通过动画xml文件的id设置需要使用的动画布局文件
     * @description 
     * @author   tianran
     * @createDate Apr 3, 2015
     * @param animIn
     * @param animOut
     */
    public static void setAnim(int animIn, int animOut) {
    	anim_in = animIn;
    	anim_out = animOut;
    }
 
    /**
     * 清空动画设置
     * @description 
     * @author   tianran
     * @createDate Apr 3, 2015
     */
    public static void clear() {
    	anim_in = 0;
    	anim_out = 0;
    }
}
