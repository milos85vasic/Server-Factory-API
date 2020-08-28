package com.example.serverFactoryAPI.security.services

import com.example.serverFactoryAPI.model.UserModel
import com.example.serverFactoryAPI.repository.UserRepository
import com.example.serverFactoryAPI.security.services.UserDetailsImplementation.Companion.build
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