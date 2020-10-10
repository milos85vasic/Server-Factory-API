package net.milosvasic.factory.api.rest.request

import javax.validation.constraints.NotBlank

class LoginRequest {
    var username: @NotBlank String? = null
    var password: @NotBlank String? = null

    constructor() {}
    constructor(username: String?,password: String?) {
        this.username = username
        this.password = password
    }
}