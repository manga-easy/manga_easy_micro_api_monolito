package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.services

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service


@Service
class CatalogService {
    @Autowired
    lateinit var catalogRepository: CatalogRepository
     fun list(genres: List<String>,
              search: String? = null,
              author: String? = null,
              page: Int? = null,
              uniqueid: String? = null,
              limit: Int? = null,
              yearAt: Int? = null,
              yearFrom: Int? = null,
              scans: String? = null,
              isAdult: Boolean,
     ): Page<CatalogEntity> {
        return catalogRepository.findAll(Specification(fun(
            root: Root<CatalogEntity>,
            _: CriteriaQuery<*>,
            builder: CriteriaBuilder
        ): Predicate? {
            var predicates = mutableListOf<Predicate>()
            if (genres.isNotEmpty()){
                genres.forEach {
                    predicates.add(builder.like(
                        builder.lower(root.get("genres")),
                        "%" + it.lowercase() + "%"
                    ))
                }
            }
            if (search != null){
                predicates.add(builder.like(
                    builder.lower(root.get("name")),
                    "%" + search.lowercase() + "%"
                ))
            }
            if (author != null){
                predicates.add(builder.like(
                    builder.lower(root.get("author")),
                    "%" + author.lowercase() + "%"
                ))
            }
            if (uniqueid != null){
                predicates.add(builder.equal(
                    builder.lower(root.get("uniqueid")),
                    uniqueid.lowercase()
                ))
            }
            if (yearAt != null && yearFrom != null){
                predicates.add(builder.between(root.get("year"), yearAt, yearFrom))
            }
            if (scans != null){
                predicates.add(builder.like(
                    builder.lower(root.get("scans")),
                    "%" + scans.lowercase() + "%"
                ))
            }
            if (!isAdult){
                predicates.add(builder.notLike(
                    builder.lower(root.get("genres")),
                    "%adult%"
                ))
            }
            return builder.and(*predicates.toTypedArray())
        }),
            PageRequest.of(page ?: 0, limit ?: 25)
        )
     }
}