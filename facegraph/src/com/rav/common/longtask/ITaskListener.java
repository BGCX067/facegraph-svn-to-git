package com.rav.common.longtask;

public interface ITaskListener {
	/**
	 * Send request for listen to task.
	 * 
	 * @param task
	 * @return id of task
	 */
	int listen( LongTask task );

	/**
	 * Task invokes this method to notify Listener about changes in task.
	 * 
	 * @param task
	 *            Listener updates information from task.
	 */
	void update( LongTaskInfo task );
}
