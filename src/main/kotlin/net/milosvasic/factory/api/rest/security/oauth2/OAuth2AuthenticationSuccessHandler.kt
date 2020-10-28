package net.milosvasic.factory.api.rest.security.oauth2

import net.milosvasic.factory.api.rest.config.AppProperties
import net.milosvasic.factory.api.rest.exception.BadRequestException
import net.milosvasic.factory.api.rest.security.TokenProvider
import net.milosvasic.factory.api.rest.util.CookieUtils.Companion.getCookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler
    : SimpleUrlAuthenticationSuccessHandler() {

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    @Autowired
    private lateinit var appProperties: AppProperties

    @Autowired
    private lateinit var httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         authentication: Authentication) {

        val targetUrl = determineTargetUrl(request, response, authentication)
        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(request: HttpServletRequest,
                                    response: HttpServletResponse,
                                    authentication: Authentication): String {

        val redirectUri = getCookie(request,
                HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map { obj: Cookie -> obj.value }
        if (redirectUri.isPresent && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
        }
        val targetUrl = redirectUri.orElse(defaultTargetUrl)
        val token = tokenProvider.createToken(authentication)
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {

        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {

        val clientRedirectUri = URI.create(uri)
        return appProperties.oauth2.authorizedRedirectUris
                .stream()
                .anyMatch { authorizedRedirectUri: String ->
                    val authorizedURI = URI.create(authorizedRedirectUri)
                    if (authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true)
                            && authorizedURI.port == clientRedirectUri.port) {
                        return@anyMatch true
                    }
                    false
                }
    }
}