package no.nav.melosys.melosysmock.eux

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.melosys.melosysmock.utils.FileUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EuxApi {

    @GetMapping("/buc/{bucID}")
    fun hentBuc(@RequestParam("bucID") bucID: String) = ObjectMapper().readTree(FileUtils.hentBuc(bucID))

    @GetMapping("/buc/{bucID}/sed/{sedID}")
    fun hentSed(@RequestParam("bucID") bucID: String, @RequestParam sedID: String) : Any =
            ObjectMapper().readTree(FileUtils.hentSed(bucID, sedID))


    @PostMapping("/buc/{bucID}/sed")
    fun opprettBuc(@RequestParam("bucID") bucID: String, @RequestParam sedID: String) : String = "100485"


    @GetMapping("/institusjoner")
    fun hentInstitusjoner() : Any = ObjectMapper().readTree(FileUtils.hentInstitusjoner())

}

