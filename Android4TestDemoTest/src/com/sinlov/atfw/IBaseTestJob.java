/*
 * Copyright (c) 2015, Incito Corporation, All Rights Reserved
 */
package com.sinlov.atfw;

/**
 * @description 
 * @author   sinlov
 * @createDate May 22, 2015
 * @version  1.0
 */
public interface IBaseTestJob {
	
	boolean childTaskBefore();
	
	boolean childTaskRunning();
	
	boolean childTaskAfter();
}
