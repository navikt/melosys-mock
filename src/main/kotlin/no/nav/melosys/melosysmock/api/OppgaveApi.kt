package no.nav.melosys.melosysmock.api

import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
fun Route.oppgaveApi() {

    @Location("/v1/")
    data class Oppgave(val tema: String)
    post { _: Oppgave ->

    }
}
