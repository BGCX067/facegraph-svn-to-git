package com.rav.common.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMapDecorator<K, V>
implements Map<K, V>
{
	/**
	 * Wrapped map;
	 */
	protected final Map<K,V> map;
	
	/**
	 * @param map wrapped map - NOT COPIED!
	 */
	protected AbstractMapDecorator(Map<K,V> map)
	{
		this.map = map;
	}
	
	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey( Object key ) {
		return map.containsKey( key );
	}

	@Override
	public boolean containsValue( Object value ) {
		return map.containsValue( value );
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public V get( Object key ) {
		return map.get( key );
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public V put( K key, V value ) {
		return map.put( key, value );
	}

	@Override
	public void putAll( Map<? extends K, ? extends V> m ) {
		map.putAll( m );
	}

	@Override
	public V remove( Object key ) {
		return map.remove( key );
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}
}
