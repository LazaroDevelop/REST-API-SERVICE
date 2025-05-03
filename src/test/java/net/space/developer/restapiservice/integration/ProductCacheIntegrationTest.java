package net.space.developer.restapiservice.integration;

import lombok.extern.slf4j.Slf4j;
import net.space.developer.restapiservice.config.CacheConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Product cache integration test class
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Slf4j
@SpringBootTest
@Import(CacheConfig.class)
class ProductCacheIntegrationTest {

    /**
     * Inject the cache manager bean
     */
    @Autowired
    CacheManager cacheManager;

    /**
     * Test the caffeine cache
     */
    @Test
    @DisplayName("Should work caffeine cache")
    void shouldWorkCaffeineCache(){
        String productCache = "product";
        String productsCache = "products";

        String key ="testKey";
        String value = "testValue";

        Cache cacheOfProduct = cacheManager.getCache(productCache);
        Cache cacheOfProducts = cacheManager.getCache(productsCache);

        assert cacheOfProduct != null;
        cacheOfProduct.put(key, value);

        assert cacheOfProducts != null;
        cacheOfProducts.put(key, value);

        String cachedProductValue = cacheOfProduct.get(key, String.class);
        String cachedProductsValue = cacheOfProducts.get(key, String.class);

        assertEquals(value, cachedProductValue);
        assertEquals(value, cachedProductsValue);
    }
}
