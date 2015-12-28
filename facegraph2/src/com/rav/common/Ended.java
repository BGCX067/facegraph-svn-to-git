package com.rav.common;

public final class Ended 
{
	public static boolean with(Status methodResult, Status predictions)
	{
		return methodResult.getStatus() == predictions.getStatus();
	}
}
