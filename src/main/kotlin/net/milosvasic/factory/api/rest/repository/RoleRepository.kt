package net.milosvasic.factory.api.rest.repository

import net.milosvasic.factory.api.rest.model.ERole
import net.milosvasic.factory.api.rest.model.RoleModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<RoleModel?, Long?> {
    fun findByName(roleName: ERole?): Optional<RoleModel?>?
}