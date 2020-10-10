package net.milosvasic.factory.api.rest.security.services

import net.milosvasic.factory.api.rest.model.RoleModel
import net.milosvasic.factory.api.rest.model.UserModel
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as UserDetailsImplementation
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