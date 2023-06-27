package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import com.fasterxml.jackson.annotation.ObjectIdGenerators


 class GetUidByFeature {
    fun get(feature: String): String{
        return ObjectIdGenerators.UUIDGenerator().generateId(feature).toString()
    }
}