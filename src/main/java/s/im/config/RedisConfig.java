package s.im.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by za-zhujun on 2017/5/15.
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig {
//    @Bean
//    @ConfigurationProperties(prefix="spring.redis")
//    public JedisPoolConfig getRedisConfig(){
//        JedisPoolConfig config = new JedisPoolConfig();
//        return config;
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix="spring.redis")
//    public JedisConnectionFactory getConnectionFactory(){
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        JedisPoolConfig config = getRedisConfig();
//        factory.setPoolConfig(config);
//        return factory;
//    }

    @Bean
    @Autowired
    public RedisTemplate getRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
