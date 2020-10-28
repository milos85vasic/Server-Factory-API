package net.milosvasic.factory.api.rest.controllers

import net.milosvasic.factory.api.rest.exception.AppException
import net.milosvasic.factory.api.rest.exception.BadRequestException
import net.milosvasic.factory.api.rest.model.AuthProvider
import net.milosvasic.factory.api.rest.model.Role
import net.milosvasic.factory.api.rest.model.RoleName
import net.milosvasic.factory.api.rest.model.User
import net.milosvasic.factory.api.rest.payload.ApiResponse
import net.milosvasic.factory.api.rest.payload.AuthResponse
import net.milosvasic.factory.api.rest.payload.LoginRequest
import net.milosvasic.factory.api.rest.payload.SignUpRequest
import net.milosvasic.factory.api.rest.repository.RoleRepository
import net.milosvasic.factory.api.rest.repository.UserRepository
import net.milosvasic.factory.api.rest.security.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var tokenProvider: TokenProvider

    @PostMapping("/login")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest): ResponseEntity<*> {

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail,
                        loginRequest.password
                )
        )
        SecurityContextHolder.getContext().authentication = authentication
        val token = tokenProvider.createToken(authentication)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: @Valid SignUpRequest): ResponseEntity<*> {

        if (userRepository.existsByUsername(signUpRequest.username)) {
            throw BadRequestException("Username address already in use.")
        }
        if (userRepository.existsByEmail(signUpRequest.email)) {
            throw BadRequestException("Email address already in use.")
        }
        val user = User(signUpRequest.name, signUpRequest.username, signUpRequest.email, signUpRequest.password)
        user.password = passwordEncoder.encode(user.password)
        val userRole = roleRepository.findByName(RoleName.ROLE_USER)
                ?.orElseThrow { AppException("User Role not set") }
        user.roles = Collections.singleton(userRole) as Set<Role>
        user.provider = AuthProvider.local
        val result = userRepository.save(user)
        val location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/user/me")
                .buildAndExpand(result.id).toUri()
        return ResponseEntity.created(location)
                .body(ApiResponse(true, "User registered successfully"))
    }
}