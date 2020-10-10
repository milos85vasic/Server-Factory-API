package net.milosvasic.factory.api.rest.controllers

import net.milosvasic.factory.api.rest.response.MessageResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/user")
class UserController {

    @GetMapping("/home")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    fun userAccess(): ResponseEntity<*>{
        return ResponseEntity.ok<Any>(MessageResponse("Hello!"))
    }
}