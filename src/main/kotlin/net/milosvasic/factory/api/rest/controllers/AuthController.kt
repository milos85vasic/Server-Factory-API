package net.milosvasic.factory.api.rest.controllers

import net.milosvasic.factory.api.rest.model.ERole
import net.milosvasic.factory.api.rest.model.RoleModel
import net.milosvasic.factory.api.rest.model.UserModel
import net.milosvasic.factory.api.rest.repository.RoleRepository
import net.milosvasic.factory.api.rest.repository.UserRepository
import net.milosvasic.factory.api.rest.request.LoginRequest
import net.milosvasic.factory.api.rest.request.SignupRequest
import net.milosvasic.factory.api.rest.response.JwtResponse
import net.milosvasic.factory.api.rest.response.MessageResponse
import net.milosvasic.factory.api.rest.security.jwt.JwtUtils
import net.milosvasic.factory.api.rest.security.services.UserDetailsImplementation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
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
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest): ResponseEntity<*> {

        val authentication: Authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails: UserDetailsImplementation = authentication.principal as UserDetailsImplementation
        val roles: MutableList<String>? = userDetails.authorities.stream()
                .map { item -> item.authority }
                .collect(Collectors.toList())
        return ResponseEntity.ok<Any>(JwtResponse(jwt,
                userDetails.id,
                userDetails.username,
                userDetails.email,
                roles))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity
                    .badRequest()
                    .body<Any>(MessageResponse("Error: Username is already taken!"))
        }
        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity
                    .badRequest()
                    .body<Any>(MessageResponse("Error: Email is already in use!"))
        }

        val user = UserModel(signUpRequest.username,
                signUpRequest.email,
                encoder.encode(signUpRequest.password))
        val strRoles: Set<String>? = signUpRequest.role
        val roles: HashSet<RoleModel> = HashSet<RoleModel>()
        println()
        if (strRoles == null) {
            // FIXME:
            val userRole: RoleModel = roleRepository.findByName(ERole.ROLE_USER)!!
                    .orElseThrow { java.lang.RuntimeException("") }!!

        } else {
            strRoles.forEach(Consumer { role: String? ->
                when (role) {
                    "admin" -> {
                        val adminRole: RoleModel = roleRepository.findByName(ERole.ROLE_ADMIN)
                                ?.orElseThrow { RuntimeException("Error: Role is not found.") }!!
                        roles.add(adminRole)
                    }

                    "pm" -> {
                        val pmRole: RoleModel = roleRepository.findByName(ERole.ROLE_PM)
                                ?.orElseThrow { RuntimeException("Error: Role is not found.") }!!
                        roles.add(pmRole)
                    }
                    else -> {
                        val userRole: RoleModel = roleRepository.findByName(ERole.ROLE_USER)
                                ?.orElseThrow { RuntimeException("Error: Role is not found.") }!!
                        roles.add(userRole)
                    }
                }
            })
        }
        user.roles = roles
        userRepository.save<UserModel>(user)
        return ResponseEntity.ok<Any>(MessageResponse("User registered successfully!"))
    }
}


