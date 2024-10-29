package br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.entities.StaffEntity

data class ListStaffDto(
    val userName: String?,
    val userImage: String?,
    val staff: StaffEntity
)