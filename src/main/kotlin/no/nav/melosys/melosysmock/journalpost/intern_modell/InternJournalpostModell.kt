package no.nav.melosys.melosysmock.journalpost.intern_modell

import java.time.LocalDate

data class JournalpostModell(
    var journalpostId: String,
    var avsenderMottaker: AvsenderMottaker,
    var dokumentModellList: List<DokumentModell> = mutableListOf(),
    var sakId: String? = null,
    var fagsystemId: String? = null,
    var journalStatus: JournalStatus? = null,
    var mottattDato: LocalDate? = null,
    var mottakskanal: String? = null,
    var arkivtema: Tema? = null,
    var journalposttype: Journalposttype? = null,
    var bruker: JournalpostBruker? = null,
    var kanal: String? = null,
    var tittel: String? = null,
    var eksternReferanseId: String? = null,
    var journalfoerendeEnhet: String? = null,
    val tilleggsoppltsninger: MutableSet<Tilleggsopplysning> = mutableSetOf()
)

data class DokumentModell(
    var dokumentId: String? = null,
    var dokumentType: String? = null,
    var erSensitiv: Boolean? = null,
    var tittel: String? = null,
    var brevkode: String? = null,
    var dokumentTilknyttetJournalpost: DokumentTilknyttetJournalpost? = null,
    var dokumentVarianter: List<DokumentVariantInnhold>? = mutableListOf(),
    var dokumentkategori: String? = null
)

data class AvsenderMottaker(
    var id: String? = null,
    var type: IdType? = null,
    var navn: String? = null,
    var land: String? = null
)

data class JournalpostBruker(
    var ident: String? = null,
    var brukerType: IdType? = null
)

class DokumentVariantInnhold(
    var filType: Arkivfiltype,
    var variantFormat: VariantFormat? = null,
    var dokumentInnhold: ByteArray
)

data class Tilleggsopplysning (
    var nokkel: String? = null,
    var verdi: String? = null
)

enum class JournalStatus {
    J, MO, M, A
}

enum class Tema {
    MED, UFM
}

enum class Journalposttype {
    INNGAAENDE, UTGAAENDE, NOTAT
}

enum class IdType {
    FNR, ORGNR, AKTOERID, UTL_ORG
}

enum class DokumentTilknyttetJournalpost {
    HOVEDDOKUMENT, VEDLEGG
}

enum class Arkivfiltype {
    PDF, PDFA, XML, RTF, AFP, META, DLF,
    JPEG, TIFF, DOC, DOCX, XLS, XLSX, AXML, DXML, JSON, PNG
}

enum class VariantFormat {
    ARKIV, FULLVERSJON, ORIGINAL
}
