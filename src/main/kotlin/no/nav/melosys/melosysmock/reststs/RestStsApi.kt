package no.nav.melosys.melosysmock.reststs

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestStsApi {
    @GetMapping("/rest/v1/sts/token")
    fun getCredentials() : Map<String, Any> = mapOf("access_token" to "eylalala", "expires_in" to 100000)
}
