
package com.albot.kkh.redis.client.service.impl;

import com.albot.kkh.redis.client.service.NewRedisService;
import com.albot.kkh.redis.server.Redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class NewRedisServiceImpl<K, V> implements NewRedisService<K, V> {

	private RedisTemplate<K, V> redisTemplate;

	public RedisTemplate<K, V> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void set(K key, V value) {
		set(key, value, null, null);
	}

	@Override
	public void set(K key, V value, Long timeout, TimeUnit unit) {
		Redis.set(redisTemplate, key, value, timeout, unit);
	}

	@Override
	public void multiSet(Map<K, V> tuples) {
		multiSet(tuples, null, null);
	}

	@Override
	public void multiSet(Map<K, V> tuples, Long timeout, TimeUnit unit) {
		Redis.multiSet(redisTemplate, tuples, timeout, unit);
	}

	@Override
	public String getString(K key) {
		return Redis.get(redisTemplate, key, String.class);
	}

	@Override
	public Byte getByte(K key) {
		Number ret = Redis.get(redisTemplate, key, Number.class);
		return ret == null ? null : ret.byteValue();
	}

	@Override
	public byte getByteValue(K key) {
		Byte val = getByte(key);
		return val == null ? 0 : val.byteValue();
	}

	@Override
	public Integer getInteger(K key) {
		Number ret = Redis.get(redisTemplate, key, Number.class);
		return ret == null ? null : ret.intValue();
	}

	@Override
	public int getIntegerValue(K key) {
		Integer val = getInteger(key);
		return val == null ? 0 : val.intValue();
	}

	@Override
	public Long getLong(K key) {
		Number ret = Redis.get(redisTemplate, key, Number.class);
		return ret == null ? null : ret.longValue();
	}

	@Override
	public long getLongValue(K key) {
		Long val = getLong(key);
		return val == null ? 0 : val.longValue();
	}

	@Override
	public Double getDouble(K key) {
		Number ret = Redis.get(redisTemplate, key, Number.class);
		return ret == null ? null : ret.doubleValue();
	}

	@Override
	public double getDoubleValue(K key) {
		Double val = getDouble(key);
		return val == null ? 0 : val.doubleValue();
	}

	@Override
	public BigDecimal getBigDecimal(K key) {
		Number ret = Redis.get(redisTemplate, key, Number.class);
		return ret == null ? null : new BigDecimal(ret.doubleValue());
	}

	@Override
	public double getBigDecimalValue(K key) {
		BigDecimal val = getBigDecimal(key);
		return val == null ? 0 : val.doubleValue();
	}

	@Override
	public Map<K, String> multiGetString(Collection<? extends K> keys) {
		return Redis.multiGet(redisTemplate, keys, String.class);

	}

	@Override
	public <T> T getObj(K key, Class<T> clazz) {
		return Redis.get(redisTemplate, key, clazz);
	}

	@Override
	public <T> Map<K, T> multiGetObject(Collection<? extends K> keys, Class<T> clazz) {
		return Redis.multiGet(redisTemplate, keys, clazz);
	}

	// list
	@Override
	public void leftPush(K key, V value) {
		leftPush(key, value, null, null);

	}

	@Override
	public void leftPush(K key, V value, Long timeout, TimeUnit unit) {
		Redis.leftPush(redisTemplate, key, value, timeout, unit);
	}

	@Override
	public void leftPushAll(K key, Collection<? extends V> values) {
		leftPushAll(key, values, null, null);
	}

	@Override
	public void leftPushAll(K key, Collection<? extends V> values, Long timeout, TimeUnit unit) {
		Redis.leftPushAll(redisTemplate, key, values, timeout, unit);
	}

	@Override
	public void rightPush(K key, V value) {
		rightPush(key, value, null, null);
	}

	@Override
	public void rightPush(K key, V value, Long timeout, TimeUnit unit) {
		Redis.rightPush(redisTemplate, key, value, timeout, unit);
	}

	@Override
	public void rightPushAll(K key, Collection<? extends V> values) {
		rightPushAll(key, values, null, null);
	}

	@Override
	public void rightPushAll(K key, Collection<? extends V> values, Long timeout, TimeUnit unit) {
		Redis.rightPushAll(redisTemplate, key, values, timeout, unit);
	}

	@Override
	public Byte leftPopByte(K key) {
		Number value = Redis.leftPop(redisTemplate, key, Number.class);
		return value == null ? null : value.byteValue();
	}

	@Override
	public List<Byte> leftPopBytes(K key, int num) {
		List<Number> results = Redis.leftPop(redisTemplate, key, Number.class, num);
		List<Byte> ret = new ArrayList<Byte>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.byteValue());
			}
		}
		return ret;
	}

	@Override
	public Integer leftPopInteger(K key) {
		Number value = Redis.leftPop(redisTemplate, key, Number.class);
		return value == null ? null : value.intValue();
	}

	@Override
	public List<Integer> leftPopIntegers(K key, int num) {
		List<Number> results = Redis.leftPop(redisTemplate, key, Number.class, num);
		List<Integer> ret = new ArrayList<Integer>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.intValue());
			}
		}
		return ret;
	}

	@Override
	public Long leftPopLong(K key) {
		Number value = Redis.leftPop(redisTemplate, key, Number.class);
		return value == null ? null : value.longValue();
	}

	@Override
	public List<Long> leftPopLongs(K key, int num) {
		List<Number> results = Redis.leftPop(redisTemplate, key, Number.class, num);
		List<Long> ret = new ArrayList<Long>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.longValue());
			}
		}
		return ret;
	}

	@Override
	public Double leftPopDouble(K key) {
		Number value = Redis.leftPop(redisTemplate, key, Number.class);
		return value == null ? null : value.doubleValue();
	}

	@Override
	public List<Double> leftPopDoubles(K key, int num) {
		List<Number> results = Redis.leftPop(redisTemplate, key, Number.class, num);
		List<Double> ret = new ArrayList<Double>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.doubleValue());
			}
		}
		return ret;
	}

	@Override
	public BigDecimal leftPopBigDecimal(K key) {
		Number value = Redis.leftPop(redisTemplate, key, Number.class);
		return value == null ? null : new BigDecimal(value.doubleValue());
	}

	@Override
	public List<BigDecimal> leftPopBigDecimals(K key, int num) {
		List<Number> results = Redis.leftPop(redisTemplate, key, Number.class, num);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(new BigDecimal(result.doubleValue()));
			}
		}
		return ret;
	}

	@Override
	public <T> T leftPopObject(K key, Class<T> clazz) {
		return Redis.leftPop(redisTemplate, key, clazz);
	}

	@Override
	public <T> List<T> leftPopObjects(K key, Class<T> clazz, int num) {
		return Redis.leftPop(redisTemplate, key, clazz, num);
	}

	@Override
	public Byte rightPopByte(K key) {
		Number value = Redis.rightPop(redisTemplate, key, Number.class);
		return value == null ? null : value.byteValue();
	}

	@Override
	public List<Byte> rightPopBytes(K key, int num) {
		List<Number> results = Redis.rightPop(redisTemplate, key, Number.class, num);
		List<Byte> ret = new ArrayList<Byte>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.byteValue());
			}
		}
		return ret;
	}

	@Override
	public Integer rightPopInteger(K key) {
		Number value = Redis.rightPop(redisTemplate, key, Number.class);
		return value == null ? null : value.intValue();
	}

	@Override
	public List<Integer> rightPopIntegers(K key, int num) {
		List<Number> results = Redis.rightPop(redisTemplate, key, Number.class, num);
		List<Integer> ret = new ArrayList<Integer>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.intValue());
			}
		}
		return ret;
	}

	@Override
	public Long rightPopLong(K key) {
		Number value = Redis.rightPop(redisTemplate, key, Number.class);
		return value == null ? null : value.longValue();
	}

	@Override
	public List<Long> rightPopLongs(K key, int num) {
		List<Number> results = Redis.rightPop(redisTemplate, key, Number.class, num);
		List<Long> ret = new ArrayList<Long>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.longValue());
			}
		}
		return ret;
	}

	@Override
	public Double rightPopDouble(K key) {
		Number value = Redis.rightPop(redisTemplate, key, Number.class);
		return value == null ? null : value.doubleValue();
	}

	@Override
	public List<Double> rightPopDoubles(K key, int num) {
		List<Number> results = Redis.rightPop(redisTemplate, key, Number.class, num);
		List<Double> ret = new ArrayList<Double>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(result.doubleValue());
			}
		}
		return ret;
	}

	@Override
	public BigDecimal rightPopBigDecimal(K key) {
		Number value = Redis.rightPop(redisTemplate, key, Number.class);
		return value == null ? null : new BigDecimal(value.doubleValue());
	}

	@Override
	public List<BigDecimal> rightPopBigDecimals(K key, int num) {
		List<Number> results = Redis.rightPop(redisTemplate, key, Number.class, num);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();
		if (results != null && results.size() > 0) {
			for (Number result : results) {
				if (result == null) {
					break;
				}
				ret.add(new BigDecimal(result.doubleValue()));
			}
		}
		return ret;
	}

	@Override
	public <T> T rightPopObject(K key, Class<T> clazz) {
		return Redis.rightPop(redisTemplate, key, clazz);
	}

	@Override
	public <T> List<T> rightPopObjects(K key, Class<T> clazz, int num) {
		return Redis.rightPop(redisTemplate, key, clazz, num);
	}

	@Override
	public Byte getByteElementAt(K key, long index) {
		Number value = Redis.elementAt(redisTemplate, key, index, Number.class);
		return value == null ? null : value.byteValue();
	}

	@Override
	public byte getByteValueElementAt(K key, long index) {
		Byte value = getByteElementAt(key, index);
		return value == null ? 0 : value.byteValue();
	}

	@Override
	public Long getLongElementAt(K key, long index) {
		Number value = Redis.elementAt(redisTemplate, key, index, Number.class);
		return value == null ? null : value.longValue();
	}

	@Override
	public long getLongValueElementAt(K key, long index) {
		Long value = getLongElementAt(key, index);
		return value == null ? 0 : value.longValue();
	}

	@Override
	public Integer getIntegerElementAt(K key, long index) {
		Number value = Redis.elementAt(redisTemplate, key, index, Number.class);
		return value == null ? null : value.intValue();
	}

	@Override
	public int getIntegerValueElementAt(K key, long index) {
		Integer value = getIntegerElementAt(key, index);
		return value == null ? 0 : value.intValue();
	}

	@Override
	public Double getDoubleElementAt(K key, long index) {
		Number value = Redis.elementAt(redisTemplate, key, index, Number.class);
		return value == null ? null : value.doubleValue();
	}

	@Override
	public double getDoubleValueElementAt(K key, long index) {
		Double value = getDoubleElementAt(key, index);
		return value == null ? 0 : value.doubleValue();
	}

	@Override
	public BigDecimal getBigDecimalElementAt(K key, long index) {
		Number value = Redis.elementAt(redisTemplate, key, index, Number.class);
		return value == null ? null : new BigDecimal(value.doubleValue());
	}

	@Override
	public double getBigDecimalValueElementAt(K key, long index) {
		BigDecimal value = getBigDecimalElementAt(key, index);
		return value == null ? 0 : value.doubleValue();
	}

	@Override
	public long delFromList(K key, V value) {
		return Redis.lRemove(redisTemplate, key, value, 1);
	}

	@Override
	public long lengthOfList(K key) {
		return Redis.llength(redisTemplate, key);
	}

	@Override
	public void sAdd(K key, V value) {
		sAdd(key, value, null, null);
	}

	@Override
	public void sAdd(K key, V value, Long timeout, TimeUnit unit) {
		Redis.sadd(redisTemplate, key, value, timeout, unit);
	}

	@Override
	public void sAddAll(K key, Collection<? extends V> values) {
		sAddAll(key, values, null, null);
	}

	@Override
	public void sAddAll(K key, Collection<? extends V> values, Long timeout, TimeUnit unit) {
		Redis.saddAll(redisTemplate, key, values, timeout, unit);
	}

	@Override
	public Byte randomByte(K key) {
		List<Number> values = Redis.randomMembers(redisTemplate, key, 1, Number.class);
		return (values == null || values.size() <= 0) ? null : values.get(0).byteValue();
	}

	@Override
	public byte randomByteValue(K key) {
		Byte value = randomByte(key);
		return value == null ? 0 : value.byteValue();
	}

	@Override
	public Integer randomInteger(K key) {
		List<Number> values = Redis.randomMembers(redisTemplate, key, 1, Number.class);
		return (values == null || values.size() <= 0) ? null : values.get(0).intValue();
	}

	@Override
	public int randomIntegerValue(K key) {
		Integer value = randomInteger(key);
		return value == null ? 0 : value.intValue();
	}

	@Override
	public Long randomLong(K key) {
		List<Number> values = Redis.randomMembers(redisTemplate, key, 1, Number.class);
		return (values == null || values.size() <= 0) ? null : values.get(0).longValue();
	}

	@Override
	public long randomLongValue(K key) {
		Long value = randomLong(key);
		return value == null ? 0 : value.longValue();
	}

	@Override
	public Double randomDouble(K key) {
		List<Number> values = Redis.randomMembers(redisTemplate, key, 1, Number.class);
		return (values == null || values.size() <= 0) ? null : values.get(0).doubleValue();
	}

	@Override
	public double randomDoubleValue(K key) {
		Double value = randomDouble(key);
		return value == null ? 0 : value.doubleValue();
	}

	@Override
	public BigDecimal randomBigDecimal(K key) {
		List<Number> values = Redis.randomMembers(redisTemplate, key, 1, Number.class);
		return (values == null || values.size() <= 0) ? null : new BigDecimal(values.get(0).doubleValue());
	}

	@Override
	public double randomBigDecimalValue(K key) {
		BigDecimal value = randomBigDecimal(key);
		return value == null ? 0 : value.doubleValue();
	}

	@Override
	public <T> T randomObject(K key, Class<T> clazz) {
		List<T> values = Redis.randomMembers(redisTemplate, key, 1, clazz);
		return (values == null || values.size() <= 0) ? null : values.get(0);
	}

	@Override
	public List<Byte> randomBytes(K key, int num) {
		List<Number> temp = Redis.randomMembers(redisTemplate, key, num, Number.class);
		List<Byte> ret = new ArrayList<Byte>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.byteValue());
			}
		}

		return ret;
	}

	@Override
	public List<Integer> randomIntegers(K key, int num) {
		List<Number> temp = Redis.randomMembers(redisTemplate, key, num, Number.class);
		List<Integer> ret = new ArrayList<Integer>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.intValue());
			}
		}

		return ret;
	}

	@Override
	public List<Long> randomLongs(K key, int num) {
		List<Number> temp = Redis.randomMembers(redisTemplate, key, num, Number.class);
		List<Long> ret = new ArrayList<Long>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.longValue());
			}
		}

		return ret;
	}

	@Override
	public List<Double> randomDoubles(K key, int num) {
		List<Number> temp = Redis.randomMembers(redisTemplate, key, num, Number.class);
		List<Double> ret = new ArrayList<Double>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.doubleValue());
			}
		}

		return ret;
	}

	@Override
	public List<BigDecimal> randomBigDecimals(K key, int num) {
		List<Number> temp = Redis.randomMembers(redisTemplate, key, num, Number.class);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(new BigDecimal(value.doubleValue()));
			}
		}

		return ret;
	}

	@Override
	public <T> List<T> randomObjects(K key, int num, Class<T> clazz) {
		return Redis.randomMembers(redisTemplate, key, num, clazz);
	}

	@Override
	public List<Byte> randomDistinctBytes(K key, int num) {
		List<Number> temp = Redis.randomDistinctMembers(redisTemplate, key, num, Number.class);
		List<Byte> ret = new ArrayList<Byte>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.byteValue());
			}
		}

		return ret;
	}

	@Override
	public List<Integer> randomDistinctIntegers(K key, int num) {
		List<Number> temp = Redis.randomDistinctMembers(redisTemplate, key, num, Number.class);
		List<Integer> ret = new ArrayList<Integer>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.intValue());
			}
		}

		return ret;
	}

	@Override
	public List<Long> randomDistinctLongs(K key, int num) {
		List<Number> temp = Redis.randomDistinctMembers(redisTemplate, key, num, Number.class);
		List<Long> ret = new ArrayList<Long>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.longValue());
			}
		}

		return ret;
	}

	@Override
	public List<Double> randomDistinctDoubles(K key, int num) {
		List<Number> temp = Redis.randomDistinctMembers(redisTemplate, key, num, Number.class);
		List<Double> ret = new ArrayList<Double>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(value.doubleValue());
			}
		}

		return ret;
	}

	@Override
	public List<BigDecimal> randomDistinctBigDecimals(K key, int num) {
		List<Number> temp = Redis.randomDistinctMembers(redisTemplate, key, num, Number.class);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();

		if (temp != null && temp.size() > 0) {
			for (Number value : temp) {
				if (value == null) {
					break;
				}
				ret.add(new BigDecimal(value.doubleValue()));
			}
		}

		return ret;
	}

	@Override
	public <T> List<T> randomDistinctObjects(K key, int num, Class<T> clazz) {
		return Redis.randomDistinctMembers(redisTemplate, key, num, clazz);
	}

	@Override
	public long sizeOfSet(K key) {
		return Redis.sCard(redisTemplate, key);
	}

	@Override
	public Byte randomPopByte(K key) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, 1, Number.class);
		return (temps == null || temps.size() <= 0) ? null : temps.get(0).byteValue();
	}

	@Override
	public byte randomPopByteValue(K key) {
		Byte value = randomByte(key);
		return value == null ? 0 : value.byteValue();
	}

	@Override
	public Integer randomPopInteger(K key) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, 1, Number.class);
		return (temps == null || temps.size() <= 0) ? null : temps.get(0).intValue();
	}

	@Override
	public int randomPopIntegerValue(K key) {
		Integer value = randomInteger(key);
		return value == null ? 0 : value.intValue();
	}

	@Override
	public Long randomPopLong(K key) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, 1, Number.class);
		return (temps == null || temps.size() <= 0) ? null : temps.get(0).longValue();
	}

	@Override
	public long randomPopLongValue(K key) {
		Long value = randomLong(key);
		return value == null ? 0 : value.longValue();
	}

	@Override
	public Double randomPopDouble(K key) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, 1, Number.class);
		return (temps == null || temps.size() <= 0) ? null : temps.get(0).doubleValue();
	}

	@Override
	public double randomPopDoubleValue(K key) {
		Double value = randomDouble(key);
		return value == null ? 0 : value.doubleValue();
	}

	@Override
	public BigDecimal randomPopBigDecimal(K key) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, 1, Number.class);
		return (temps == null || temps.size() <= 0) ? null : new BigDecimal(temps.get(0).doubleValue());
	}

	@Override
	public double randomPopBigDecimalValue(K key) {
		BigDecimal value = randomBigDecimal(key);
		return value == null ? 0 : value.doubleValue();
	}

	@Override
	public <T> T randomPopObject(K key, Class<T> clazz) {
		List<T> temps = Redis.randomPopMembers(redisTemplate, key, 1, clazz);
		return (temps == null || temps.size() <= 0) ? null : temps.get(0);
	}

	@Override
	public List<Byte> randomPopBytes(K key, int num) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, num, Number.class);
		List<Byte> ret = new ArrayList<Byte>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.byteValue());
			}
		}

		return ret;
	}

	@Override
	public List<Integer> randomPopIntegers(K key, int num) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, num, Number.class);
		List<Integer> ret = new ArrayList<Integer>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.intValue());
			}
		}

		return ret;
	}

	@Override
	public List<Long> randomPopLongs(K key, int num) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, num, Number.class);
		List<Long> ret = new ArrayList<Long>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.longValue());
			}
		}

		return ret;
	}

	@Override
	public List<Double> randomPopDoubles(K key, int num) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, num, Number.class);
		List<Double> ret = new ArrayList<Double>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.doubleValue());
			}
		}

		return ret;
	}

	@Override
	public List<BigDecimal> randomPopBigDecimals(K key, int num) {
		List<Number> temps = Redis.randomPopMembers(redisTemplate, key, num, Number.class);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(new BigDecimal(temp.doubleValue()));
			}
		}

		return ret;
	}

	@Override
	public <T> List<T> randomPopObjects(K key, int num, Class<T> clazz) {
		return Redis.randomPopMembers(redisTemplate, key, num, clazz);
	}

	@Override
	public void zAdd(K key, V value, double score) {
		zAdd(key, value, score, null, null);
	}

	@Override
	public void zAdd(K key, V value, double score, Long timeout, TimeUnit unit) {
		Redis.zAdd(redisTemplate, key, value, score, timeout, unit);
	}

	@Override
	public void zAddAll(K key, Map<V, Double> tuples) {
		zAddAll(key, tuples, null, null);
	}

	@Override
	public void zAddAll(K key, Map<V, Double> tuples, Long timeout, TimeUnit unit) {
		Redis.zAddAll(redisTemplate, key, tuples, timeout, unit);
	}

	@Override
	public long delFromZSet(K key, V value) {
		Set<V> values = new HashSet<V>();
		values.add(value);

		return Redis.zRemove(redisTemplate, key, values);
	}

	@Override
	public long delAllFromZSet(K key, Collection<? extends V> values) {
		return Redis.zRemove(redisTemplate, key, values);
	}

	@Override
	public List<Byte> zRangeBytes(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, false);
		List<Byte> ret = new ArrayList<Byte>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.byteValue());
			}
		}

		return ret;
	}

	@Override
	public List<Integer> zRangeIntegers(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, false);
		List<Integer> ret = new ArrayList<Integer>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.intValue());
			}
		}

		return ret;
	}

	@Override
	public List<Long> zRangeLongs(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, false);
		List<Long> ret = new ArrayList<Long>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.longValue());
			}
		}

		return ret;
	}

	@Override
	public List<Double> zRangeDoubles(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, false);
		List<Double> ret = new ArrayList<Double>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.doubleValue());
			}
		}

		return ret;
	}

	@Override
	public List<BigDecimal> zRangeBigDecimals(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, false);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(new BigDecimal(temp.doubleValue()));
			}
		}

		return ret;
	}

	@Override
	public <T> List<T> zRangeObjects(K key, long start, long end, Class<T> clazz) {
		Set<T> temps = Redis.zrange(redisTemplate, key, start, end, clazz, false);
		List<T> ret = new ArrayList<T>();
		ret.addAll(temps);
		return ret;
	}

	@Override
	public List<Byte> zRevRangeBytes(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, true);
		List<Byte> ret = new ArrayList<Byte>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.byteValue());
			}
		}

		return ret;
	}

	@Override
	public List<Integer> zRevRangeIntegers(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, true);
		List<Integer> ret = new ArrayList<Integer>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.intValue());
			}
		}

		return ret;
	}

	@Override
	public List<Long> zRevRangeLongs(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, true);
		List<Long> ret = new ArrayList<Long>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.longValue());
			}
		}

		return ret;
	}

	@Override
	public List<Double> zRevRangeDoubles(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, true);
		List<Double> ret = new ArrayList<Double>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(temp.doubleValue());
			}
		}

		return ret;
	}

	@Override
	public List<BigDecimal> zRevRangeBigDecimals(K key, long start, long end) {
		Set<Number> temps = Redis.zrange(redisTemplate, key, start, end, Number.class, true);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();

		if (temps != null && temps.size() > 0) {
			for (Number temp : temps) {
				if (temp == null) {
					break;
				}
				ret.add(new BigDecimal(temp.doubleValue()));
			}
		}

		return ret;
	}

	@Override
	public <T> List<T> zRevRangeObjects(K key, long start, long end, Class<T> clazz) {
		Set<T> temps = Redis.zrange(redisTemplate, key, start, end, clazz, true);
		List<T> ret = new ArrayList<T>();
		ret.addAll(temps);
		return ret;
	}

	@Override
	public Map<Byte, Double> zRangeBytesWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, false);
		Map<Byte, Double> ret = new LinkedHashMap<Byte, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().byteValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<Integer, Double> zRangeIntegersWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, false);
		Map<Integer, Double> ret = new LinkedHashMap<Integer, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().intValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<Long, Double> zRangeLongsWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, false);
		Map<Long, Double> ret = new LinkedHashMap<Long, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().longValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<Double, Double> zRangeDoublesWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, false);
		Map<Double, Double> ret = new LinkedHashMap<Double, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().doubleValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<BigDecimal, Double> zRangeBigDecimalsWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, false);
		Map<BigDecimal, Double> ret = new LinkedHashMap<BigDecimal, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(new BigDecimal(value.getValue().doubleValue()), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public <T> Map<T, Double> zRangeObjectsWithScore(K key, long start, long end, Class<T> clazz) {
		Set<TypedTuple<T>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, clazz, false);
		Map<T, Double> ret = new LinkedHashMap<T, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<T> value : values) {
				ret.put(value.getValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<Byte, Double> zRevRangeBytesWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, true);
		Map<Byte, Double> ret = new LinkedHashMap<Byte, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().byteValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<Integer, Double> zRevRangeIntegersWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, true);
		Map<Integer, Double> ret = new LinkedHashMap<Integer, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().intValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<Long, Double> zRevRangeLongsWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, true);
		Map<Long, Double> ret = new LinkedHashMap<Long, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().longValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<Double, Double> zRevRangeDoublesWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, true);
		Map<Double, Double> ret = new LinkedHashMap<Double, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(value.getValue().doubleValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public Map<BigDecimal, Double> zRevRangeBigDecimalsWithScore(K key, long start, long end) {
		Set<TypedTuple<Number>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, Number.class, true);
		Map<BigDecimal, Double> ret = new LinkedHashMap<BigDecimal, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<Number> value : values) {
				ret.put(new BigDecimal(value.getValue().doubleValue()), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public <T> Map<T, Double> zRevRangeObjectsWithScore(K key, long start, long end, Class<T> clazz) {
		Set<TypedTuple<T>> values = Redis.zrangeWithScore(redisTemplate, key, start, end, clazz, true);
		Map<T, Double> ret = new LinkedHashMap<T, Double>();

		if (values != null && values.size() > 0) {
			for (TypedTuple<T> value : values) {
				ret.put(value.getValue(), value.getScore());
			}
		}

		return ret;
	}

	@Override
	public List<Byte> zRangeBytesByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, false);
		List<Byte> ret = new ArrayList<Byte>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.byteValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<Integer> zRangeIntegersByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, false);
		List<Integer> ret = new ArrayList<Integer>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.intValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<Long> zRangeLongByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, false);
		List<Long> ret = new ArrayList<Long>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.longValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<Double> zRangeDoublesByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, false);
		List<Double> ret = new ArrayList<Double>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.doubleValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<BigDecimal> zRangeBigDecimalsByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, false);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(new BigDecimal(value.doubleValue()));
				}
			}
		}

		return ret;
	}

	@Override
	public <T> List<T> zRangeObjectsByScore(K key, double min, double max, long offset, long limit, Class<T> clazz) {
		Set<T> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, clazz, false);
		List<T> ret = new ArrayList<T>();

		if (values != null && values.size() > 0) {
			for (T value : values) {
				if (value != null) {
					ret.add(value);
				}
			}
		}

		return ret;
	}

	@Override
	public List<Byte> zRevRangeBytesByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, true);
		List<Byte> ret = new ArrayList<Byte>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.byteValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<Integer> zRevRangeIntegersByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, true);
		List<Integer> ret = new ArrayList<Integer>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.intValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<Long> zRevRangeLongByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, true);
		List<Long> ret = new ArrayList<Long>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.longValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<Double> zRevRangeDoublesByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, true);
		List<Double> ret = new ArrayList<Double>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(value.doubleValue());
				}
			}
		}

		return ret;
	}

	@Override
	public List<BigDecimal> zRevRangeBigDecimalsByScore(K key, double min, double max, long offset, long limit) {
		Set<Number> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, Number.class, true);
		List<BigDecimal> ret = new ArrayList<BigDecimal>();

		if (values != null && values.size() > 0) {
			for (Number value : values) {
				if (value != null) {
					ret.add(new BigDecimal(value.doubleValue()));
				}
			}
		}

		return ret;
	}

	@Override
	public <T> List<T> zRevRangeObjectsByScore(K key, double min, double max, long offset, long limit, Class<T> clazz) {
		Set<T> values = Redis.zrangeByScore(redisTemplate, key, min, max, offset, limit, clazz, true);
		List<T> ret = new ArrayList<T>();

		if (values != null && values.size() > 0) {
			for (T value : values) {
				if (value != null) {
					ret.add(value);
				}
			}
		}

		return ret;
	}

	@Override
	public Map<Byte, Double> zRevRangeBytesByScoreWithScore(K key, double min, double max, long offset, long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, true);
		Map<Byte, Double> pairs = new LinkedHashMap<Byte, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().byteValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<Long, Double> zRevRangeLongsByScoreWithScore(K key, double min, double max, long offset, long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, true);
		Map<Long, Double> pairs = new LinkedHashMap<Long, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().longValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<Double, Double> zRevRangeDoublesByScoreWithScore(K key, double min, double max, long offset,
			long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, true);
		Map<Double, Double> pairs = new LinkedHashMap<Double, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().doubleValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<BigDecimal, Double> zRevRangeBigDecimalsByScoreWithScore(K key, double min, double max, long offset,
			long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, true);
		Map<BigDecimal, Double> pairs = new LinkedHashMap<BigDecimal, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(new BigDecimal(tuple.getValue().doubleValue()), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<Integer, Double> zRevRangeIntegersByScoreWithScore(K key, double min, double max, long offset,
			long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, true);
		Map<Integer, Double> pairs = new LinkedHashMap<Integer, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().intValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public <T> Map<T, Double> zRevRangeObjectsByScoreWithScore(K key, double min, double max, long offset, long limit,
			Class<T> clazz) {
		Set<TypedTuple<T>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit, clazz,
				true);
		Map<T, Double> pairs = new LinkedHashMap<T, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<T> tuple : tuples) {
				pairs.put(tuple.getValue(), tuple.getScore());
			}
		}
		return pairs;
	}

	@Override
	public Map<Byte, Double> zRangeBytesByScoreWithScore(K key, double min, double max, long offset, long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, false);
		Map<Byte, Double> pairs = new LinkedHashMap<Byte, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().byteValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<Integer, Double> zRangeIntegersByScoreWithScore(K key, double min, double max, long offset, long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, false);
		Map<Integer, Double> pairs = new LinkedHashMap<Integer, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().intValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<Long, Double> zRangeLongsByScoreWithScore(K key, double min, double max, long offset, long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, false);
		Map<Long, Double> pairs = new LinkedHashMap<Long, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().longValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<Double, Double> zRangeDoublesByScoreWithScore(K key, double min, double max, long offset, long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, false);
		Map<Double, Double> pairs = new LinkedHashMap<Double, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(tuple.getValue().doubleValue(), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public Map<BigDecimal, Double> zRangeBigDecimalsByScoreWithScore(K key, double min, double max, long offset,
			long limit) {
		Set<TypedTuple<Number>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit,
				Number.class, false);
		Map<BigDecimal, Double> pairs = new LinkedHashMap<BigDecimal, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<Number> tuple : tuples) {
				pairs.put(new BigDecimal(tuple.getValue().doubleValue()), tuple.getScore());
			}
		}

		return pairs;
	}

	@Override
	public <T> Map<T, Double> zRangeObjectsByScoreWithScore(K key, double min, double max, long offset, long limit,
			Class<T> clazz) {
		Set<TypedTuple<T>> tuples = Redis.zrangeByScoreWithScore(redisTemplate, key, min, max, offset, limit, clazz,
				false);
		Map<T, Double> pairs = new LinkedHashMap<T, Double>();

		if (tuples != null && tuples.size() > 0) {
			for (TypedTuple<T> tuple : tuples) {
				pairs.put(tuple.getValue(), tuple.getScore());
			}
		}
		return pairs;
	}

	@Override
	public double increaseScoreInZset(K key, V value, double delta) {
		return Redis.zIncrBy(redisTemplate, key, value, delta);
	}

	@Override
	public long sizeOfZSet(K key) {
		return Redis.zCard(redisTemplate, key);
	}

	@Override
	public long zRank(K key, V value) {
		return Redis.zRank(redisTemplate, key, value);
	}

	@Override
	public long put(K key, Object hashKey, Object value) {
		return put(key, hashKey, value, null, null);
	}

	@Override
	public long put(K key, Object hashKey, Object value, Long timeout, TimeUnit unit) {
		return Redis.put(redisTemplate, key, hashKey, value, timeout, unit);
	}

	@Override
	public long putAll(K key, Map<Object, Object> value) {
		return putAll(key, value, null, null);
	}

	@Override
	public long putAll(K key, Map<Object, Object> value, Long timeout, TimeUnit unit) {
		return Redis.putAll(redisTemplate, key, value, timeout, unit);
	}

	@Override
	public Byte hGetByte(K key, Object hashKey) {
		Number value = Redis.hGet(redisTemplate, key, hashKey, Number.class);
		return value == null ? null : value.byteValue();
	}

	@Override
	public Integer hGetInteger(K key, Object hashKey) {
		Number value = Redis.hGet(redisTemplate, key, hashKey, Number.class);
		return value == null ? null : value.intValue();
	}

	@Override
	public Long hGetLong(K key, Object hashKey) {
		Number value = Redis.hGet(redisTemplate, key, hashKey, Number.class);
		return value == null ? null : value.longValue();
	}

	@Override
	public Double hGetDouble(K key, Object hashKey) {
		Number value = Redis.hGet(redisTemplate, key, hashKey, Number.class);
		return value == null ? null : value.doubleValue();
	}

	@Override
	public BigDecimal hGetBigDecimal(K key, Object hashKey) {
		Number value = Redis.hGet(redisTemplate, key, hashKey, Number.class);
		return new BigDecimal(value.doubleValue());
	}

	@Override
	public <T> T hGetObject(K key, Object hashKey, Class<T> clazz) {
		return Redis.hGet(redisTemplate, key, hashKey, clazz);
	}

	@Override
	public <T> List<T> hMultiGetObjects(K key, Collection<Object> hashKeys, Class<T> clazz) {
		return Redis.hMultiGet(redisTemplate, key, hashKeys, clazz);
	}

	@Override
	public <T> Map<Object, T> hGetAll(K key, Class<T> clazz) {
		return Redis.hGetAll(redisTemplate, key, clazz);
	}

	@Override
	public boolean hContainKey(K key, Object hashKey) {
		return Redis.hContainKey(redisTemplate, key, hashKey);
	}

	@Override
	public int delFromHash(K key, Object hashKey) {
		return Redis.hDel(redisTemplate, key, new Object[] { hashKey });
	}

	@Override
	public int delAllFromHash(K key, Object[] hashKeys) {
		return Redis.hDel(redisTemplate, key, hashKeys);
	}

	@Override
	public long sizeOfHash(K key) {
		return Redis.hLen(redisTemplate, key);
	}

	@Override
	public void del(K key) {
		Set<K> keys = new HashSet<K>();
		keys.add(key);
		Redis.del(redisTemplate, keys);
	}

	@Override
	public void delAll(Collection<K> keys) {
		Redis.del(redisTemplate, keys);
	}

	@Override
	public boolean hasKey(K key) {
		return Redis.hasKey(redisTemplate, key);
	}

	@Override
	public boolean zContains(K key, V value) {
		return Redis.zContains(redisTemplate, key, value);
	}

	@Override
	public long zCountLargerThan(K key, double min) {
		return Redis.zCount(redisTemplate, key, min + 0.001, Double.MAX_VALUE);
	}

	@Override
	public long zCountSmallerThan(K key, double max) {
		return Redis.zCount(redisTemplate, key, Double.NEGATIVE_INFINITY, max - 0.001);
	}

	@Override
	public boolean lock(K key) {
		Object lockKey = key + ":lock";

		if (Redis.ttl(redisTemplate, (K) lockKey) < 0) {
			del((K) lockKey.toString());
		}

		if (Redis.incrBy(redisTemplate, (K) lockKey, 1L, 1L, TimeUnit.MINUTES) <= 1) {
			return true;
		}

		return false;
	}

	@Override
	public void unlock(K key) {
		String lockKey = key + ":lock";
		Redis.del(redisTemplate, (K) lockKey);
	}

	@Override
	public double zScore(K key, V value) {
		return Redis.zScore(redisTemplate, key, value);
	}

	@Override
	public String getStringElementAt(K key, long index) {
		return Redis.elementAt(redisTemplate, key, index, String.class);
	}

	@Override
	public long incr(K key) {
		return Redis.incrBy(redisTemplate, key, 1L);
	}

	@Override
	public long incrBy(K key, long delta) {
		return Redis.incrBy(redisTemplate, key, delta);
	}

	@Override
	public void setTimeOut(K key, long expireTime, TimeUnit unit) {
		Redis.expire(redisTemplate, key, expireTime, unit);
	}

    @Override
    public long ttl(K key) {
        return Redis.ttl(redisTemplate, key);
    }

    @Override
    public void addToZSets(Collection<K> keys, V value, double score) {
        addToZSets(keys, value, score, null, null);
    }

    @Override
    public void addToZSets(Collection<K> keys, V value, double score, Long timeout, TimeUnit unit) {
        Redis.addToZSets(redisTemplate, keys, value, score, timeout, unit);
    }

    @Override
    public void addAllToZSets(Collection<K> keys, Map<V, Double> tuples) {
        addAllToZSets(keys, tuples, null, null);
    }

    @Override
    public void addAllToZSets(Collection<K> keys, Map<V, Double> tuples, Long timeout, TimeUnit unit) {
        Redis.addAllToZSets(redisTemplate, keys, tuples, timeout, unit);
    }

    @Override
    public void delFromZsets(Collection<K> keys, V value) {
        Redis.delFromZSets(redisTemplate, keys, value);
    }

    @Override
    public void delAllFromZsets(Collection<K> keys, Collection<V> values) {
        Redis.delAllFromZSets(redisTemplate, keys, values);
    }

    @Override
    public List<Byte> lRangeBytes(K key, long start, long end) {
        List<Number> values = Redis.lRange(redisTemplate, key, start, end, Number.class);
        if (values == null || values.size() <= 0) {
            return null;
        }
        List<Byte> ret = new ArrayList<Byte>();
        for (Number value : values) {
            ret.add(value.byteValue());
        }
        return ret;
    }

    @Override
    public List<Integer> lRangeIntegers(K key, long start, long end) {
        List<Number> values = Redis.lRange(redisTemplate, key, start, end, Number.class);
        if (values == null || values.size() <= 0) {
            return null;
        }
        List<Integer> ret = new ArrayList<Integer>();
        for (Number value : values) {
            ret.add(value.intValue());
        }
        return ret;
    }

    @Override
    public List<Long> lRangeLongs(K key, long start, long end) {
        List<Number> values = Redis.lRange(redisTemplate, key, start, end, Number.class);
        if (values == null || values.size() <= 0) {
            return null;
        }
        List<Long> ret = new ArrayList<Long>();
        for (Number value : values) {
            ret.add(value.longValue());
        }
        return ret;
    }

    @Override
    public List<Double> lRangeDoubles(K key, long start, long end) {
        List<Number> values = Redis.lRange(redisTemplate, key, start, end, Number.class);
        if (values == null || values.size() <= 0) {
            return null;
        }
        List<Double> ret = new ArrayList<Double>();
        for (Number value : values) {
            ret.add(value.doubleValue());
        }
        return ret;
    }

    @Override
    public List<BigDecimal> lRangeBigDecimals(K key, long start, long end) {
        List<Number> values = Redis.lRange(redisTemplate, key, start, end, Number.class);
        if (values == null || values.size() <= 0) {
            return null;
        }
        List<BigDecimal> ret = new ArrayList<BigDecimal>();
        for (Number value : values) {
            ret.add(new BigDecimal(value.doubleValue()));
        }
        return ret;
    }

    @Override
    public <T> List<T> lRangeObjects(K key, long start, long end, Class<T> clazz) {
        return Redis.lRange(redisTemplate, key, start, end, clazz);
    }

	@Override
	public long sRemove(K key, V[] value) {
		return Redis.sRemove(redisTemplate, key, value);
	}

	@Override
	public boolean sIsMember(K key, V value) {
		return Redis.isMember(redisTemplate, key, value);
	}

	@Override
	public double hIncrBy(K key, Object hashKey, double delta) {
		Double ret = Redis.hIncrBy(redisTemplate, key, hashKey, delta);
		return ret == null ? -1 : ret.doubleValue();
	}
}
