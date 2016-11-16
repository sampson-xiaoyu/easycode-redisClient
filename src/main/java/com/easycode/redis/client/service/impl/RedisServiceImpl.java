package com.easycode.redis.client.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.easycode.commons.StringUtils;
import com.easycode.redis.client.service.RedisService;

public class RedisServiceImpl implements RedisService {

	private RedisTemplate<String, String> stringRedisTemplate;

	private RedisTemplate<String, Object> objRedisTemplate;

	public RedisTemplate<String, String> getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	public void setStringRedisTemplate(RedisTemplate<String, String> stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public RedisTemplate<String, Object> getObjRedisTemplate() {
		return objRedisTemplate;
	}

	public void setObjRedisTemplate(RedisTemplate<String, Object> objRedisTemplate) {
		this.objRedisTemplate = objRedisTemplate;
	}

	// String

	// ------------------------------Set String--------------------
	@Override
	public int setString(String key, String value) {
		return setString(key, value, null, null);
	}

	@Override
	public int setString(String key, String value, Long timeout) {
		return setString(key, value, timeout, null);
	}

	@Override
	public int setString(String key, String value, Long timeout, TimeUnit unit) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return 0;
		}

		if (timeout == null) {
			stringRedisTemplate.opsForValue().set(key, value);
		} else {
			if (unit == null) {
				unit = TimeUnit.MILLISECONDS;
			}
			stringRedisTemplate.opsForValue().set(key, value, timeout.longValue(), unit);
		}

		return 1;
	}

	// -----------------------------Get-------------------------
	@Override
	public String getString(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	// -----------------------------DEL-------------------------
	@Override
	public void delString(String key) {
		stringRedisTemplate.delete(key);
	}

	// ----------------------------Append-------------------------
	@Override
	public void appendString(String key, String appendStr) {
		stringRedisTemplate.opsForValue().append(key, appendStr);
	}

	// Object
	// ----------------------------Set-------------------------

	@Override
	public int setObj(String key, Object value) {
		return setObj(key, value, null, null);
	}

	@Override
	public int setObj(String key, Object value, Long timeout) {
		return setObj(key, value, timeout, null);
	}

	@Override
	public int setObj(String key, Object value, Long timeout, TimeUnit unit) {
		if (StringUtils.isEmpty(key) || value == null) {
			return 0;
		}

		if (timeout == null) {
			objRedisTemplate.opsForValue().set(key, value);
		} else {
			if (unit == null) {
				unit = TimeUnit.MILLISECONDS;
			}
			objRedisTemplate.opsForValue().set(key, value, timeout.longValue(), unit);
		}

		return 1;
	}

	// -----------------------------Get-------------------------
	@Override
	public <T> T getObj(String key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return (T) objRedisTemplate.opsForValue().get(key);
	}

	// -----------------------------Del-------------------------
	@Override
	public void delObj(String key) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		objRedisTemplate.delete(key);
	}

	// List

	// ----------------------------Add-------------------------

	@Override
	public int leftPush(String key, Object value) {
		return push(key, value, null, null, 0);
	}

	@Override
	public int leftPush(String key, Object value, Long timeout) {
		return push(key, value, timeout, null, 0);
	}

	@Override
	public int leftPush(String key, Object value, Long timeout, TimeUnit unit) {
		return push(key, value, timeout, unit, 0);
	}

	@Override
	public int rightPush(String key, Object value) {
		return push(key, value, null, null, 1);
	}

	@Override
	public int rightPush(String key, Object value, Long timeout) {
		return push(key, value, timeout, null, 1);
	}

	@Override
	public int rightPush(String key, Object value, Long timeout, TimeUnit unit) {
		return push(key, value, timeout, unit, 1);
	}

	@Override
	public int push(String key, Object value, Long timeout, TimeUnit unit, Integer type) {
		if (StringUtils.isEmpty(key) || value == null) {
			return 0;
		}
		boolean isFirst = true;

		if (objRedisTemplate.hasKey(key)) {
			isFirst = false;
		}

		if (type.intValue() == 0) {
			objRedisTemplate.opsForList().leftPush(key, value);
		} else {
			objRedisTemplate.opsForList().rightPush(key, value);
		}

		if (isFirst && timeout != null) {
			if (unit == null) {
				unit = TimeUnit.MILLISECONDS;
			}
			objRedisTemplate.expire(key, timeout, unit);
		}

		return 1;
	}

	@Override
	public Long leftPush(String key, Object... values) {
		return push(key, null, null, 0, values);
	}

	@Override
	public Long leftPush(String key, Long timeout, Object... values) {
		return push(key, timeout, null, 0, values);
	}

	@Override
	public Long leftPush(String key, Long timeout, TimeUnit unit, Object... values) {
		return push(key, timeout, unit, 0, values);
	}

	@Override
	public Long rightPush(String key, Object... values) {
		return push(key, null, null, 1, values);
	}

	@Override
	public Long rightPush(String key, Long timeout, Object... values) {
		return push(key, timeout, null, 1, values);
	}

	@Override
	public Long rightPush(String key, Long timeout, TimeUnit unit, Object... values) {
		return push(key, timeout, unit, 1, values);
	}

	@Override
	public Long push(String key, Long timeout, TimeUnit unit, Integer type, Object... values) {
		if (StringUtils.isEmpty(key) || values == null || values.length < 1) {
			return 0L;
		}
		boolean isFirst = true;
		Long result = null;

		if (objRedisTemplate.hasKey(key)) {
			isFirst = false;
		}

		if (type.intValue() == 0) {
			result = objRedisTemplate.opsForList().leftPushAll(key, values);
		} else {
			result = objRedisTemplate.opsForList().rightPushAll(key, values);
		}

		if (isFirst && timeout != null) {
			if (unit == null) {
				unit = TimeUnit.MILLISECONDS;
			}
			objRedisTemplate.expire(key, timeout, unit);
		}
		return result;
	}

	// ------------------------------Get
	@Override
	public <T> List<T> range(String key, Class<T> clazz) {
		return range(key, 0L, -1L, clazz);
	}

	@Override
	public <T> List<T> range(String key, Long start, Long end, Class<T> clazz) {
		if (StringUtils.isEmpty(key) || !objRedisTemplate.hasKey(key)) {
			return null;
		}

		if (start < 0) {
			start = 0L;
		}

		List<T> list = (List<T>) (objRedisTemplate.opsForList().range(key, start, end));

		return list;
	}

	@Override
	public <T> T leftPop(String key, Class<T> clazz) {
		return pop(key, clazz, 0);
	}

	@Override
	public <T> T rightPop(String key, Class<T> clazz) {
		return pop(key, clazz, 1);
	}

	@Override
	public Object rightPop(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return objRedisTemplate.opsForList().leftPop(key);
	}

	@Override
	public <T> T pop(String key, Class<T> clazz, Integer type) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		if (type == null) {
			type = 1;
		}

		if (type.intValue() == 0) {
			return ((T) objRedisTemplate.opsForList().leftPop(key));
		} else {
			return ((T) objRedisTemplate.opsForList().rightPop(key));
		}
	}

	@Override
	public <T> T elementAt(String key, Long index, Class<T> clazz) {
		if (index < -1) {
			return null;
		}

		T result = (T) objRedisTemplate.opsForList().index(key, index);

		return result;
	}

	// ------------------------------Del
	@Override
	public void delList(String key) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		delObj(key);
	}

	@Override
	public Long remove(String key, Object value) {
		return remove(key, 0L, value);
	}

	@Override
	public Long leftRemove(String key, Object value, Long count) {
		if (count.longValue() < 0) {
			count = 0 - count;
		}
		return remove(key, count, value);
	}

	@Override
	public Long rightRemove(String key, Object value, Long count) {
		if (count > 0) {
			count = 0L - count;
		}
		return remove(key, count, value);
	}

	@Override
	public Long remove(String key, Long count, Object value) {
		if (StringUtils.isEmpty(key) || value == null) {
			return 0L;
		}
		return objRedisTemplate.opsForList().remove(key, count, value);
	}

	@Override
	public Long sizeOfList(String key) {
		if (StringUtils.isEmpty(key)) {
			return 0L;
		}
		return objRedisTemplate.opsForList().size(key);
	}

	@Override
	public Long getLength(String key) {
		if (StringUtils.isEmpty(key)) {
			return 0L;
		}
		return objRedisTemplate.opsForList().size(key);
	}

	// Set
	// -------------------------Add----------------------------------------
	@Override
	public Long addToSet(String key, Object[] values) {
		return addToSet(key, null, null, values);
	}

	@Override
	public Long addToSet(String key, Long timeout, Object[] values) {
		return addToSet(key, timeout, null, values);
	}

	@Override
	public Long addToSet(String key, Long timeout, TimeUnit unit, Object[] values) {

		if (values == null || values.length <= 0) {
			return 0L;
		}

		boolean isFirst = true;
		if (objRedisTemplate.hasKey(key)) {
			isFirst = false;
		}

		Long rtn = objRedisTemplate.opsForSet().add(key, values);

		if (isFirst && timeout != null) {
			if (unit == null) {
				unit = TimeUnit.MILLISECONDS;
			}
			objRedisTemplate.expire(key, timeout, unit);
		}

		if (rtn.longValue() <= 0) {
			return 0L;
		}

		return objRedisTemplate.opsForSet().add(key, values);
	}

	// -----------------------------Get-------------------------------------
	@Override
	public <T> Set<T> diff(String key, String otherKey, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		if (StringUtils.isEmpty(otherKey)) {
			return members(key, clazz);
		}
		Set<String> otherKeys = new HashSet<String>();
		otherKeys.add(otherKey);
		return diff(key, otherKeys, clazz);
	}

	@Override
	public <T> Set<T> inter(String key, String otherKey, Class<T> clazz) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(otherKey)) {
			return null;
		}
		Set<String> otherKeys = new HashSet<String>();
		otherKeys.add(otherKey);
		return inter(key, otherKeys, clazz);
	}

	@Override
	public <T> Set<T> diff(String key, Set<String> otherKeys, Class<T> clazz) {
		return ((Set<T>) objRedisTemplate.opsForSet().difference(key, otherKeys));
	}

	@Override
	public <T> Set<T> inter(String key, Set<String> otherKeys, Class<T> clazz) {
		return (Set<T>) objRedisTemplate.opsForSet().intersect(key, otherKeys);
	}

	@Override
	public boolean isMember(String key, Object obj) {
		return objRedisTemplate.opsForSet().isMember(key, obj);
	}

	@Override
	public <T> Set<T> members(String key, Class<T> clazz) {
		return ((Set<T>) objRedisTemplate.opsForSet().members(key));
	}

	@Override
	public <T> T randomPop(String key, Class<T> clazz) {
		return (T) objRedisTemplate.opsForSet().pop(key);
	}

	@Override
	public <T> List<T> randomMembers(String key, Long count, Class<T> clazz) {
		return (List<T>) objRedisTemplate.opsForSet().randomMembers(key, count);
	}

	@Override
	public <T> Set<T> randomDistinctMembers(String key, Long count, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return (Set<T>) objRedisTemplate.opsForSet().distinctRandomMembers(key, count);
	}

	@Override
	public <T> Set<T> union(String key, Set<String> otherKeys, Class<T> clazz) {
		return (Set<T>) objRedisTemplate.opsForSet().union(key, otherKeys);
	}

	@Override
	public <T> Set<T> union(String key, String otherKey, Class<T> clazz) {
		Set<String> otherKeys = new HashSet<String>();
		otherKeys.add(otherKey);
		return union(key, otherKeys, clazz);
	}

	// -------------------------Del------------------------------
	@Override
	public Long removeElement(String key, Object value) {
		return objRedisTemplate.opsForSet().remove(key, new Object[] { value });
	}

	@Override
	public Long removeElements(String key, Object... values) {
		return objRedisTemplate.opsForSet().remove(key, values);
	}

	@Override
	public Long removeElements(String key, List<Object> values) {
		return removeElements(key, values.toArray());
	}

	@Override
	public Long removeElements(String key, Set<Object> values) {
		return removeElement(key, values.toArray());
	}

	// -------------------------Util
	@Override
	public Long sizeOfSet(String key) {
		return objRedisTemplate.opsForSet().size(key);
	}

	// Map
	// --------------------------Add

	public void put(String key, Object hashKey, Object value) {
		put(key, hashKey, value, null, null);
	}

	public void put(String key, Object hashKey, Object value, Long timeout) {
		put(key, hashKey, value, timeout, null);
	}

	public void put(String key, Object hashKey, Object value, Long timeout, TimeUnit unit) {
		if (StringUtils.isEmpty(key) || hashKey == null) {
			return;
		}

		boolean isFirst = true;
		if (existInMap(key, hashKey)) {
			isFirst = false;
		}

		objRedisTemplate.opsForHash().put(key, hashKey, value);

		if (isFirst && timeout != null) {
			if (unit == null) {
				unit = TimeUnit.MILLISECONDS;
			}
			objRedisTemplate.expire(key, timeout, unit);
		}
	}

	// --------------------------Get

	public <T> T getFromMap(String key, Object hashKey) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return (T) objRedisTemplate.opsForHash().get(key, hashKey);
	}

	public boolean existInMap(String key, Object hashKey) {
		if (StringUtils.isEmpty(key)) {
			return false;
		}
		return objRedisTemplate.opsForHash().hasKey(key, hashKey);
	}

	public <T> Set<T> keys(String key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return (Set<T>) objRedisTemplate.opsForHash().keys(key);
	}

	public <K, V> Map<K, V> entries(String key, Class<K> keyClazz, Class<V> valueClazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return ((Map<K, V>) objRedisTemplate.opsForHash().entries(key));
	}

	// --------------------------Del
	@Override
	public void delFromMap(String key, Object hashKey) {
		objRedisTemplate.opsForHash().delete(key, new Object[] { hashKey });
	}

	@Override
	public void delFromMap(String key, Object... hashKeys) {
		objRedisTemplate.opsForHash().delete(key, hashKeys);
	}

	// --------------------------Util
	@Override
	public Long sizeOfMap(String key) {
		if (StringUtils.isEmpty(key)) {
			return 0L;
		}
		return objRedisTemplate.opsForHash().size(key);
	}

	@Override
	public Long increment(String key) {
		return stringRedisTemplate.opsForValue().increment(key, 1);
	}

	@Override
	public Long increment(String key, Long step) {
		return stringRedisTemplate.opsForValue().increment(key, step);
	}

	@Override
	public void setTimeout(String key, Long timeout, TimeUnit unit) {
		objRedisTemplate.expire(key, timeout, unit);
	}

	@Override
	public boolean containKey(String key) {
		return objRedisTemplate.hasKey(key);
	}

	@Override
	public Long increment(String key, Long step, Long timeout, TimeUnit unit) {
		if (key == null) {
			return 0L;
		}
		boolean first = true;
		if (containKey(key)) {
			first = false;
		}
		Long val = increment(key, step);
		if (first) {
			setTimeout(key, timeout, unit);
		}
		return val;
	}

	@Override
	public boolean lock(String key) {
		String lockKey = key + ":lock";
		long ttl = ttl(lockKey);
		if(ttl < 0) {
			del(lockKey);
		}
		
		if (increment(lockKey, 1L, 1L, TimeUnit.MINUTES) <= 1) {
			return true;
		}
		return false;
	}

	@Override
	public void unlock(String key) {
		String lockKey = key + ":lock";
		del(lockKey);
	}

	@Override
	public void del(String key) {
		objRedisTemplate.delete(key);
	}

	@Override
	public void del(Set<String> keys) {
		objRedisTemplate.delete(keys);
	}

	// ZSET
	@Override
	public void zAdd(String key, Object value, double score) {
		if (value == null) {
			return;
		}
		objRedisTemplate.opsForZSet().add(key, value, score);
	}

	@Override
	public <V> void zAdd(String key, Set<TypedTuple<Object>> values) {
		if (values == null || values.size() <= 0) {
			return;
		}
		objRedisTemplate.opsForZSet().add(key, values);
	}

	@Override
	public double zIncrBy(String key, Object value, double delta) {
		return objRedisTemplate.opsForZSet().incrementScore(key, value, delta);
	}

	@Override
	public <T> Set<T> zRange(String key, long start, long end, Class<T> clazz) {
		return (Set<T>) objRedisTemplate.opsForZSet().range(key, start, end);
	}

	@Override
	public <T> Set<T> zRevRange(String key, long start, long end, Class<T> clazz) {
		return (Set<T>) objRedisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	@Override
	public <T> Set<T> zRangeByScore(String key, double min, double max, long offset, long count, Class<T> clazz) {
		return (Set<T>) objRedisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
	}

	@Override
	public <T> Set<T> zRevRangeByScore(String key, double min, double max, long offset, long count, Class<T> clazz) {
		return (Set<T>) objRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
	}

	@Override
	public Long zRemove(String key, Object... values) {
		return objRedisTemplate.opsForZSet().remove(key, values);
	}

	@Override
	public double genScore(long value, long demical) {
		String ret = String.valueOf(value) + "." + addZeroForNum(String.valueOf(demical), 5);
		return Double.parseDouble(ret);
	}

	@Override
	public Long sizeOfZSet(String key) {
		return objRedisTemplate.opsForZSet().size(key);
	}

	private String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左(前)补0
			// sb.append(str).append("0");//右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	@Override
	public Set<String> keys(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return (Set<String>) objRedisTemplate.keys(key);
	}

	@Override
	public void expireObj(String key, Long timeout, TimeUnit unit) {
		objRedisTemplate.expire(key, timeout, unit);
	}

	@Override
	public void putAll(String key, Map<? extends Object, ? extends Object> m) {
		objRedisTemplate.opsForHash().putAll(key, m);

	}

	@Override
	public List<Object> mulitGet(String key, Collection<Object> hashKeys) {
		if (StringUtils.isEmpty(key) || hashKeys == null || hashKeys.size() == 0) {
			return null;
		}

		List<Object> ret = objRedisTemplate.opsForHash().multiGet(key, hashKeys);
		ret.remove(null);
		return ret;
	}

	@Override
	public <T> List<T> mulitGet(String key, Collection<Long> hashKeys, Class<T> clazz) {
		List<Object> keys = new ArrayList<Object>();
		for (Long hashKey : hashKeys) {
			keys.add(hashKey.toString());
		}
		return (List<T>) mulitGet(key, keys);
	}

	@Override
	public Long zCount(String key, double min, double max) {
		if (StringUtils.isEmpty(key)) {
			return 0L;
		}
		Long ret = objRedisTemplate.opsForZSet().count(key, min, max);
		return ret == null ? 0 : ret;
	}

	@Override
	public double zScore(String key, Object value) {
		Double score = objRedisTemplate.opsForZSet().score(key, value);

		if (score == null) {
			return -1;
		}

		return score.doubleValue();
	}

	@Override
	public boolean zContains(String key, Object value) {
		double score = zScore(key, value);

		if (score <= 0) {
			return false;
		}

		return true;
	}

	@Override
	public long ttl(String key) {
		if (StringUtils.isEmpty(key)) {
			return -1;
		}
		
		Long ret = objRedisTemplate.getExpire(key);
		return ret == null ? -1 : ret.longValue();
	}

}
