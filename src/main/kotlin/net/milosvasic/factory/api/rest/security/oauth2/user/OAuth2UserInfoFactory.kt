package net.milosvasic.factory.api.rest.security.oauth2.user

import net.milosvasic.factory.api.rest.exception.OAuth2AuthenticationProcessingException
import net.milosvasic.factory.api.rest.model.AuthProvider

class OAuth2UserInfoFactory {

    companion object {

        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            return when {
                registrationId.equals(AuthProvider.google.toString(), ignoreCase = true) -> {
                    GoogleOAuth2UserInfo(attributes)
                }
                registrationId.equals(AuthProvider.facebook.toString(), ignoreCase = true) -> {
                    FacebookOAuth2UserInfo(attributes)
                }
                registrationId.equals(AuthProvider.github.toString(), ignoreCase = true) -> {
                    GithubOAuth2UserInfo(attributes)
                }
                else -> {
                    throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
                }
            }
        }
    }
}