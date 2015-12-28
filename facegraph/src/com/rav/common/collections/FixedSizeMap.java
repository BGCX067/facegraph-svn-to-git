package com.rav.common.collections;

import java.util.Map;

public final class FixedSizeMap<K, V>
extends AbstractMapDecorator<K, V>
implements BoundedMap<K, V>
{
	private static int defaultSize = 16;
	private final int maxSize;
	public FixedSizeMap(Map<K,V> map, int maxSize)
	{
		super(map);
		this.maxSize = maxSize;
	}
	
	@Override
	public boolean isFull() {
		return super.size() == maxSize ? true : false;
	}

	@Override
	public int getMaxSize() {
		return maxSize;
	}
	
	public static <K, V> Map<K, V> decorate(Map<K, V> map)
	{
		return new FixedSizeMap<K, V>( map, defaultSize < map.size() ? map.size() : defaultSize);
	}
	
	@Override
	public V put(K key, V value) {
		if(super.containsKey( key ))
			return super.put( key, value );
		if(isFull())
			throw new IllegalStateException( "Map is full." );
		return super.put( key, value );
	};
	
	/**
	 * All of elements of map passed to putAll method will get to fixed map, or none of them.
	 */
	@Override
	public void putAll( Map<? extends K, ? extends V> m ) {
		if(this.spaceLeft() - countNewElements( m ) < 0)
			throw new IllegalStateException("There is not enough space to put whole passed map.");
		super.putAll( m );
	}
	
	private int countNewElements( Map<? extends K, ? extends V> m )
	{
		int newElementCounter = 0;
		for(K key : this.keySet())
		{
			if(!this.containsKey( key ))
				newElementCounter += 1;
		}
		return newElementCounter;
	}
	
	private int spaceLeft()
	{
		return this.getMaxSize() - this.size();
	}
	
	

}
