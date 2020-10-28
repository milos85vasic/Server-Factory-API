package net.milosvasic.factory.api.rest.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.ArrayList

@ConfigurationProperties(prefix = "app")
class AppProperties {

    var auth = Auth()
    var oauth2 = OAuth2()

    class Auth {

        var tokenSecret: String? = null
        var tokenExpirationMSec: Long = 0
    }

    class OAuth2 {

        var authorizedRedirectUris: List<String> = ArrayList()
            private set

        fun authorizedRedirectUris(authorizedRedirectUris: List<String>): OAuth2 {

            this.authorizedRedirectUris = authorizedRedirectUris
            return this
        }
    }
}