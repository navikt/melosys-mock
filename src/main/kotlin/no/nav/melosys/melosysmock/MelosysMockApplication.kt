package no.nav.melosys.melosysmock

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import no.nav.melosys.melosysmock.api.euxApi
import no.nav.melosys.melosysmock.api.journalpostapiApi


fun main(args: Array<String>) : Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.MelosysMockApplication() {

    install(ContentNegotiation) {
        jackson {}
    }
    install(Locations)

    install(Routing) {
        route("/api") {
        }

        route("/cpi") {
            euxApi()
        }

        route("/rest") {
            journalpostapiApi()
        }
    }
}
