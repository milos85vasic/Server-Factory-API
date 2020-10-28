package net.milosvasic.factory.api.rest.security

import net.milosvasic.factory.api.rest.exception.ResourceNotFoundException
import net.milosvasic.factory.api.rest.repository.UserRepository
import net.milosvasic.factory.api.rest.security.UserPrincipal.Companion.create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(usernameOrEmail: String): UserDetails {

        val user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow { UsernameNotFoundException("User not found with username or email : $usernameOrEmail") }
        return create(user)
    }

    @Transactional
    fun loadUserById(id: Long): UserDetails {

        val user = userRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("User", "id", id) }
        return create(user!!)
    }
}