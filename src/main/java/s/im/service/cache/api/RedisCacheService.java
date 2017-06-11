package s.im.service.cache.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by za-zhujun on 2017/5/16.
 */
public interface RedisCacheService extends CacheService {
    boolean expire(String key, long timeoutInMillSec);

    <T> boolean addList(String key, T... values);

    <T> List<T> getList(String key, Class<T> clazz);

    <T> boolean addListMember(String key, T object);

    <T> boolean addSetMember(String key, T... values);

    <T> Set<T> getSetMember(String key, Class<T> clazz);

    <T> boolean removeSetMember(String key, T... values);

    <T> boolean isSetMember(String key, T object);

    <MK, MV> void addMap(String key, Map<MK, MV> map);

    <MK, MV> Map<MK, MV> getMap(MK key);

    <MK, MV> MV getMapEntryValue(String mapKey, MK entryKey);

    <MK, MV> void addOrUpdateMapEntry(String mapKey, MK entryKey, MV entryValue);

    <MK, MV> MV deleteMapEntry(String mapKey, MK entryKey);

    <MK> Set<MK> getMapKeys(String mapKey);

    <MK> boolean existEntryKey(String mapKey, MK entryKey);
}
