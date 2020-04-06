package no.nav.melosys.melosysmock.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
fun Route.dokkatApi() {

    @Location("/dokumenttypeinfo/v4/{eksternDokumenttypeId}/{eksternIdType}")
    data class GetTypeId(val eksternDokumenttypeId: String, val eksternIdType: String)
    get { req: GetTypeId ->
        call.respond(message = mapOf("dokumenttypeId" to req.eksternIdType), status = HttpStatusCode.OK)
    }

    @Location("/dokumenttypeinfo/v4{dokumenttypeId}")
    data class GetTypeInfo(val dokumenttypeId: String)
    get { req: GetTypeInfo ->
        call.respond(
                message = mapOf(
                        "dokumenttypeId" to req.dokumenttypeId,
                        "dokumentTittel" to "tittel for ${req.dokumenttypeId}",
                        "dokumentType" to "SED",
                        "dokumentKategori" to "SED",
                        "sensitivt" to false,
                        "tema" to "UFM",
                        "behandlingstema" to null,
                        "arkivSystem" to null,
                        "artifaktId" to null
                ),
                status = HttpStatusCode.OK)
    }
}
