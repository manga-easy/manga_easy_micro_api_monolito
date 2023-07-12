package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.springframework.stereotype.Service

@Service
class GetUidByFeature {
    fun get(feature: String): String{
        return ObjectIdGenerators.UUIDGenerator().generateId(feature).toString()
    }
}