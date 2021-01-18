package no.nav.melosys.melosysmock.sak

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.web.bind.annotation.*
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1/saker")
class SakApi {

    @PostMapping
    fun opprettSak(@RequestBody opprettSakRequest: SakDto): SakDto {

        if (opprettSakRequest.fagsakNr == null || opprettSakRequest.aktoerId == null || opprettSakRequest.tema == null) {
            throw IllegalArgumentException("fagsakNr, aktoerId og tema er påkrevd")
        }

        val sak = Sak(
            id = Random.nextLong(),
            tema = opprettSakRequest.tema!!,
            applikasjon = opprettSakRequest.applikasjon!!,
            fagsakNr = opprettSakRequest.fagsakNr!!,
            aktoerId = opprettSakRequest.aktoerId!!
        )

        SakRepo.leggTilSak(sak)
        return SakDto(
            tema = sak.tema,
            applikasjon = sak.applikasjon,
            fagsakNr = sak.fagsakNr,
            aktoerId = sak.aktoerId,
            orgnr = opprettSakRequest.orgnr,
            opprettetAv = "Melosys-mock :-)",
            opprettetTidspunkt = "1900-01-01"
        )
    }

    @GetMapping("/{id}")
    fun hentSak(@PathVariable("id") id: Long): SakDto {
        return SakRepo.repo[id]!!.let {
            SakDto(
                tema = it.tema,
                applikasjon = it.applikasjon,
                fagsakNr = it.fagsakNr,
                aktoerId = it.aktoerId,
                orgnr = "orgnr123321",
                opprettetAv = "Melosys-mock :-)",
                opprettetTidspunkt = "1900-01-01"
            )
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class SakDto(
    var tema: String? = null,
    var applikasjon: String? = null,
    var fagsakNr: String? = null,
    var aktoerId: String? = null,
    var orgnr: String? = null,
    var opprettetAv: String?,
    var opprettetTidspunkt: String? = null
)
