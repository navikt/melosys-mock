package no.nav.melosys.melosysmock.oppgave

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OppgaveApi {
    @GetMapping("/v1/oppgaver/{oppgaveID}")
    fun hentOppgave(@RequestParam("oppgaveID") oppgaveID: String) = "TODO"
}

