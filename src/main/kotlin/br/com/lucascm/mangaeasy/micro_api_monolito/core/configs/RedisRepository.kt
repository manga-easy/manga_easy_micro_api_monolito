package br.com.lucascm.mangaeasy.micro_api_monolito.core.configs

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface RedisRepository<T, D> : JpaRepository<T, D> {
}