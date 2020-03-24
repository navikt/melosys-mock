package no.nav.melosys.melosysmock.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import no.nav.melosys.melosysmock.utils.FileUtils

@KtorExperimentalLocationsAPI
fun Route.euxApi() {

    @Location("/buc/{bucID}")
    data class Buc(val bucID: String)
    get { req: Buc ->
        call.respond(status = HttpStatusCode.OK, message = FileUtils.hentBuc(req.bucID))
    }
    delete<Buc> {
        call.respond(status = HttpStatusCode.OK, message = "{}")

    }

    @Location("/buc/{bucID}/sed/{sedID}")
    data class Sed(val bucID: String, val sedID: String)
    get { req: Sed ->
        call.respond(status = HttpStatusCode.OK, message = FileUtils.hentSed(req.bucID, req.sedID))
    }

    @Location("/buc/{bucID}/sed")
    data class OpprettSed(val bucID: String)
    post { req: OpprettSed ->
        call.respondText(status = HttpStatusCode.OK, text = "100485")
    }

    @Location("/institusjoner")
    class Institusjoner
    get { _: Institusjoner ->
        call.respond(HttpStatusCode.OK, FileUtils.hentInstitusjoner())
    }
}

