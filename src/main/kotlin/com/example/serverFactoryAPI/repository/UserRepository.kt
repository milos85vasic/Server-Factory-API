package com.example.serverFactoryAPI.repository

import com.example.serverFactoryAPI.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Repository
interface UserRepository : JpaRepository<UserModel?, Long?> {
    fun findByUsername(username: String): Optional<UserModel?>

    fun existsByUsername(username: @NotBlank @Size(max = 20, min = 3) String?):Boolean

    fun existsByEmail(email: @NotBlank @Size(max = 50) @Email String?):Boolean
}