package no.nav.melosys.melosysmock.melosyseessi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MelosysEessiApi {

    @GetMapping("/api/buc/{bucType}/institusjoner")
    fun hentMottakerinstitusjoner() : Array<Unit> = emptyArray()
}
