package net.milosvasic.factory.api.rest.payload

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class SignUpRequest {

    var name: @NotBlank String? = null
    var username: @NotBlank String? = null
    var email: @NotBlank @Email String? = null
    var password: @NotBlank String? = null
}