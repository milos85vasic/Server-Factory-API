package net.milosvasic.factory.api.rest.repository

import net.milosvasic.factory.api.rest.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User?, Long?> {

    fun findByUsernameOrEmail(username: String?, email: String?): Optional<User>
    fun existsByEmail(email: String?): Boolean
    fun existsByUsername(username: String?): Boolean
}