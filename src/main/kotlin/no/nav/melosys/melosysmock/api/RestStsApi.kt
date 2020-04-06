package no.nav.melosys.melosysmock.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
fun Route.restStsApi() {

    @Location("/v1/sts/token")
    data class GetCredentials(val ignore: String = "ignoreme")
    get { _: GetCredentials ->
        call.respond(message = mapOf("access_token" to "eylalala", "expires_in" to 100000), status = HttpStatusCode.OK)
    }
}
