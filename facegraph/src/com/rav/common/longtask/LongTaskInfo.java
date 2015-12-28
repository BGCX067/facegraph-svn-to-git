package com.rav.common.longtask;

import com.rav.common.longtask.LongTask.TaskState;

public class LongTaskInfo {
	public final int currentProgress;
	public final TaskState state;
	public final int taskId;
	
	public LongTaskInfo(LongTask task)
	{
		this.currentProgress = task.getProgress();
		this.state = task.get_State();
		this.taskId = task.get_Id();
	}
}
