package com.example.serverFactoryAPI.security.services

import com.example.serverFactoryAPI.model.RoleModel
import com.example.serverFactoryAPI.model.UserModel
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class UserDetailsImplementation(val id: Long?, private val username: String, val email: String?, @field:JsonIgnore private val password: String,
                                private val authorities: Collection<GrantedAuthority>) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as UserDetailsImplementation
        return id == user.id
    }

    companion object {
        private const val serialVersionUID = 1L
        fun build(user: UserModel): UserDetailsImplementation {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                    .map { role: RoleModel -> SimpleGrantedAuthority(role.name!!.name) }
                    .collect(Collectors.toList())
            return UserDetailsImplementation(
                    user.id,
                    user.username!!,
                    user.email,
                    user.password!!,
                    authorities)
        }
    }

}