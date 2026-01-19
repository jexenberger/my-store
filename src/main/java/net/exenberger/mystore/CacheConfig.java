package net.exenberger.mystore;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        var simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(
                new ConcurrentMapCache("address"),
                new ConcurrentMapCache("latLong"))
        );
        return simpleCacheManager;
    }

}
