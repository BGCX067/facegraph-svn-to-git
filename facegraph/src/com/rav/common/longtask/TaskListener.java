package com.rav.common.longtask;

import com.rav.common.longtask.LongTask.TaskState;

public class TaskListener implements ITaskListener {
	public final int min;
	public final int max;
	private int progress;
	private TaskState state;

	private final int taskId;

	public TaskListener( LongTask task ) {
		this.max = task.getMax();
		this.min = task.getMin();
		taskId = listen( task );
	}

	public synchronized int getProgress()
	{
		return progress;
	}
	public synchronized TaskState getState()
	{
		return this.state;
	}
	
	@Override
	public int listen( LongTask task ) {
		return task.hashCode();
	}

	@Override
	public synchronized void update( LongTaskInfo task ) {
		if(task.taskId != this.taskId)
			throw new IllegalArgumentException("Information about bad task. Wrong task ID.");
		this.progress = task.currentProgress;
		this.state = task.state;
		
		this.notify();
	}

}
