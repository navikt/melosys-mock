package no.nav.melosys.melosysmock.api

import io.ktor.locations.Location
import io.ktor.routing.Route

fun Route.oppgaveApi() {

    @Location("/v1/")
    data class Todo(val tema: String)
}
