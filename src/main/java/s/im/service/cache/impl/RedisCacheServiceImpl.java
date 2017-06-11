package s.im.service.cache.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import s.im.service.cache.api.RedisCacheService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by za-zhujun on 2017/5/15.
 */
@Component
public class RedisCacheServiceImpl implements RedisCacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    private final RedisTemplate redisTemplate;

    public RedisCacheServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean set(final String key, final String value) {
        if (StringUtils.isNotBlank(key)) {
            redisTemplate.opsForValue().set(StringUtils.trimToEmpty(key), value);
            return true;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public String get(String key) {
        if (StringUtils.isNotBlank(key)) {
            return (String) redisTemplate.opsForValue().get(StringUtils.trimToEmpty(key));
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public boolean expire(String key, long timeoutInMillSec) {
        if (StringUtils.isNotBlank(key)) {
            return redisTemplate.expire(key, timeoutInMillSec, TimeUnit.MILLISECONDS);
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <T> boolean addList(String key, T... values) {
        if (StringUtils.isNotBlank(key) && ArrayUtils.isNotEmpty(values)) {
            Long affectedCount = redisTemplate.opsForList().rightPushAll(key, values);
            return affectedCount == values.length;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        if (StringUtils.isNotBlank(key) && clazz != null) {
            List values = redisTemplate.opsForList().range(key, 0L, -1L);
            return (List<T>) values;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <T> boolean addListMember(String key, T object) {
        if (StringUtils.isNotBlank(key)) {
            Long affectedRows = redisTemplate.opsForList().rightPush(key, object);
            return affectedRows == 1;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <T> boolean addSetMember(String key, T... values) {
        if (StringUtils.isNotBlank(key)) {
            Long affectedRows = redisTemplate.opsForSet().add(key, values);
            return affectedRows == values.length;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <T> Set<T> getSetMember(String key, Class<T> clazz) {
        if (StringUtils.isNotBlank(key)) {
            Set members = redisTemplate.opsForSet().members(key);
            return (Set<T>) members;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <T> boolean removeSetMember(String key, T... values) {
        if (StringUtils.isNotBlank(key)) {
            Long remove = redisTemplate.opsForSet().remove(key, values);
            return values.length == remove;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public boolean remove(String key) {
        if (StringUtils.isNotBlank(key)) {
            redisTemplate.delete(key);
            return true;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <T> boolean isSetMember(String key, T object) {
        if (StringUtils.isNotBlank(key)) {
            return redisTemplate.opsForSet().isMember(key, object);
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <K, V> void addMap(String key, Map<K, V> map) {
        if (StringUtils.isNotBlank(key)) {
            redisTemplate.opsForHash().putAll(key, map);
            return;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <K, V> Map<K, V> getMap(K key) {
        if (key != null) {
            return redisTemplate.opsForHash().entries(key);
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <K, V> V getMapEntryValue(String mapKey, K entryKey) {
        if (StringUtils.isNotBlank(mapKey)) {
            return (V) redisTemplate.opsForHash().get(mapKey, entryKey);
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <MK, MV> void addOrUpdateMapEntry(String mapKey, MK entryKey, MV entryValue) {
        if (StringUtils.isNotBlank(mapKey) && entryKey != null) {
            redisTemplate.opsForHash().put(mapKey, entryKey, entryValue);
            return;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <MK, MV> MV deleteMapEntry(String mapKey, MK entryKey) {
        if (StringUtils.isNotBlank(mapKey) && entryKey != null) {
            MV o = (MV) redisTemplate.opsForHash().get(mapKey, entryKey);
            redisTemplate.opsForHash().delete(mapKey, entryKey);
            return o;
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <MK> Set<MK> getMapKeys(String mapKey) {
        if (StringUtils.isNotBlank(mapKey)) {
            return (Set<MK>) redisTemplate.opsForHash().keys(mapKey);
        }
        throw new RuntimeException("redis key is blank");
    }

    @Override
    public <MK> boolean existEntryKey(String mapKey, MK entryKey) {
        if (StringUtils.isNotBlank(mapKey)) {
            return redisTemplate.opsForHash().hasKey(mapKey, entryKey);
        }
        throw new RuntimeException("redis key is blank");
    }
}
