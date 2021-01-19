package no.nav.melosys.melosysmock.person

import java.time.LocalDate

object PersonRepo {
    val repo = mutableMapOf<String, Person>()
    val aktørIdRepo = mutableMapOf<String, Person>()

    fun leggTilPerson(person: Person) {
        repo[person.ident] = person
        aktørIdRepo[person.aktørId] = person
    }
}

data class Person (
    val ident: String,
    val fornavn: String,
    val etternavn: String,
    val foedselsdato: LocalDate,
    val statsborgerskap: String,
    val kjønn: String,
    val aktørId: String
)
