package com.easycode.redis.client.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

public interface RedisService {
	// String

		// -----------------------------Set--------------------------
		public int setString(String key, String value);

		public int setString(String key, String value, Long timeout);

		public int setString(String key, String value, Long timeout, TimeUnit unit);

		// -----------------------------Get-------------------------
		public String getString(String key);

		// -----------------------------DEL-------------------------
		public void delString(String key);

		// ----------------------------Append-------------------------
		public void appendString(String key, String appendStr);

		// Object

		// ----------------------------Set-------------------------
		public int setObj(String key, Object value);

		public int setObj(String key, Object value, Long timeout);

		public int setObj(String key, Object value, Long timeout, TimeUnit unit);

		// -----------------------------Get-------------------------
		public <T> T getObj(String key, Class<T> clazz);

		// -----------------------------Del-------------------------
		public void delObj(String key);

		// List

		// ----------------------------Add-------------------------

		public int leftPush(String key, Object value);

		public int leftPush(String key, Object value, Long timeout);

		public int leftPush(String key, Object value, Long timeout, TimeUnit unit);

		public int rightPush(String key, Object value);

		public int rightPush(String key, Object value, Long timeout);

		public int rightPush(String key, Object value, Long timeout, TimeUnit unit);

		public int push(String key, Object value, Long timeout, TimeUnit unit,
				Integer type);

		public Long leftPush(String key, Object... values);

		public Long leftPush(String key, Long timeout, Object... values);

		public Long leftPush(String key, Long timeout, TimeUnit unit,
				Object... values);

		public Long rightPush(String key, Object... values);

		public Long rightPush(String key, Long timeout, Object... values);

		public Long rightPush(String key, Long timeout, TimeUnit unit,
				Object... values);

		public Long push(String key, Long timeout, TimeUnit unit, Integer type,
				Object... values);

		public Long sizeOfList(String key);
		// ------------------------------Get
		public <T> List<T> range(String key, Class<T> clazz);

		public <T> List<T> range(String key, Long start, Long end, Class<T> clazz);

		public <T> T leftPop(String key, Class<T> clazz);

		public <T> T rightPop(String key, Class<T> clazz);
		
		public Object rightPop(String key);

		public <T> T pop(String key, Class<T> clazz, Integer type);

		public <T> T elementAt(String key, Long index, Class<T> clazz);

		// ------------------------------Del
		public void delList(String key);

		public Long remove(String key, Object value);

		public Long leftRemove(String key, Object value, Long count);

		public Long rightRemove(String key, Object value, Long count);

		public Long remove(String key, Long count, Object value);

		public Long getLength(String key);
		// Set
		// -------------------------Add
		public Long addToSet(String key, Object[] values);

		public Long addToSet(String key, Long timeout, Object[] values);

		public Long addToSet(String key, Long timeout, TimeUnit unit,
				Object[] values);

		// -------------------------Get
		public <T> Set<T> diff(String key, String otherKey, Class<T> clazz);

		public <T> Set<T> inter(String key, String otherKey, Class<T> clazz);

		public <T> Set<T> diff(String key, Set<String> otherKeys, Class<T> clazz);

		public <T> Set<T> inter(String key, Set<String> otherKeys, Class<T> clazz);

		public boolean isMember(String key, Object obj);

		public <T> Set<T> members(String key, Class<T> clazz);

		public <T> T randomPop(String key, Class<T> clazz);

		public <T> List<T> randomMembers(String key, Long count, Class<T> clazz);

		public <T> Set<T> randomDistinctMembers(String key, Long count,
				Class<T> clazz);

		public <T> Set<T> union(String key, Set<String> otherKeys, Class<T> clazz);

		public <T> Set<T> union(String key, String otherKey, Class<T> clazz);

		// -------------------------Del
		public Long removeElement(String key, Object value);

		public Long removeElements(String key, Object... values);

		public Long removeElements(String key, List<Object> values);

		public Long removeElements(String key, Set<Object> values);

		// -------------------------Util
		public Long sizeOfSet(String key);

		// Map
		// --------------------------Add

		public void put(String key, Object hashKey, Object value);

		public void put(String key, Object hashKey, Object value, Long timeout);

		public void put(String key, Object hashKey, Object value, Long timeout,
				TimeUnit unit);

		// --------------------------Get

		public <T> T getFromMap(String key, Object hashKey);

		public boolean existInMap(String key, Object hashKey);

		public <T> Set<T> keys(String key, Class<T> clazz);

		public <K, V> Map<K, V> entries(String key, Class<K> keyClazz,
				Class<V> valueClazz);

		// --------------------------Del

		public void delFromMap(String key, Object hashKey);

		public void delFromMap(String key, Object... hashKeys);

		// --------------------------Util
		public Long sizeOfMap(String key);

		public Long increment(String key);

		public Long increment(String key, Long step);

		public Long increment(String key, Long step, Long timeout, TimeUnit unit);

		public void setTimeout(String keyt, Long timeout, TimeUnit unit);

		public boolean containKey(String key);

		public boolean lock(String key);

		public void unlock(String key);

		public void del(String key);

		public void del(Set<String> keys);

		// ZSET
		public void zAdd(String key, Object value, double score);
		
		public <V> void zAdd(String key, Set<TypedTuple<Object>> values);
		
		public double zIncrBy(String key, Object value, double delta);

		public <T> Set<T> zRevRange(String key, long start, long end, Class<T> clazz);
		
		public <T> Set<T> zRange(String key, long start, long end, Class<T> clazz);

		public Long zRemove(String key, Object... values);

		public double genScore(long value, long demical);

		Long sizeOfZSet(String key);

		
		Set<String> keys(String keyPattern);
		
		public <T> Set<T> zRangeByScore(String key, double min, double max, long offset, long count, Class<T> clazz);
		
		public <T> Set<T> zRevRangeByScore(String key, double min, double max, long offset, long count, Class<T> clazz);
		
		void expireObj(String key, Long timeout, TimeUnit unit);

		
		void putAll(String key, Map<? extends Object, ? extends Object> m);
		
		List<Object> mulitGet(String key, Collection<Object> hashKeys);
		
		
		<T> List<T> mulitGet(String key, Collection<Long> hashKeys, Class<T> clazz);
		
		Long zCount(String key, double min, double max);
		
		double zScore(String key, Object value);
		
		boolean zContains(String key, Object value);
		
		long ttl(String key);
}
