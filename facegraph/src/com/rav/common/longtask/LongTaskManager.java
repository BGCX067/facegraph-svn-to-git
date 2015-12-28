package com.rav.common.longtask;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.rav.common.collections.FixedSizeMap;

public final class LongTaskManager {
	private static final int poolCapacity = 10;
	private static Map<String, LongTask> pool = FixedSizeMap
			.decorate( new HashMap<String, LongTask>( poolCapacity ) );

	private LongTaskManager() {

	}

	public static void putTask(String id, LongTask task)
	{
		pool.put( id, task );
	}
	
	private static void evicte()
	{
		for(Entry<String, LongTask> i : pool.entrySet())
		{
			if(!i.getValue().isAlive() && i.getValue().isDisposed())
				pool.remove( i.getKey() );
		}
	}
	
	public static LongTask get(String id)
	{
		return pool.get( id );
	}
	
}
