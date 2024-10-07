package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ListReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.services.ReviewService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews")
class ReviewDashController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var reviewService: ReviewService


    @GetMapping("/v1")
    fun list(
        @RequestParam page: Int?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): List<ListReviewDto> {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return reviewService.list(page ?: 0)
    }
}