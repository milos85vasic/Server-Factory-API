package net.milosvasic.factory.api.rest.security.oauth2.user

abstract class OAuth2UserInfo(protected var attributes: Map<String, Any>) {

    abstract fun getId():String
    abstract fun getName():String
    abstract fun getEmail():String
    abstract fun getImageUrl():String
}