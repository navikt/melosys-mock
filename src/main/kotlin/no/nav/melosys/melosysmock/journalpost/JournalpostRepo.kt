package no.nav.melosys.melosysmock.journalpost

import no.nav.melosys.melosysmock.journalpost.intern_modell.JournalpostModell

object JournalpostRepo {
    val repo: MutableMap<String, JournalpostModell> = mutableMapOf()
}
