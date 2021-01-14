package no.nav.melosys.melosysmock.journalpost.journalpostapi

import no.nav.melosys.melosysmock.journalpost.JournalpostMapper
import no.nav.melosys.melosysmock.journalpost.JournalpostRepo.repo
import no.nav.melosys.melosysmock.journalpost.intern_modell.JournalStatus
import no.nav.melosys.melosysmock.journalpost.intern_modell.JournalpostModell
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/journalpostapi/v1")
class JournalpostApi {

    @PostMapping("journalpost")
    fun opprettJournalpost(
        @RequestBody request: OpprettJournalpostRequest,
        @RequestParam("forsoekFerdigstill", required = false) forsoekFerdigstill: Boolean = false
    ): Map<String, Any> {
        return JournalpostMapper().tilModell(request, forsoekFerdigstill)
            .also { repo[it.journalpostId] = it }
            .let {
                mapOf(
                    "journalpostId" to it.journalpostId,
                    "journalpostferdigstilt" to if (it.journalStatus == JournalStatus.J) "true" else "false",
                    "dokumenter" to it.dokumentModellList.map { d -> mapOf("dokumentInfoId" to d.dokumentId) }
                )
            }
    }

    @PutMapping("/{journalpostID}")
    fun oppdaterJournalpost(
        @PathVariable("journalpostID") journalpostID: String,
        @RequestBody request: OppdaterJournalpostRequest
    ) {
        return (repo[journalpostID] ?: throw NoSuchElementException(""))
            .let { JournalpostMapper().oppdaterModell(request, it) }
            .also { repo[journalpostID] = it }
            .let { mapOf("journalpostId" to it.journalpostId) }
    }
}
