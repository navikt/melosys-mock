package no.nav.melosys.melosysmock.journalpost.journalfoerinngaaende

import no.nav.melosys.melosysmock.journalpost.JournalpostRepo.repo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class JournalfoerinngaaendeApi {
    @GetMapping("/rest/journalfoerinngaaende/v1/journalposter/{journalpostID}")
    fun hentJournalpost(@PathVariable("journalpostID") journalpostID: String): HentJournalpostResponse {
        return lagRespons((repo[journalpostID] ?: throw NoSuchElementException("Finner ikke journalpost $journalpostID")))
    }
}
