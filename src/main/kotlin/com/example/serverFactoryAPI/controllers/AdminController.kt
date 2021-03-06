package com.example.serverFactoryAPI.controllers

import com.example.serverFactoryAPI.response.MessageResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
class AdminController {

    @GetMapping("/home")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccess():ResponseEntity<*>{
        return ResponseEntity.ok<Any>(MessageResponse("Hello admin!"));
    }
}