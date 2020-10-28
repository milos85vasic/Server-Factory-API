package net.milosvasic.factory.api.rest.payload

import javax.validation.constraints.NotBlank

class LoginRequest {

    var usernameOrEmail: @NotBlank String? = null
    var password: @NotBlank String? = null
}