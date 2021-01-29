package no.nav.melosys.melosysmock.medl

import no.nav.melosys.melosysmock.medl.MedlRepo.repo
import no.nav.tjenester.medlemskapsunntak.api.v1.MedlemskapsunntakForGet
import no.nav.tjenester.medlemskapsunntak.api.v1.MedlemskapsunntakForPost
import no.nav.tjenester.medlemskapsunntak.api.v1.MedlemskapsunntakForPut
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/medl2/api/v1/medlemskapsunntak")
class MedlApi {

    @GetMapping
    fun hentPeriodeliste(
        @RequestHeader("Nav-Personident") fnr: String,
        @RequestParam("fraOgMed", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") fom: LocalDate?,
        @RequestParam("tilOgMed", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") tom: LocalDate?
    ): List<MedlemskapsunntakForGet> =
        repo.finn(fnr, fom, tom).toList()

    @GetMapping("/{periodeId}")
    fun hentPeriode(
        @PathVariable periodeId: Long
    ): MedlemskapsunntakForGet =
        repo.hent(periodeId)

    @PostMapping
    fun opprettPeriode(
        @RequestBody medlemskapsunntakForPost: MedlemskapsunntakForPost
    ): MedlemskapsunntakForGet =
        repo.opprett(medlemskapsunntakForPost)

    @PutMapping
    fun oppdaterPeriode(
        @RequestBody medlemskapsunntakForPut: MedlemskapsunntakForPut
    ): MedlemskapsunntakForGet =
        repo.oppdater(medlemskapsunntakForPut)
}
