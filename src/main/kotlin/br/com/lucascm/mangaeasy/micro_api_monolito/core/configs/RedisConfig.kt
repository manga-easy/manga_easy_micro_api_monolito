package br.com.lucascm.mangaeasy.micro_api_monolito.core.configs

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.time.Duration


@Configuration
class RedisConfig {
    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val cacheConfigurations = mapOf(
            RedisCacheName.LIST_CATALOG
                    to config(Duration.ofHours(12)),
            RedisCacheName.LIST_REVIEW
                    to config(Duration.ofHours(2)),
            RedisCacheName.GET_MANGA_WEEKLY
                    to config(Duration.ofDays(7)),
            RedisCacheName.RECOMMENDATIONS_ANILIST
                    to config(Duration.ofDays(30)),
            RedisCacheName.RECOMMENDATIONS
                    to config(Duration.ofDays(1))
        )
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config(Duration.ofHours(2)))
            .withInitialCacheConfigurations(cacheConfigurations)
            .build()
    }

    private fun config(duration: Duration): RedisCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(duration)
            .disableCachingNullValues()
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))

}