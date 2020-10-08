package com.example.serverFactoryAPI.util

import com.example.serverFactoryAPI.response.JwtResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post


class LogInUtils{

    companion object {
        @Throws(Exception::class)
        fun getTokenForLogin(username: String, password: String, mockMvc: MockMvc): String? {
            val mapper = ObjectMapper()
            val content = mockMvc.perform(post("/api/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\": \"$username\", \"password\": \"$password\"}"))
                    .andReturn()
                    .response
                    .contentAsString
            val authResponse: JwtResponse = mapper.readValue(content, JwtResponse::class.java)
            return authResponse.accessToken
        }
    }
}