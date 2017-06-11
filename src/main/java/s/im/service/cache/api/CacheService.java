package s.im.service.cache.api;

/**
 * Created by za-zhujun on 2017/5/15.
 */
public interface CacheService {

    boolean set(String key, String value);

    String get(String key);

    boolean remove(String key);
}
