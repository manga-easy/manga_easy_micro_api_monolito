package br.com.lucascm.mangaeasy.micro_api_monolito.core.configs

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
@Deprecated("Use o @Cacheable")
interface RedisRepository<T, D> : JpaRepository<T, D> {
}