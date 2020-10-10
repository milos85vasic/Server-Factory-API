package net.milosvasic.factory.api.rest.security.services

import net.milosvasic.factory.api.rest.model.UserModel
import net.milosvasic.factory.api.rest.repository.UserRepository
import net.milosvasic.factory.api.rest.security.services.UserDetailsImplementation.Companion.build
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier


@Service
class UserDetailsServiceImplementation : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserModel = userRepository.findByUsername(username)
                .orElseThrow(Supplier { UsernameNotFoundException("User Not Found with username: $username") })!!
        return build(user)
    }
}