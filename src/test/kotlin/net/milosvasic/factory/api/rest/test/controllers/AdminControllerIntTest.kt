package net.milosvasic.factory.api.rest.test.controllers

import org.junit.jupiter.api.Test
import net.milosvasic.factory.api.rest.test.util.LogInUtils
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerIntTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun getAdminProtectedGreetingForUser() {
        val token: String? = LogInUtils.getTokenForLogin("User1","User1234", mockMvc)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/home")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token"))
                .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun getAdminProtectedGreetingForAdmin() {
        val token: String? = LogInUtils.getTokenForLogin("Admin","Admin1234", mockMvc)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/home")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getAdminProtectedGreetingForAnyone() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/home")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}