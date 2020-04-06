package no.nav.melosys.melosysmock.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import no.nav.melosys.melosysmock.kafka.SedMottattProducer

@KtorExperimentalLocationsAPI
fun Route.kafkaApi() {

    @Location("/publiser")
    class PubliserKafkaDto
    get {_: PubliserKafkaDto ->
        SedMottattProducer.produserTestMelding()
        call.respond(message = mapOf("result" to "OK"), status = HttpStatusCode.OK)
    }

}
