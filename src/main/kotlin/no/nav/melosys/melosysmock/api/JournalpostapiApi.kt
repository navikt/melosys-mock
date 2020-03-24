package no.nav.melosys.melosysmock.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
fun Route.journalpostapiApi() {

    data class Dokument(val dokumentInfoId: String)
    data class JournalpostResponse(
            val journalpostId: String,
            val dokumenter: List<Dokument>,
            val journalstatus: String,
            val melding: String = ""
    )

    @Location("/journalpostapi/v1/journalpost")
    class OpprettJournalpost
    post { _: OpprettJournalpost ->
        call.respond(
                status = HttpStatusCode.OK,
                message = JournalpostResponse(
                        journalpostId = "1",
                        dokumenter = listOf(Dokument(dokumentInfoId = "3")),
                        journalstatus = "ENDELIG"
                )
        )
    }
}
