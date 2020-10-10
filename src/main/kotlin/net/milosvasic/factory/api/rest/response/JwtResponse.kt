package net.milosvasic.factory.api.rest.response

class JwtResponse{

    var accessToken: String? = null
    var id: Long? = null
    var username: String? = null
    var email: String? = null
    var roles: MutableList<String>? = null
    var tokenType: String? = null

    constructor()

    constructor(accessToken: String, id: Long?, username: String, email: String?, roles: MutableList<String>?, tokenType: String? = "Bearer"){
        this.accessToken = accessToken
        this.id = id
        this.username = username
        this.email = email
        this.roles = roles
        this.tokenType = tokenType
    }

}