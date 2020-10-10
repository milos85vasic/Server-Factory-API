package net.milosvasic.factory.api.rest.test.controllers

import net.milosvasic.factory.api.rest.test.util.LogInUtils
import org.junit.Test
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
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @org.junit.Before
    fun setUp() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun getAuthorizedProtectedGreetingForUser() {
        val token: String? = LogInUtils.getTokenForLogin("User1","User1234", mockMvc)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/home")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getAuthorizedProtectedGreetingForAnyone() {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/home")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}