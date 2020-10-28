package net.milosvasic.factory.api.rest.security.oauth2

import net.milosvasic.factory.api.rest.exception.AppException
import net.milosvasic.factory.api.rest.exception.OAuth2AuthenticationProcessingException
import net.milosvasic.factory.api.rest.model.AuthProvider
import net.milosvasic.factory.api.rest.model.Role
import net.milosvasic.factory.api.rest.model.RoleName
import net.milosvasic.factory.api.rest.model.User
import net.milosvasic.factory.api.rest.repository.RoleRepository
import net.milosvasic.factory.api.rest.repository.UserRepository
import net.milosvasic.factory.api.rest.security.UserPrincipal
import net.milosvasic.factory.api.rest.security.oauth2.user.OAuth2UserInfo
import net.milosvasic.factory.api.rest.security.oauth2.user.OAuth2UserInfoFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class CustomOAuth2UserService : DefaultOAuth2UserService() {

    @Autowired
    private lateinit var  userRepository: UserRepository

    @Autowired
    private lateinit var  roleRepository: RoleRepository

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {

        val oAuth2User = super.loadUser(oAuth2UserRequest)
        return try {
            processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {

        val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }
        val userOptional: Optional<User> = userRepository.findByUsernameOrEmail(oAuth2UserInfo.getEmail(),oAuth2UserInfo.getEmail())
        var user: User
        if (userOptional.isPresent) {
            user = userOptional.get()
            if (!user.provider?.equals(AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId))!!) {
                throw OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.provider.toString() + " account. Please use your " + user.provider.toString() +
                        " account to login.")
            }
            user = updateExistingUser(user, oAuth2UserInfo)
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
        }
        return UserPrincipal.create(user, oAuth2User.attributes)
    }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): User {

        val user = User()
        user.provider = AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId)
        user.providerId = oAuth2UserInfo.getId()
        user.name = oAuth2UserInfo.getName()
        user.email = oAuth2UserInfo.getEmail()
        user.imageUrl  = oAuth2UserInfo.getImageUrl()

        val userRole = roleRepository.findByName(RoleName.ROLE_USER)
                ?.orElseThrow { AppException("User Role not set") }
        user.roles = Collections.singleton(userRole) as Set<Role>
        return userRepository.save(user)
    }

    private fun updateExistingUser(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User {

        existingUser.name = oAuth2UserInfo.getName()
        existingUser.imageUrl = oAuth2UserInfo.getImageUrl()
        return userRepository.save(existingUser)
    }
}