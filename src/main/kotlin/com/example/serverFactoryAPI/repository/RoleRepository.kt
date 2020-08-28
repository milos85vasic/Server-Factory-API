package com.example.serverFactoryAPI.repository

import com.example.serverFactoryAPI.model.ERole
import com.example.serverFactoryAPI.model.RoleModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<RoleModel?, Long?> {
    fun findByName(roleName: ERole?): Optional<RoleModel?>?
}