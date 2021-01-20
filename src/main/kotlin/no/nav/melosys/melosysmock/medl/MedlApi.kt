package no.nav.melosys.melosysmock.medl

import no.nav.melosys.melosysmock.person.PersonRepo
import no.nav.tjenester.medlemskapsunntak.api.v1.MedlemskapsunntakForGet
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/medl2/api/v1/medlemskapsunntak")
class MedlApi {

    @GetMapping
    fun hentPeriodeliste(@RequestHeader("Nav-Personident") fnr: String): List<MedlemskapsunntakForGet> {
        if (PersonRepo.repo[fnr] == null) throw NoSuchElementException("Ingen person med fnr $fnr")
        return emptyList()
    }
}
