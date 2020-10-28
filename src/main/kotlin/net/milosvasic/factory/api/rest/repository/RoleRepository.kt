package net.milosvasic.factory.api.rest.repository

import net.milosvasic.factory.api.rest.model.Role
import net.milosvasic.factory.api.rest.model.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role?, Long?> {

    fun findByName(roleName: RoleName?): Optional<Role?>?
}