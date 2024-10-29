package br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.services

import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.services.ProfileService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.dtos.ListStaffDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.repositories.StaffRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StaffService {
    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var staffRepository: StaffRepository
    
    fun findAll(): List<ListStaffDto> {
        val result = staffRepository.findAll()
        return result.map {
            val profile = profileService.findByUserId(it.userId)
            ListStaffDto(
                userName = profile.name,
                userImage = profile.picture,
                staff = it
            )
        }
    }
}