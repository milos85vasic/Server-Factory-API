package net.milosvasic.factory.api.rest.controllers

import net.milosvasic.factory.api.rest.exception.ResourceNotFoundException
import net.milosvasic.factory.api.rest.model.User
import net.milosvasic.factory.api.rest.repository.UserRepository
import net.milosvasic.factory.api.rest.security.CurrentUser
import net.milosvasic.factory.api.rest.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User? {
        return userPrincipal.id?.let {
            userRepository.findById(it)
                .orElseThrow { ResourceNotFoundException("User", "id", userPrincipal.id) }
        }
    }
}