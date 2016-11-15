package com.albot.kkh.redis.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javafx.scene.chart.ValueAxis;
import org.omg.IOP.ComponentIdHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

public class Redis {

	public static <K, V, T> T execute(RedisTemplate<K, V> redisTemplate, RedisCallback<T> action, boolean pipelined) {
		if (action == null) {
			return null;
		}

		return redisTemplate.execute(action, false, pipelined);
	}

	public static Long convert(Long source, TimeUnit unit) {
		// 默认为毫秒，最终转化结果为秒
		if (source == null) {
			return null;
		}

		if (unit == null) {
			unit = TimeUnit.MILLISECONDS;
		}

		if (unit == TimeUnit.MILLISECONDS) {
			source = source / 1000;
		} else if (unit == TimeUnit.MINUTES) {
			source = source * 60;
		} else if (unit == TimeUnit.HOURS) {
			source = source * 60 * 60;
		} else if (unit == TimeUnit.DAYS) {
			source = source * 60 * 60 * 24;
		}

		if (source.longValue() <= 0) {
			return 1L;
		}

		return source.longValue();
	}

	// Object
	public static final <K, V> void set(final RedisTemplate<K, V> redisTemplate, final K key, final V value,
			final Long expireTime, TimeUnit unit) {
		if (key == null || StringUtils.isEmpty(key.toString()) || value == null
				|| StringUtils.isEmpty(value.toString())) {
			return;
		}

		if (expireTime == null || expireTime.longValue() <= 0) {
			redisTemplate.opsForValue().set(key, value);
		} else {
			if (unit == null) {
				unit = TimeUnit.MILLISECONDS;
			}
			redisTemplate.opsForValue().set(key, value, expireTime, unit);
		}
	}

	public static final <K, V> void multiSet(final RedisTemplate<K, V> redisTemplate,
			final Map<? extends K, ? extends V> pairs, Long expireTime, TimeUnit unit) {
		if (pairs == null || pairs.size() <= 0) {
			return;
		}

		if (unit == null) {
			unit = TimeUnit.MILLISECONDS;
		}

		final Long finalExpireTime = Redis.convert(expireTime, unit);

		if (finalExpireTime == null || finalExpireTime.longValue() <= 0) {
			redisTemplate.opsForValue().multiSet(pairs);
			return;
		}

		redisTemplate.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				for (Object key : pairs.keySet()) {
					Object value = pairs.get(key);
					if (key == null || StringUtils.isEmpty(key.toString()) || value == null
							|| StringUtils.isEmpty(value)) {
						continue;
					}
					connection.set(serializeKey(redisTemplate, key), serializeValue(redisTemplate, value));
					if (finalExpireTime != null && finalExpireTime.longValue() > 0) {
						connection.expire(serializeKey(redisTemplate, key), finalExpireTime);
					}
				}
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static <K, V, T> T get(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz) {
		return (T) redisTemplate.opsForValue().get(key);
	}

	@SuppressWarnings("unchecked")
	public static <K, V, T> Map<K, T> multiGet(RedisTemplate<K, V> redisTemplate, Collection<? extends K> keys,
			Class<T> clazz) {
		List<T> values = (List<T>) redisTemplate.opsForValue().multiGet((Collection<K>) keys);
		Map<K, T> ret = new LinkedHashMap<K, T>();
		int i = 0;
		for (K key : keys) {
			ret.put(key, values.get(i++));
		}
		return ret;
	}

	public static <K, V> void del(RedisTemplate<K, V> redisTemplate, Collection<K> keys) {
		redisTemplate.delete(keys);
	}

	public static <K, V> void del(RedisTemplate<K, V> redisTemplate, K key) {
		redisTemplate.delete(key);
	}

	public static <K, V> boolean hasKey(RedisTemplate<K, V> redisTemplate, K key) {
		return redisTemplate.hasKey(key);
	}

	// Set
	@SuppressWarnings("unchecked")
	public static <K, V> void sadd(final RedisTemplate<K, V> redisTemplate, final K key, final V value, Long expireTime,
			TimeUnit unit) {
		if (key == null || StringUtils.isEmpty(key.toString()) || value == null
				|| StringUtils.isEmpty(value.toString())) {
			return;
		}
		final Long finalExpireTime = Redis.convert(expireTime, unit);
		if (expireTime == null || expireTime.longValue() <= 0) {
			redisTemplate.opsForSet().add(key, value);
		} else {
			redisTemplate.executePipelined(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					connection.sAdd(serializeKey(redisTemplate, key), serializeValue(redisTemplate, value));
					connection.expire(serializeKey(redisTemplate, key), finalExpireTime);
					return null;
				}
			});
		}
	}

	public static <K, V> void saddAll(final RedisTemplate<K, V> redisTemplate, final K key,
			final Collection<? extends V> values, Long expireTime, TimeUnit unit) {
		if (key == null || StringUtils.isEmpty(key.toString()) || values == null || values.size() <= 0) {
			return;
		}

		final Long finalExpireTime = Redis.convert(expireTime, unit);

		if (finalExpireTime == null || finalExpireTime.longValue() <= 0) {
			redisTemplate.opsForSet().add(key, (V[]) values.toArray());
		} else {
			redisTemplate.executePipelined(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[][] valuesBytes = new byte[values.size()][];
					int i = 0;
					for (V value : values) {
						valuesBytes[i++] = serializeValue(redisTemplate, value);
					}
					connection.sAdd(serializeKey(redisTemplate, key), valuesBytes);
					connection.expire(serializeKey(redisTemplate, key), finalExpireTime);
					return null;
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> sDiff(RedisTemplate<K, V> redisTemplate, K key1, K key2, Class<T> clazz) {
		if (StringUtils.isEmpty(key1) || StringUtils.isEmpty(key2)) {
			return null;
		}
		return (Set<T>) redisTemplate.opsForSet().difference(key1, key2);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> sDiff(RedisTemplate<K, V> redisTemplate, K key, Collection<K> otherKeys,
			Class<T> clazz) {
		if (StringUtils.isEmpty(key) || otherKeys == null || otherKeys.size() <= 0) {
			return null;
		}
		return (Set<T>) redisTemplate.opsForSet().difference(key, otherKeys);
	}

	public static final <K, V, T> long sDiffAndStore(RedisTemplate<K, V> redisTemplate, K key1, K key2, K storeKey) {

		if (StringUtils.isEmpty(key1) || StringUtils.isEmpty(key2) || StringUtils.isEmpty(storeKey)) {
			return 0;
		}

		Long ret = redisTemplate.opsForSet().differenceAndStore(key1, key2, storeKey);

		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V, T> long sDiffAndStore(RedisTemplate<K, V> redisTemplate, K key1, Collection<K> otherKeys,
			K storeKey) {

		if (StringUtils.isEmpty(key1) || otherKeys == null || otherKeys.size() <= 0 || StringUtils.isEmpty(storeKey)) {
			return 0;
		}

		Long ret = redisTemplate.opsForSet().differenceAndStore(key1, otherKeys, storeKey);

		return ret == null ? 0 : ret.longValue();
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> sInter(RedisTemplate<K, V> redisTemplate, K key1, K key2, Class<T> clazz) {
		if (StringUtils.isEmpty(key1) || StringUtils.isEmpty(key2)) {
			return null;
		}
		return (Set<T>) redisTemplate.opsForSet().intersect(key1, key2);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> sInter(RedisTemplate<K, V> redisTemplate, K key1, Collection<K> otherKeys,
			Class<T> clazz) {
		if (StringUtils.isEmpty(key1) || otherKeys == null || otherKeys.size() <= 0) {
			return null;
		}

		return (Set<T>) redisTemplate.opsForSet().intersect(key1, otherKeys);
	}

	public static final <K, V, T> long sInterAndStore(RedisTemplate<K, V> redisTemplate, K key1, K key2, K storeKey) {
		if (StringUtils.isEmpty(key1) || StringUtils.isEmpty(key2) || StringUtils.isEmpty(storeKey)) {
			return 0;
		}

		Long ret = redisTemplate.opsForSet().intersectAndStore(key1, key2, storeKey);

		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V, T> long sInterAndStore(RedisTemplate<K, V> redisTemplate, K key1,
			Collection<K> otherKeys, K storeKey) {
		if (StringUtils.isEmpty(key1) || otherKeys == null || otherKeys.size() <= 0 || StringUtils.isEmpty(storeKey)) {
			return 0;
		}

		Long ret = redisTemplate.opsForSet().intersectAndStore(key1, otherKeys, storeKey);

		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V> boolean isMember(RedisTemplate<K, V> redisTemplate, K key, V value) {
		if (StringUtils.isEmpty(key) || value == null) {
			return false;
		}
		return redisTemplate.opsForSet().isMember(key, value);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> members(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return (Set<T>) redisTemplate.opsForSet().members(key);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> List<T> randomMembers(RedisTemplate<K, V> redisTemplate, K key, int count,
			Class<T> clazz) {
		if (StringUtils.isEmpty(key) || count <= 0) {
			return null;
		}

		if (count == 1) {
			List<T> ret = new ArrayList<T>();
			ret.add((T) redisTemplate.opsForSet().randomMember(key));
			return ret;
		}

		return (List<T>) redisTemplate.opsForSet().randomMembers(key, count);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> List<T> randomDistinctMembers(RedisTemplate<K, V> redisTemplate, K key, int count,
			Class<T> clazz) {
		if (StringUtils.isEmpty(key) || count <= 0) {
			return null;
		}

		if (count == 1) {
			List<T> ret = new ArrayList<T>();
			ret.add((T) redisTemplate.opsForSet().randomMember(key));
			return ret;
		}

		return (List<T>) redisTemplate.opsForSet().distinctRandomMembers(key, count);
	}

	public static final <K, V, T> List<T> randomPopMembers(final RedisTemplate<K, V> redisTemplate, final K key,
			final int count, final Class<T> clazz) {
		if (StringUtils.isEmpty(key) || count <= 0) {
			return null;
		}
		List<T> ret = new ArrayList<T>();
		RedisCallback<List<T>> action = new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				List<T> ret = new ArrayList<T>();
				byte[] keyBytes = serializeKey(redisTemplate, key);
				for (int i = 0; i < count; i++) {
					byte[] value = connection.sPop(keyBytes);
					if (value == null || value.length <= 0) {
						break;
					}
					ret.add(deserializeValue(redisTemplate, value, clazz));
				}
				return ret;
			}
		};
		ret = execute(redisTemplate, action, false);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> union(RedisTemplate<K, V> redisTemplate, K key1, K key2, Class<T> clazz) {
		if (StringUtils.isEmpty(key1) || StringUtils.isEmpty(key2)) {
			return null;
		}

		return (Set<T>) redisTemplate.opsForSet().union(key1, key2);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> union(RedisTemplate<K, V> redisTemplate, K key1, Collection<K> otherKeys,
			Class<T> clazz) {
		if (StringUtils.isEmpty(key1) || otherKeys == null || otherKeys.size() <= 0) {
			return null;
		}

		return (Set<T>) redisTemplate.opsForSet().union(key1, otherKeys);
	}

	public static final <K, V, T> long unionAndStore(RedisTemplate<K, V> redisTemplate, K key1, K key2, K storeKey) {
		if (StringUtils.isEmpty(key1) || StringUtils.isEmpty(key2) || StringUtils.isEmpty(storeKey)) {
			return 0;
		}

		Long ret = redisTemplate.opsForSet().unionAndStore(key1, key2, storeKey);

		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V, T> long unionAndStore(RedisTemplate<K, V> redisTemplate, K key1, Collection<K> otherKeys,
			K storeKey) {
		if (StringUtils.isEmpty(key1) || otherKeys == null || otherKeys.size() <= 0 || StringUtils.isEmpty(storeKey)) {
			return 0;
		}

		Long ret = redisTemplate.opsForSet().unionAndStore(key1, otherKeys, storeKey);

		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V> long sRemove(RedisTemplate<K, V> redisTemplate, K key, V[] values) {
		if (StringUtils.isEmpty(key) || values == null || values.length <= 0) {
			return 0;
		}

		Long count = redisTemplate.opsForSet().remove(key, values);

		return count == null ? 0 : count.longValue();

	}

	public static final <K, V> long sCard(RedisTemplate<K, V> redisTemplate, K key) {
		if (StringUtils.isEmpty(key)) {
			return 0;
		}

		Long size = redisTemplate.opsForSet().size(key);

		return size == null ? 0 : size.longValue();
	}

	// List
	public static final <K, V> long leftPush(final RedisTemplate<K, V> redisTemplate, final K key, final V value,
			Long expireTime, TimeUnit unit) {
		if (key == null || StringUtils.isEmpty(key) || value == null || StringUtils.isEmpty(value.toString())) {
			return 0L;
		}
		if (unit == null) {
			unit = TimeUnit.MILLISECONDS;
		}

		Long count = 0L;

		final Long finalExpireTime = Redis.convert(expireTime, unit);
		if (finalExpireTime == null) {
			count = redisTemplate.opsForList().leftPush(key, value);
		} else {
			RedisCallback<Long> action = new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					Long result = connection.lPush(serializeKey(redisTemplate, key),
							serializeValue(redisTemplate, value));
					connection.expire(serializeKey(redisTemplate, key), finalExpireTime);
					return result;
				}
			};
			count = execute(redisTemplate, action, true);
		}

		return count == null ? 0 : count.longValue();
	}

	public static final <K, V> long leftPushAll(final RedisTemplate<K, V> redisTemplate, final K key,
			final Collection<? extends V> values, Long expireTime, TimeUnit unit) {
		if (StringUtils.isEmpty(key) || values == null || values.size() <= 0) {
			return 0;
		}

		if (unit == null) {
			unit = TimeUnit.MICROSECONDS;
		}

		Long count = 0L;

		final Long finalExpireSeconds = Redis.convert(expireTime, unit);
		if (finalExpireSeconds == null || finalExpireSeconds.longValue() <= 0) {
			count = redisTemplate.opsForList().leftPushAll(key, new ArrayList<V>(values));
		} else {
			RedisCallback<Long> action = new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					byte[][] valueBytes = new byte[values.size()][];
					int i = 0;
					for (Object value : values) {
						valueBytes[i++] = serializeValue(redisTemplate, value);
					}
					Long result = connection.lPush(serializeKey(redisTemplate, key), valueBytes);
					connection.expire(serializeKey(redisTemplate, key), finalExpireSeconds);
					return result;
				}
			};
			count = Redis.execute(redisTemplate, action, true);
		}

		return count == null ? 0l : count.longValue();
	}

	public static final <K, V> long rightPush(final RedisTemplate<K, V> redisTemplate, final K key, final V value,
			Long expireTime, TimeUnit unit) {
		if (key == null || StringUtils.isEmpty(key) || value == null || StringUtils.isEmpty(value.toString())) {
			return 0L;
		}
		if (unit == null) {
			unit = TimeUnit.MILLISECONDS;
		}

		Long count = 0L;

		final Long finalExpireTime = Redis.convert(expireTime, unit);
		if (finalExpireTime == null) {
			count = redisTemplate.opsForList().rightPush(key, value);
		} else {
			RedisCallback<Long> action = new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					Long result = connection.rPush(serializeKey(redisTemplate, key),
							serializeValue(redisTemplate, value));
					connection.expire(serializeKey(redisTemplate, key), finalExpireTime);
					return result;
				}
			};
			count = Redis.execute(redisTemplate, action, true);
		}

		return count == null ? 0 : count.longValue();
	}

	public static final <K, V> long rightPushAll(final RedisTemplate<K, V> redisTemplate, final K key,
			final Collection<? extends V> values, Long expireTime, TimeUnit unit) {
		if (StringUtils.isEmpty(key) || values == null || values.size() <= 0) {
			return 0;
		}

		if (unit == null) {
			unit = TimeUnit.MICROSECONDS;
		}

		Long count = 0L;

		final Long finalExpireSeconds = Redis.convert(expireTime, unit);
		if (finalExpireSeconds == null || finalExpireSeconds.longValue() <= 0) {
			count = redisTemplate.opsForList().rightPushAll(key, new ArrayList<V>(values));
		} else {
			RedisCallback<Long> action = new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					byte[][] valueBytes = new byte[values.size()][];
					int i = 0;
					for (Object value : values) {
						valueBytes[i++] = serializeValue(redisTemplate, value);
					}
					Long result = connection.rPush(serializeKey(redisTemplate, key), valueBytes);
					connection.expire(serializeKey(redisTemplate, key), finalExpireSeconds);
					return result;
				}
			};
			count = Redis.execute(redisTemplate, action, true);
		}

		return count == null ? 0l : count.longValue();
	}

	public static final <K, V> long llength(RedisTemplate<K, V> redisTemplate, K key) {
		if (StringUtils.isEmpty(key)) {
			return 0L;
		}
		return redisTemplate.opsForList().size(key);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> List<T> range(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz, long start,
			long end) {
		if (StringUtils.isEmpty(key) || clazz == null || start > end) {
			return new ArrayList<T>();
		}
		return (List<T>) redisTemplate.opsForList().range(key, start, end);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> T leftPop(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return (T) redisTemplate.opsForList().leftPop(key);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> List<T> lRange(RedisTemplate<K, V> redisTemplate, K key, long start, long end, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return (List<T>) redisTemplate.opsForList().range(key, start, end);
	}

	public static final <K, V, T> List<T> leftPop(final RedisTemplate<K, V> redisTemplate, final K key,
			final Class<T> clazz, final int num) {
		List<T> ret = new ArrayList<T>();
		if (StringUtils.isEmpty(key) || num <= 0) {
			return ret;
		}

		if (num == 1) {
			ret.add(leftPop(redisTemplate, key, clazz));
			return ret;
		}

		RedisCallback<List<T>> action = new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				List<T> ret = new ArrayList<T>();
				byte[] keyBytes = serializeKey(redisTemplate, key);

				for (int i = 0; i < num; i++) {
					byte[] value = connection.lPop(keyBytes);
					if (value == null || value.length <= 0) {
						break;
					}
					ret.add(deserializeValue(redisTemplate, value, clazz));
				}
				return ret;
			}
		};

		return Redis.execute(redisTemplate, action, false);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> T rightPop(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return (T) redisTemplate.opsForList().rightPop(key);
	}

	public static final <K, V, T> List<T> rightPop(final RedisTemplate<K, V> redisTemplate, final K key,
			final Class<T> clazz, final int num) {
		List<T> ret = new ArrayList<T>();
		if (StringUtils.isEmpty(key) || num <= 0) {
			return ret;
		}

		if (num == 1) {
			ret.add(rightPop(redisTemplate, key, clazz));
			return ret;
		}

		RedisCallback<List<T>> action = new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				List<T> ret = new ArrayList<T>();
				byte[] keyBytes = serializeKey(redisTemplate, key);

				for (int i = 0; i < num; i++) {
					byte[] value = connection.rPop(keyBytes);
					if (value == null || value.length <= 0) {
						break;
					}
					ret.add(deserializeValue(redisTemplate, value, clazz));
				}
				return ret;
			}
		};

		return Redis.execute(redisTemplate, action, false);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> T elementAt(RedisTemplate<K, V> redisTemplate, K key, long index, Class<T> clazz) {
		if (StringUtils.isEmpty(key) || index < 0) {
			return null;
		}
		return (T) redisTemplate.opsForList().index(key, index);
	}

	public static final <K, V> long lRemove(RedisTemplate<K, V> redisTemplate, K key, V value, long count) {
		if (StringUtils.isEmpty(key) || value == null) {
			return 0;
		}
		Long ret = redisTemplate.opsForList().remove(key, count, value);
		return ret == null ? 0 : ret.longValue();
	}

	// Zset
	public static final <K, V> long zAdd(final RedisTemplate<K, V> redisTemplate, final K key, final V value,
			final double score, Long expireTime, TimeUnit unit) {
		if (StringUtils.isEmpty(key)) {
			return 0l;
		}

		if (expireTime == null || expireTime.longValue() <= 0) {
			return redisTemplate.opsForZSet().add(key, value, score) ? 1l : 0l;
		}

		unit = unit == null ? TimeUnit.MILLISECONDS : unit;

		final Long expireSeconds = Redis.convert(expireTime, unit);

		RedisCallback<Long> action = new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyBytes = serializeKey(redisTemplate, key);
				Long ret = connection.zAdd(keyBytes, score, serializeValue(redisTemplate, value)) ? 1L : 0L;
				connection.expire(keyBytes, expireSeconds);
				return ret;
			}
		};

		return Redis.execute(redisTemplate, action, true);
	}

    public static final <K, V> void addToZSets(final RedisTemplate<K, V> template, final Collection<K> keys,
                                               final V value, final double score, Long timeout, TimeUnit unit) {
        if (keys == null || keys.size() <= 0 || value == null) {
            return;
        }

        unit = unit == null ? TimeUnit.MILLISECONDS : unit;
        final Long seconds = Redis.convert(timeout, unit);

        RedisCallback<Long> action = new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] rawValue = serializeValue(template, value);
                for (K key : keys) {
                    byte[] rawKey = serializeKey(template, key);
                    connection.zAdd(rawKey, score, rawValue);
                    if (seconds != null) {
                        connection.expire(rawKey, seconds);
                    }
                }
                return null;
            }
        };

        Redis.execute(template, action, true);
    }

    public static final <K, V> void addAllToZSets(final RedisTemplate<K, V> template, final Collection<K> keys,
                                                  final Map<V, Double> tuples, Long timeout, TimeUnit unit) {
        if (keys == null || keys.size() <= 0 || tuples == null || tuples.size() <= 0) {
            return;
        }

        unit = unit == null ? TimeUnit.MILLISECONDS : unit;
        final Long seconds = Redis.convert(timeout, unit);

        RedisCallback<Long> action = new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[][] rawValues = new byte[tuples.size()][];
                double[] scores = new double[tuples.size()];
                int i = 0;

                for (Map.Entry<V, Double> tuple : tuples.entrySet()) {
                    rawValues[i] = serializeValue(template, tuple.getKey());
                    scores[i] = tuple.getValue();
                    i++;
                }

                for (K key : keys) {
                    byte[] rawKey = serializeKey(template, key);
                    for (i = 0; i < rawValues.length; i++) {
                        connection.zAdd(rawKey, scores[i], rawValues[i]);
                    }
                    if (seconds != null) {
                        connection.expire(rawKey, seconds);
                    }
                }
                return null;
            }
        };

        Redis.execute(template, action, true);
    }

    public static final <K, V> long zAddAll(final RedisTemplate<K, V> redisTemplate, final K key,
                                            final Map<V, Double> tuples, Long expireTime, TimeUnit unit) {
        if (StringUtils.isEmpty(key) || tuples == null || tuples.size() <= 0) {
            return 0;
        }

		final Long expireSeconds = Redis.convert(expireTime, unit);

		RedisCallback<Long> action = new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long ret = 0;

				byte[] keyBytes = serializeKey(redisTemplate, key);
				for (Entry<V, Double> tuple : tuples.entrySet()) {
					double score = tuple.getValue() == null ? 0 : tuple.getValue().doubleValue();
					byte[] value = serializeValue(redisTemplate, tuple.getKey());
					connection.zAdd(keyBytes, score, value);
				}

				if (expireSeconds != null && expireSeconds.longValue() > 0) {
					connection.expire(keyBytes, expireSeconds);
				}
				return ret;
			}
		};

		return Redis.execute(redisTemplate, action, true);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> zrange(RedisTemplate<K, V> redisTemplate, K key, long start, long end,
			Class<T> clazz, boolean reverse) {
		if (key == null || StringUtils.isEmpty(key) || start > end) {
			return new LinkedHashSet<T>();
		}
		if (reverse) {
			return (Set<T>) redisTemplate.opsForZSet().reverseRange(key, start, end);
		}
		return (Set<T>) redisTemplate.opsForZSet().range(key, start, end);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> zrangeByScore(RedisTemplate<K, V> redisTemplate, K key, double min, double max,
			Class<T> clazz, boolean reverse) {
		if (key == null || StringUtils.isEmpty(key) || min > max) {
			return new LinkedHashSet<T>();
		}

		if (reverse) {
			return (Set<T>) redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
		}
		return (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> zrangeByScore(RedisTemplate<K, V> redisTemplate, K key, double min, double max,
			long offset, long limit, Class<T> clazz, boolean reverse) {
		if (StringUtils.isEmpty(key) || min > max) {
			return new LinkedHashSet<T>();
		}
		if (offset < 0) {
			offset = 0;
		}
		if (reverse) {
			return (Set<T>) redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, limit);
		}
		return (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, limit);
	}

	public static final <K, V, T> Set<TypedTuple<T>> zrangeWithScore(final RedisTemplate<K, V> redisTemplate,
			final K key, final long start, final long end, final Class<T> clazz, final boolean reverse) {
		if (key == null || StringUtils.isEmpty(key) || start > end) {
			return new LinkedHashSet<TypedTuple<T>>();
		}
		RedisCallback<Set<TypedTuple<T>>> action = new RedisCallback<Set<TypedTuple<T>>>() {
			@Override
			public Set<TypedTuple<T>> doInRedis(RedisConnection connection) throws DataAccessException {
				Set<TypedTuple<T>> ret = new LinkedHashSet<ZSetOperations.TypedTuple<T>>();
				Set<Tuple> tuples = null;
				if (reverse) {
					tuples = connection.zRevRangeWithScores(serializeKey(redisTemplate, key), start, end);
				} else {
					tuples = connection.zRangeWithScores(serializeKey(redisTemplate, key), start, end);
				}

				if (tuples != null && tuples.size() > 0) {
					for (Tuple tuple : tuples) {
						ret.add(new DefaultTypedTuple<T>(deserializeValue(redisTemplate, tuple.getValue(), clazz),
								tuple.getScore()));
					}
				}
				return ret;
			}
		};
		return execute(redisTemplate, action, false);
	}

	public static final <K, V, T> Set<TypedTuple<T>> zrangeByScoreWithScore(final RedisTemplate<K, V> redisTemplate,
			final K key, final double min, final double max, final Class<T> clazz, final boolean reverse) {
		if (StringUtils.isEmpty(key) || min > max) {
			return new LinkedHashSet<TypedTuple<T>>();
		}

		RedisCallback<Set<TypedTuple<T>>> action = new RedisCallback<Set<TypedTuple<T>>>() {
			@Override
			public Set<TypedTuple<T>> doInRedis(RedisConnection connection) throws DataAccessException {
				Set<TypedTuple<T>> ret = new LinkedHashSet<ZSetOperations.TypedTuple<T>>();
				Set<Tuple> tuples = null;
				if (reverse) {
					tuples = connection.zRevRangeByScoreWithScores(serializeKey(redisTemplate, key), min, max);
				} else {
					tuples = connection.zRangeByScoreWithScores(serializeKey(redisTemplate, key), min, max);
				}

				if (tuples != null && tuples.size() > 0) {
					for (Tuple tuple : tuples) {
						ret.add(new DefaultTypedTuple<T>(deserializeValue(redisTemplate, tuple.getValue(), clazz),
								tuple.getScore()));
					}
				}
				return ret;
			}
		};

		return execute(redisTemplate, action, false);
	}

	public static final <K, V, T> Set<TypedTuple<T>> zrangeByScoreWithScore(final RedisTemplate<K, V> redisTemplate,
			final K key, final double min, final double max, final long offset, final long limit, final Class<T> clazz,
			final boolean reverse) {
		if (StringUtils.isEmpty(key) || min > max) {
			return new LinkedHashSet<TypedTuple<T>>();
		}

		RedisCallback<Set<TypedTuple<T>>> action = new RedisCallback<Set<TypedTuple<T>>>() {
			@Override
			public Set<TypedTuple<T>> doInRedis(RedisConnection connection) throws DataAccessException {
				Set<TypedTuple<T>> ret = new LinkedHashSet<ZSetOperations.TypedTuple<T>>();
				Set<Tuple> tuples = null;
				if (reverse) {
					tuples = connection.zRevRangeByScoreWithScores(serializeKey(redisTemplate, key), min, max, offset,
							limit);
				} else {
					tuples = connection.zRangeByScoreWithScores(serializeKey(redisTemplate, key), min, max, offset,
							limit);
				}

				if (tuples != null && tuples.size() > 0) {
					for (Tuple tuple : tuples) {
						ret.add(new DefaultTypedTuple<T>(deserializeValue(redisTemplate, tuple.getValue(), clazz),
								tuple.getScore()));
					}
				}
				return ret;
			}
		};

		return execute(redisTemplate, action, false);
	}

	public static final <K, V> double zIncrBy(RedisTemplate<K, V> redisTemplate, K key, V value, double delta) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return 0;
		}
		Double ret = redisTemplate.opsForZSet().incrementScore(key, value, delta);
		return ret == null ? 0 : ret.doubleValue();
	}

	public static final <K, V> long zRemove(RedisTemplate<K, V> redisTemplate, K key, Collection<? extends V> values) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(values) || values.size() <=0) {
			return 0;
		}
		Long ret = redisTemplate.opsForZSet().remove(key, values.toArray());
		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V> long zCard(RedisTemplate<K, V> redisTemplate, K key) {
		if (StringUtils.isEmpty(key)) {
			return 0;
		}

		Long ret = redisTemplate.opsForZSet().zCard(key);
		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V> long zCount(RedisTemplate<K, V> redisTemplate, K key, double min, double max) {
		Long count = redisTemplate.opsForZSet().count(key, min, max);
		return count == null ? 0 : count.longValue();
	}

	public static final <K, V> long zRank(RedisTemplate<K, V> redisTemplate, K key, V value) {
		if (StringUtils.isEmpty(key) || value == null) {
			return Long.MAX_VALUE;
		}
		Long rank = redisTemplate.opsForZSet().rank(key, value);
		return rank == null ? -1 : rank.longValue();
	}

	// Map
	public static final <K, V> long put(final RedisTemplate<K, V> redisTemplate, final K key, final Object hashKey,
			final Object value, Long timeout, TimeUnit unit) {
		if (StringUtils.isEmpty(key) || value == null) {
			return 0;
		}

		if (unit == null) {
			unit = TimeUnit.MILLISECONDS;
		}

		final Long finalTimeOut = convert(timeout, unit);

		Long ret = 1L;

		if (finalTimeOut == null || finalTimeOut.longValue() <= 0) {
			redisTemplate.opsForHash().put(key, hashKey, value);
		} else {
			RedisCallback<Long> action = new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] rawKey = serializeKey(redisTemplate, key);
					long ret = connection.hSet(rawKey, serializeHashKey(redisTemplate, hashKey),
							serializeValue(redisTemplate, value)) ? 1 : 0;
					connection.expire(rawKey, finalTimeOut);
					return ret;
				}
			};
			ret = execute(redisTemplate, action, true);
		}

		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V> long putAll(final RedisTemplate<K, V> redisTemplate, final K key,
			final Map<Object, Object> tuples, Long timeout, TimeUnit unit) {
		if (StringUtils.isEmpty(key) || tuples == null || tuples.size() <= 0) {
			return 0;
		}
		unit = unit == null ? TimeUnit.MILLISECONDS : unit;
		final Long finalTimeout = convert(timeout, unit);
		Long ret = 0L;

		RedisCallback<Long> action = new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				Map<byte[], byte[]> values = new LinkedHashMap<byte[], byte[]>();
				for (Map.Entry<Object, Object> tuple : tuples.entrySet()) {
					values.put(serializeHashKey(redisTemplate, tuple.getKey()),
							serializeHashValue(redisTemplate, tuple.getValue()));
				}
				connection.hMSet(serializeKey(redisTemplate, key), values);
				if (finalTimeout != null && finalTimeout.longValue() > 0) {
					connection.expire(serializeKey(redisTemplate, key), finalTimeout);
				}
				return (long) tuples.size();
			}
		};

		ret = execute(redisTemplate, action, true);
		return ret == null ? 0 : ret.longValue();
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> T hGet(RedisTemplate<K, V> redisTemplate, K key, Object hashKey, Class<T> clazz) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey)) {
			return null;
		}
		return (T) redisTemplate.opsForHash().get(key, hashKey);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Map<Object, T> hGetAll(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return (Map<Object, T>) redisTemplate.opsForHash().entries(key);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> List<T> hMultiGet(RedisTemplate<K, V> redisTemplate, K key,
			Collection<Object> hashKeys, Class<T> clazz) {
		if (StringUtils.isEmpty(key) || hashKeys == null || hashKeys.size() <= 0) {
			return null;
		}

		List<T> result = (List<T>) redisTemplate.opsForHash().multiGet(key, hashKeys);
//		result.removeAll(null);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> hKeys(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return (Set<T>) redisTemplate.opsForHash().keys(key);
	}

	@SuppressWarnings("unchecked")
	public static final <K, V, T> Set<T> hValues(RedisTemplate<K, V> redisTemplate, K key, Class<T> clazz) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return (Set<T>) redisTemplate.opsForHash().values(key);
	}

	public static final <K, V> boolean hContainKey(RedisTemplate<K, V> redisTemplate, K key, Object hashKey) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey)) {
			return false;
		}
		return redisTemplate.opsForHash().hasKey(key, hashKey);
	}

	public static final <K, V> int hDel(RedisTemplate<K, V> redisTemplate, K key, Object[] hashKeys) {
		if (StringUtils.isEmpty(key) || hashKeys == null || hashKeys.length <= 0) {
			return 0;
		}
		redisTemplate.opsForHash().delete(key, hashKeys);
		return hashKeys.length;
	}

	public static final <K, V> long hLen(RedisTemplate<K, V> redisTemplate, K key) {
		if (StringUtils.isEmpty(key)) {
			return 0;
		}

		Long ret = redisTemplate.opsForHash().size(key);
		return ret == null ? 0 : ret.longValue();
	}

	public static final <K, V> long incrBy(RedisTemplate<K, V> redisTemplate, K key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	public static final <K, V> long incrBy(RedisTemplate<K, V> redisTemplate, K key, long delta, Long timeout,
			TimeUnit unit) {
		if (key == null) {
			return 0L;
		}
		boolean first = true;
		if (hasKey(redisTemplate, key)) {
			first = false;
		}
		Long val = incrBy(redisTemplate, key, delta);
		if (first) {
			expire(redisTemplate, key, timeout, unit);
		}
		return val.longValue();
	}

	public static <K, V> void expire(RedisTemplate<K, V> redisTemplate, K key, Long timeout, TimeUnit unit) {
		if (unit == null) {
			unit = TimeUnit.MILLISECONDS;
		}
		redisTemplate.expire(key, timeout, unit);
	}

	// Serializer
	@SuppressWarnings("unchecked")
	private static final <K, V> byte[] serializeKey(RedisTemplate<K, V> redisTemplate, Object key) {
		RedisSerializer serializer = redisTemplate.getKeySerializer();
		serializer = serializer == null ? redisTemplate.getStringSerializer() : serializer;
		return serializer.serialize(key);
	}

	@SuppressWarnings("unchecked")
	private static final <K, V> byte[] serializeValue(RedisTemplate<K, V> redisTemplate, Object value) {
		RedisSerializer serializer = redisTemplate.getValueSerializer();
		serializer = serializer == null ? redisTemplate.getDefaultSerializer() : serializer;
		return serializer.serialize(value);
	}

	@SuppressWarnings("unchecked")
	private static final <K, V, T> T deserializeValue(RedisTemplate<K, V> redisTemplate, byte[] value, Class<T> clazz) {
		if (value == null || value.length <= 0) {
			return null;
		}
		if (clazz == String.class) {
			return (T) redisTemplate.getStringSerializer().deserialize(value);
		}
		RedisSerializer serializer = redisTemplate.getValueSerializer();
		serializer = serializer == null ? redisTemplate.getDefaultSerializer() : serializer;
		return (T) serializer.deserialize(value);
	}

	@SuppressWarnings("unchecked")
	private static final <K, V> byte[] serializeHashKey(RedisTemplate<K, V> redisTemplate, Object hashKey) {
		RedisSerializer serializer = redisTemplate.getHashKeySerializer();
		serializer = serializer == null ? redisTemplate.getDefaultSerializer() : serializer;
		return serializer.serialize(hashKey);
	}

	@SuppressWarnings("unchecked")
	private static final <K, V> byte[] serializeHashValue(RedisTemplate<K, V> redisTemplate, Object hashValue) {
		RedisSerializer serializer = redisTemplate.getHashValueSerializer();
		serializer = serializer == null ? redisTemplate.getDefaultSerializer() : serializer;
		return serializer.serialize(hashValue);
	}

	public static <K, V> boolean zContains(RedisTemplate<K, V> redisTemplate, K key, V value) {
		long rank = zRank(redisTemplate, key, value);
		if (rank == Long.MAX_VALUE || rank < 0) {
			return false;
		}
		return true;
	}

	public static <K, V> double zScore(RedisTemplate<K, V> redisTemplate, K key, V value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return Double.MIN_VALUE;
		}
		Double score = redisTemplate.opsForZSet().score(key, value);
		return score == null ? Double.MIN_VALUE : score.doubleValue();
	}

	public static <K, V> long ttl(RedisTemplate<K, V> redisTemplate, K key) {
		if (StringUtils.isEmpty(key)) {
			return -1;
		}
		Long ttl = redisTemplate.getExpire(key);
		return ttl == null ? -1 : ttl.longValue();
	}

	public static <K, V> void delFromZSets(final RedisTemplate<K, V> redisTemplate, final Collection<K> keys, final V value) {
		if (keys == null || keys.size() <= 0 || value == null) {
			return;
		}

		RedisCallback<Long> action = new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] rawValue = serializeValue(redisTemplate, value);
				for (K key : keys) {
					connection.zRem(serializeKey(redisTemplate, key), rawValue);
				}
				return null;
			}
		};
		execute(redisTemplate, action, true);
	}

	public static <K, V> void delAllFromZSets(final RedisTemplate<K, V> redisTemplate, final Collection<K> keys, final Collection<V> values) {
		if (keys == null || keys.size() <= 0 || values == null || values.size() <= 0) {
			return;
		}
		RedisCallback<Long> action = new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[][] rawValues = new byte[values.size()][];
				int i = 0;
				for (V value : values) {
					rawValues[i++] = serializeValue(redisTemplate, value);
				}
				for (K key : keys) {
					connection.zRem(serializeKey(redisTemplate, key), rawValues);
				}
				return null;
			}
		};
		execute(redisTemplate, action, true);
	}

	public static final <K, V> double hIncrBy(final RedisTemplate<K, V> redisTemplate, final K key, final Object hashKey, final double delta) {
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey)) {
			return -1;
		}
		return redisTemplate.opsForHash().increment(key, hashKey, delta);
	}
}
