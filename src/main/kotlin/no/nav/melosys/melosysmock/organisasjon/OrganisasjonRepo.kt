package no.nav.melosys.melosysmock.organisasjon

object OrganisasjonRepo {
    val repo: MutableMap<String, Organisasjon> = mutableMapOf()

    init {
        repo["11111111"] = Organisasjon(
            orgnr = "11111111",
            navn = "Ståles Stål AS",
            forretningsadresse = SemistrukturertAdresse(
                adresselinje1 = "Adresselinje1",
                poststed = "Oslo",
                postnummer = "0010",
                kommunenr = "0301",
                landkode = "NOR"
            ),
            postadresse = SemistrukturertAdresse(
                adresselinje1 = "Adresselinje1",
                poststed = "Adresselinje3",
                postnummer = "0010",
                kommunenr = "0301",
                landkode = "NOR"
            )
        )
    }
}

data class Organisasjon(
    val orgnr: String,
    val navn: String,
    val forretningsadresse: SemistrukturertAdresse,
    val postadresse: SemistrukturertAdresse
)

data class SemistrukturertAdresse(
    val adresselinje1: String,
    val poststed: String,
    val postnummer: String,
    val kommunenr: String,
    val landkode: String
)
