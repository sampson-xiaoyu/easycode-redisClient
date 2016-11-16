package com.easycode.redis.client.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface NewRedisService<K, V> {

	// base key-value
	/**
	 * 在redis中新增没有过期时间的<tt>key-value</tt>对
	 * 
	 * @param key
	 *            redis中保存的key
	 * @param value
	 *            对应的值
	 */
	void set(K key, V value);

	/**
	 * 在redis中新增有过期时间的<tt>key-value</tt>对
	 * 
	 * @param key
	 *            redis中保存的key
	 * @param value
	 *            redis中保存的value
	 * @param timeout
	 *            key的过期是时间
	 * @param unit
	 *            时间的单位，参照TimeUnit
	 */
	void set(K key, V value, Long timeout, TimeUnit unit);

	/**
	 * 在redis中，同时set多个不含过期时间的值
	 * 
	 * @param tuples
	 *            需要set到redis中的key-value对
	 */
	void multiSet(Map<K, V> tuples);

	/**
	 * 在redis中，同时set多个含过期时间的值
	 * 
	 * @param tuples
	 *            需要set到redis中的key-value对
	 * @param timeout
	 *            过期时间
	 * @param unit
	 *            时间单位
	 */
	void multiSet(Map<K, V> tuples, Long timeout, TimeUnit unit);

	/**
	 * 获取一个String类型的值
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>null</tt>
	 */
	String getString(K key);

	/**
	 * 获取一个Byte对象
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>null</tt>
	 */
	Byte getByte(K key);

	/**
	 * 获取一个byte值
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>0</tt>
	 */
	byte getByteValue(K key);

	/**
	 * 获取一个Integer对象
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>null</tt>
	 */
	Integer getInteger(K key);

	/**
	 * 获取一个int值
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>0</tt>
	 */
	int getIntegerValue(K key);

	/**
	 * 获取一个Long对象
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>null</tt>
	 */
	Long getLong(K key);

	/**
	 * 获取一个long值
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>0</tt>
	 */
	long getLongValue(K key);

	/**
	 * 获取一个Double对象
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>null</tt>
	 */
	Double getDouble(K key);

	/**
	 * 获取一个double值
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>0</tt>
	 */
	double getDoubleValue(K key);

	/**
	 * 获取一个BigDecimal对象
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>null</tt>
	 */
	BigDecimal getBigDecimal(K key);

	/**
	 * 获取一个BigDecimal对象对应的double值
	 * 
	 * @param key
	 *            redis中保存的key
	 * @return key在redis中对应的value，不存在返回<tt>0</tt>
	 */
	double getBigDecimalValue(K key);

	/**
	 * 获取一个clazz类型的对象
	 * 
	 * @param key
	 *            redis中的key
	 * @param clazz
	 *            需要转化的对象类型
	 * @return key在redis中对应的value，不存在返回<tt>null</tt>
	 */
	<T> T getObj(K key, Class<T> clazz);

	/**
	 * 获取多个String值
	 * 
	 * @param keys
	 *            redis中key的集合
	 * @return keys在redis中对应的String对象值，会过滤null
	 */
	Map<K, String> multiGetString(Collection<? extends K> keys);

	/**
	 * 获取多个对象
	 * 
	 * @param keys
	 *            redis中key的集合
	 * @param clazz
	 *            需要转化的对象类型
	 * @return keys在redis中对应的对象值，会过滤null
	 */
	<T> Map<K, T> multiGetObject(Collection<? extends K> keys, Class<T> clazz);

	/**
	 * 删除redis中的某个key
	 * 
	 * @param key
	 *            需要删除的key
	 */
	void del(K key);

	/**
	 * 批量删除Redis中的某些key
	 * 
	 * @param keys
	 *            需要删除的key的集合
	 */
	void delAll(Collection<K> keys);

	/**
	 * 判断redis中是否包含某个key
	 * 
	 * @param key
	 *            需要判断的key
	 * @return true：存在；false：不存在
	 */
	boolean hasKey(K key);

	// list

	/**
	 * 向redis列表中插入单个元素，每次均在列表头追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param value
	 *            需要插入的元素值
	 */
	void leftPush(K key, V value);

	/**
	 * 向redis列表中插入单个元素，每次均在列表头追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param value
	 *            需要插入的元素值
	 * @param timeout
	 *            列表的过期时间
	 * @param unit
	 *            过期时间的时间单位
	 */
	void leftPush(K key, V value, Long timeout, TimeUnit unit);

	/**
	 * 向redis列表中插入多个元素，每次均在列表头追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param values
	 *            需要插入的元素列表
	 */
	void leftPushAll(K key, Collection<? extends V> values);

	/**
	 * 向redis列表中插入多个元素，每次均在列表头追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param values
	 *            需要插入的元素列表
	 * @param timeout
	 *            链表的过期时间
	 * @param unit
	 *            过期时间单位
	 */
	void leftPushAll(K key, Collection<? extends V> values, Long timeout, TimeUnit unit);

	/**
	 * 向redis列表中插入单个元素，每次均在列表尾部追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param value
	 *            需要插入的元素值
	 */
	void rightPush(K key, V value);

	/**
	 * 向redis列表中插入单个元素，每次均在列表尾部追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param value
	 *            需要插入的元素值
	 * @param timeout
	 *            列表过期时间
	 * @param unit
	 *            过期时间单位
	 */
	void rightPush(K key, V value, Long timeout, TimeUnit unit);

	/**
	 * 向redis列表中插入多个元素，每次均在列表尾部追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param values
	 *            需要插入的元素列表
	 */
	void rightPushAll(K key, Collection<? extends V> values);

	/**
	 * 向redis列表中插入多个元素，每次均在列表尾部追加
	 * 
	 * @param key
	 *            列表对应的rediskey
	 * @param values
	 *            需要插入的元素列表
	 * @param timeout
	 *            列表过期时间
	 * @param unit
	 *            过期时间单位
	 */
	void rightPushAll(K key, Collection<? extends V> values, Long timeout, TimeUnit unit);

	/**
	 * 从队列头pop出单个Byte对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列头pop出的Byte对象，如果列表为空，则返回null
	 */
	Byte leftPopByte(K key);

	/**
	 * 从队列头pop出多个Byte对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列头pop出的Byte对象列表，如果列表为空，则返回null
	 */
	List<Byte> leftPopBytes(K key, int num);

	/**
	 * 从队列头pop出单个Integer对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列头pop出的Integer对象，如果列表为空，则返回null
	 */
	Integer leftPopInteger(K key);

	/**
	 * 从队列头pop出多个Integer对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列头pop出的Integer对象列表，如果列表为空，则返回null
	 */
	List<Integer> leftPopIntegers(K key, int num);

	/**
	 * 从队列头pop出单个Long对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列头pop出的Long对象，如果列表为空，则返回null
	 */
	Long leftPopLong(K key);

	/**
	 * 从队列头pop出多个Long对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列头pop出的Long对象列表，如果列表为空，则返回null
	 */
	List<Long> leftPopLongs(K key, int num);

	/**
	 * 从队列头pop出单个Double对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列头pop出的Double对象，如果列表为空，则返回null
	 */
	Double leftPopDouble(K key);

	/**
	 * 从队列头pop出多个Double对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列头pop出的Double对象列表，如果列表为空，则返回null
	 */
	List<Double> leftPopDoubles(K key, int num);

	/**
	 * 从队列头pop出单个BigDecimal对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列头pop出的BigDecimal对象，如果列表为空，则返回null
	 */
	BigDecimal leftPopBigDecimal(K key);

	/**
	 * 从队列头pop出多个BigDecimal对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列头pop出的BigDecimal对象列表，如果列表为空，则返回null
	 */
	List<BigDecimal> leftPopBigDecimals(K key, int num);

	/**
	 * 从队列头pop出单个对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param clazz
	 *            需要转化的对象类型
	 * @return 从队列头pop出的对象
	 */
	<T> T leftPopObject(K key, Class<T> clazz);

	/**
	 * 从队列头pop出多个对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param clazz
	 *            需要转化的对象类型
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列头pop出的对象列表
	 */
	<T> List<T> leftPopObjects(K key, Class<T> clazz, int num);

	/**
	 * 从队列尾pop出单个Byte对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列尾pop出的Byte对象，如果列表为空，则返回null
	 */
	Byte rightPopByte(K key);

	/**
	 * 从队列尾pop出多个Byte对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列尾pop出的Byte对象列表，如果列表为空，则返回null
	 */
	List<Byte> rightPopBytes(K key, int num);

	/**
	 * 从队列尾pop出单个Integer对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列尾pop出的Integer对象，如果列表为空，则返回null
	 */
	Integer rightPopInteger(K key);

	/**
	 * 从队列尾pop出多个Integer对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列尾pop出的Integer对象列表，如果列表为空，则返回null
	 */
	List<Integer> rightPopIntegers(K key, int num);

	/**
	 * 从队列尾pop出单个Long对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列尾pop出的Long对象，如果列表为空，则返回null
	 */
	Long rightPopLong(K key);

	/**
	 * 从队列尾pop出多个Long对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列尾pop出的Long对象列表，如果列表为空，则返回null
	 */
	List<Long> rightPopLongs(K key, int num);

	/**
	 * 从队列尾pop出单个Double对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列尾pop出的Double对象，如果列表为空，则返回null
	 */
	Double rightPopDouble(K key);

	/**
	 * 从队列尾pop出多个Double对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列尾pop出的Double对象列表，如果列表为空，则返回null
	 */
	List<Double> rightPopDoubles(K key, int num);

	/**
	 * 从队列尾pop出单个BigDecimal对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 从队列尾pop出的BigDecimal对象，如果列表为空，则返回null
	 */
	BigDecimal rightPopBigDecimal(K key);

	/**
	 * 从队列尾pop出多个BigDecimal对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列尾pop出的BigDecimal对象列表，如果列表为空，则返回null
	 */
	List<BigDecimal> rightPopBigDecimals(K key, int num);

	/**
	 * 从队列尾pop出单个对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param clazz
	 *            需要转化的对象类型
	 * @return 从队列尾pop出的对象
	 */
	<T> T rightPopObject(K key, Class<T> clazz);

	/**
	 * 从队列尾pop出多个对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param clazz
	 *            需要转化的对象类型
	 * @param num
	 *            需要pop出的元素个数
	 * @return 从队列尾pop出的对象列表
	 */
	<T> List<T> rightPopObjects(K key, Class<T> clazz, int num);

	/**
	 * 获取列表中第index个元素，并转化为Byte对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的String对象，若不存在，返回null
	 */
	String getStringElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为Byte对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的Byte对象，若不存在，返回null
	 */
	Byte getByteElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为byte值
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的byte值，若不存在，返回0
	 */
	byte getByteValueElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为Integer对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的Integer对象，若不存在，返回null
	 */
	Integer getIntegerElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为int值
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的int值，若不存在，返回0
	 */
	int getIntegerValueElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为Long对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的Long对象，若不存在，返回null
	 */
	Long getLongElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为long值
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的long值，若不存在，返回0
	 */
	long getLongValueElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为Double对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的Double对象，若不存在，返回null
	 */
	Double getDoubleElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为double值
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的double值，若不存在，返回0
	 */
	double getDoubleValueElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为BigDecimal对象
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的BigDecimal对象，若不存在，返回null
	 */
	BigDecimal getBigDecimalElementAt(K key, long index);

	/**
	 * 获取列表中第index个元素，并转化为double值
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param index
	 *            元素坐标
	 * @return 取出的double值，若不存在，返回0
	 */
	double getBigDecimalValueElementAt(K key, long index);

	/**
	 * 删除列表中的元素
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @param value
	 *            需要删除的元素
	 * @return 删除的元素个数
	 */
	long delFromList(K key, V value);

	/**
	 * 获取列表长度
	 * 
	 * @param key
	 *            列表对应的redis-key
	 * @return 列表长度，若不存在，返回0
	 */
	long lengthOfList(K key);

	// Set
	/**
	 * 新增一个元素到set中
	 * 
	 * @param key
	 *            redis中set对应的key
	 * @param value
	 *            需要放入的值（value）
	 */
	void sAdd(K key, V value);

	/**
	 * 新增一个元素到set中
	 * 
	 * @param key
	 *            redis中set对应的key
	 * @param value
	 *            需要放入的值（value）
	 * @param timeout
	 *            set的过期时间
	 * @param unit
	 *            set的过期单位
	 */
	void sAdd(K key, V value, Long timeout, TimeUnit unit);

	/**
	 * 批量添加元素到set中
	 * 
	 * @param key
	 *            redis中set对应的key
	 * @param values
	 *            需要放入set的值的集合
	 */
	void sAddAll(K key, Collection<? extends V> values);

	/**
	 * 批量添加元素到set中
	 * 
	 * @param key
	 *            redis中set对应的key
	 * @param values
	 *            需要放入set的值的集合
	 * @param timeout
	 *            set的过期时间
	 * @param unit
	 *            set的过期时间单位
	 */
	void sAddAll(K key, Collection<? extends V> values, Long timeout, TimeUnit unit);

	Byte randomByte(K key);

	byte randomByteValue(K key);

	Integer randomInteger(K key);

	int randomIntegerValue(K key);

	Long randomLong(K key);

	long randomLongValue(K key);

	Double randomDouble(K key);

	double randomDoubleValue(K key);

	BigDecimal randomBigDecimal(K key);

	double randomBigDecimalValue(K key);

	<T> T randomObject(K key, Class<T> clazz);

	List<Byte> randomBytes(K key, int num);

	List<Integer> randomIntegers(K key, int num);

	List<Long> randomLongs(K key, int num);

	List<Double> randomDoubles(K key, int num);

	List<BigDecimal> randomBigDecimals(K key, int num);

	<T> List<T> randomObjects(K key, int num, Class<T> clazz);

	List<Byte> randomDistinctBytes(K key, int num);

	List<Integer> randomDistinctIntegers(K key, int num);

	List<Long> randomDistinctLongs(K key, int num);

	List<Double> randomDistinctDoubles(K key, int num);

	List<BigDecimal> randomDistinctBigDecimals(K key, int num);

	<T> List<T> randomDistinctObjects(K key, int num, Class<T> clazz);

	Byte randomPopByte(K key);

	byte randomPopByteValue(K key);

	Integer randomPopInteger(K key);

	int randomPopIntegerValue(K key);

	Long randomPopLong(K key);

	long randomPopLongValue(K key);

	Double randomPopDouble(K key);

	double randomPopDoubleValue(K key);

	BigDecimal randomPopBigDecimal(K key);

	double randomPopBigDecimalValue(K key);

	<T> T randomPopObject(K key, Class<T> clazz);

	List<Byte> randomPopBytes(K key, int num);

	List<Integer> randomPopIntegers(K key, int num);

	List<Long> randomPopLongs(K key, int num);

	List<Double> randomPopDoubles(K key, int num);

	List<BigDecimal> randomPopBigDecimals(K key, int num);

	<T> List<T> randomPopObjects(K key, int num, Class<T> clazz);

	long sizeOfSet(K key);

	long sRemove(K key,  V[] value);

	boolean sIsMember(K key, V value);

	// Zset
	void zAdd(K key, V value, double score);

	void zAdd(K key, V value, double score, Long timeout, TimeUnit unit);

	void zAddAll(K key, Map<V, Double> tuples);

	void zAddAll(K key, Map<V, Double> tuples, Long timeout, TimeUnit unit);

	void addToZSets(Collection<K> keys, V value, double score);
	
	void addToZSets(Collection<K> keys, V value, double score, Long timeout, TimeUnit unit);
	
	void addAllToZSets(Collection<K> keys, Map<V, Double> tuples);
	
	void addAllToZSets(Collection<K> keys, Map<V, Double> tuples, Long timeout, TimeUnit unit);
	
	long delFromZSet(K key, V value);

	long delAllFromZSet(K key, Collection<? extends V> values);

	List<Byte> zRangeBytes(K key, long start, long end);

	List<Integer> zRangeIntegers(K key, long start, long end);

	List<Long> zRangeLongs(K key, long start, long end);

	List<Double> zRangeDoubles(K key, long start, long end);

	List<BigDecimal> zRangeBigDecimals(K key, long start, long end);

	<T> List<T> zRangeObjects(K key, long start, long end, Class<T> clazz);

	Map<Byte, Double> zRangeBytesWithScore(K key, long start, long end);

	Map<Integer, Double> zRangeIntegersWithScore(K key, long start, long end);

	Map<Long, Double> zRangeLongsWithScore(K key, long start, long end);

	Map<Double, Double> zRangeDoublesWithScore(K key, long start, long end);

	Map<BigDecimal, Double> zRangeBigDecimalsWithScore(K key, long start, long end);

	<T> Map<T, Double> zRangeObjectsWithScore(K key, long start, long end, Class<T> clazz);

	List<Byte> zRevRangeBytes(K key, long start, long end);

	List<Integer> zRevRangeIntegers(K key, long start, long end);

	List<Long> zRevRangeLongs(K key, long start, long end);

	List<Double> zRevRangeDoubles(K key, long start, long end);

	List<BigDecimal> zRevRangeBigDecimals(K key, long start, long end);

	<T> List<T> zRevRangeObjects(K key, long start, long end, Class<T> clazz);

	Map<Byte, Double> zRevRangeBytesWithScore(K key, long start, long end);

	Map<Integer, Double> zRevRangeIntegersWithScore(K key, long start, long end);

	Map<Long, Double> zRevRangeLongsWithScore(K key, long start, long end);

	Map<Double, Double> zRevRangeDoublesWithScore(K key, long start, long end);

	Map<BigDecimal, Double> zRevRangeBigDecimalsWithScore(K key, long start, long end);

	<T> Map<T, Double> zRevRangeObjectsWithScore(K key, long start, long end, Class<T> clazz);

	List<Byte> zRangeBytesByScore(K key, double min, double max, long offset, long limit);

	List<Integer> zRangeIntegersByScore(K key, double min, double max, long offset, long limit);

	List<Long> zRangeLongByScore(K key, double min, double max, long offset, long limit);

	List<Double> zRangeDoublesByScore(K key, double min, double max, long offset, long limit);

	List<BigDecimal> zRangeBigDecimalsByScore(K key, double min, double max, long offset, long limit);

	<T> List<T> zRangeObjectsByScore(K key, double min, double max, long offset, long limit, Class<T> clazz);

	List<Byte> zRevRangeBytesByScore(K key, double min, double max, long offset, long limit);

	List<Integer> zRevRangeIntegersByScore(K key, double min, double max, long offset, long limit);

	List<Long> zRevRangeLongByScore(K key, double min, double max, long offset, long limit);

	List<Double> zRevRangeDoublesByScore(K key, double min, double max, long offset, long limit);

	List<BigDecimal> zRevRangeBigDecimalsByScore(K key, double min, double max, long offset, long limit);

	<T> List<T> zRevRangeObjectsByScore(K key, double min, double max, long offset, long limit, Class<T> clazz);

	Map<Byte, Double> zRevRangeBytesByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<Integer, Double> zRevRangeIntegersByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<Long, Double> zRevRangeLongsByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<Double, Double> zRevRangeDoublesByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<BigDecimal, Double> zRevRangeBigDecimalsByScoreWithScore(K key, double min, double max, long offset,
			long limit);

	<T> Map<T, Double> zRevRangeObjectsByScoreWithScore(K key, double min, double max, long offset, long limit,
			Class<T> clazz);

	Map<Byte, Double> zRangeBytesByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<Integer, Double> zRangeIntegersByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<Long, Double> zRangeLongsByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<Double, Double> zRangeDoublesByScoreWithScore(K key, double min, double max, long offset, long limit);

	Map<BigDecimal, Double> zRangeBigDecimalsByScoreWithScore(K key, double min, double max, long offset, long limit);

	<T> Map<T, Double> zRangeObjectsByScoreWithScore(K key, double min, double max, long offset, long limit,
			Class<T> clazz);

	double increaseScoreInZset(K key, V value, double delta);

	long sizeOfZSet(K key);

	long zRank(K key, V value);

	boolean zContains(K key, V value);

	long zCountLargerThan(K key, double min);

	long zCountSmallerThan(K key, double max);

	double zScore(K key, V value);

	// Hash
	long put(K key, Object hashKey, Object value);

	long put(K key, Object hashKey, Object value, Long timeout, TimeUnit unit);

	long putAll(K key, Map<Object, Object> value);

	long putAll(K key, Map<Object, Object> value, Long timeout, TimeUnit unit);

	Byte hGetByte(K key, Object hashKey);

	Integer hGetInteger(K key, Object hashKey);

	Long hGetLong(K key, Object hashKey);

	Double hGetDouble(K key, Object hashKey);

	BigDecimal hGetBigDecimal(K key, Object hashKey);

	<T> T hGetObject(K key, Object hashKey, Class<T> clazz);

	<T> List<T> hMultiGetObjects(K key, Collection<Object> hashKeys, Class<T> clazz);

	<T> Map<Object, T> hGetAll(K key, Class<T> clazz);

	boolean hContainKey(K key, Object hashKey);

	double hIncrBy(K key, Object hashKey, double delta);

	int delFromHash(K key, Object hashKey);

	int delAllFromHash(K key, Object[] hashKeys);

	long sizeOfHash(K key);

	boolean lock(K key);

	void unlock(K key);
	
	long incr(K key);
	
	long incrBy(K key, long delta);
	
	void setTimeOut(K key, long expireTime, TimeUnit unit);
	
	long ttl(K key);

	void delFromZsets(Collection<K> keys, V value);

	void delAllFromZsets(Collection<K> keys, Collection<V> values);

	/**
	 * 根据坐标获取Byte列表中的元素
	 * @param key		redis中对应的key
	 * @param start		获取元素的起始坐标，从0开始
	 * @param end		获取元素的终止坐标，若要取全部元素，则设为－1
	 * @return			对应的元素列表
	 */
	List<Byte> lRangeBytes(K key, long start, long end);

	/**
	 * 根据坐标获取Integer列表中的元素
	 * @param key		redis中对应的key
	 * @param start		获取元素的起始坐标，从0开始
	 * @param end		获取元素的终止坐标，若要取全部元素，则设为－1
	 * @return			对应的元素列表
	 */
	List<Integer> lRangeIntegers(K key, long start, long end);

	/**
	 * 根据坐标获取Long列表中的元素
	 * @param key		redis中对应的key
	 * @param start		获取元素的起始坐标，从0开始
	 * @param end		获取元素的终止坐标，若要取全部元素，则设为－1
	 * @return			对应的元素列表
	 */
	List<Long> lRangeLongs(K key, long start, long end);

	/**
	 * 根据坐标获取Double列表中的元素
	 * @param key		redis中对应的key
	 * @param start		获取元素的起始坐标，从0开始
	 * @param end		获取元素的终止坐标，若要取全部元素，则设为－1
	 * @return			对应的元素列表
	 */
	List<Double> lRangeDoubles(K key, long start, long end);

	/**
	 * 根据坐标获取BigDecimal列表中的元素
	 * @param key		redis中对应的key
	 * @param start		获取元素的起始坐标，从0开始
	 * @param end		获取元素的终止坐标，若要取全部元素，则设为－1
	 * @return			对应的元素列表
	 */
	List<BigDecimal> lRangeBigDecimals(K key, long start, long end);

	/**
	 *根据坐标获取Object列表中的元素
	 * @param key		redis中对应的key
	 * @param start		获取元素的起始坐标，从0开始
	 * @param end		获取元素的终止坐标，若要取全部元素，则设为－1
	 * @param clazz		获取的object类型的具体类型
	 * @return			对应的元素列表
	 */
	<T> List<T> lRangeObjects(K key, long start, long end, Class<T> clazz);
}
