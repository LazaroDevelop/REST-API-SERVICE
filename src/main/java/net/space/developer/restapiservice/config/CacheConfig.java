package net.space.developer.restapiservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Cache manager configuration bean
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Configuration
public class CacheConfig {

    /**
     * Cache manager configuration bean
     *
     * @return an instance of cache manager to manage the cache use in the system
     */
    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineBuilder());
        return cacheManager;
    }

    /**
     * Caffeine cache builder function
     *
     * @return an instance of caffeine cache
     */
    private Caffeine<Object, Object> caffeineBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10));
    }

}
