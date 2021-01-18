package no.nav.melosys.melosysmock.journalpost.journalfoerinngaaende

import com.fasterxml.jackson.annotation.JsonFormat
import no.nav.melosys.melosysmock.journalpost.intern_modell.IdType
import no.nav.melosys.melosysmock.journalpost.intern_modell.JournalStatus
import no.nav.melosys.melosysmock.journalpost.intern_modell.JournalpostModell
import no.nav.melosys.melosysmock.journalpost.intern_modell.Journalposttype
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun lagRespons(journalpost: JournalpostModell) : HentJournalpostResponse {
    if (journalpost.journalposttype == Journalposttype.UTGAAENDE) {
        throw Exception("Journalpost er ikke av type inngaaende!")
    }
    return HentJournalpostResponse(
        journalTilstand = if (journalpost.journalStatus == JournalStatus.J) JournalTilstand.ENDELIG else JournalTilstand.MIDLERTIDIG,
        avsender = Avsender(
            avsenderType = if (journalpost.avsenderMottaker.type == IdType.FNR) BrukerType.PERSON else BrukerType.ORGANISASJON,
            identifikator = journalpost.avsenderMottaker.id,
            navn = journalpost.avsenderMottaker.navn
        ),
        brukerListe = listOf(Bruker(
            brukerType = if (journalpost.bruker?.brukerType == IdType.FNR) BrukerType.PERSON else BrukerType.ORGANISASJON,
            identifikator = journalpost.bruker?.ident
        )),
        arkivSak = ArkivSak(
            arkivSakId = journalpost.sakId,
            arkivSakSystem = null
        ),
        tema = journalpost.arkivtema?.name,
        tittel = journalpost.tittel,
        kanalReferanseId = journalpost.eksternReferanseId,
        forsendelseMottatt = journalpost.mottattDato?.atStartOfDay()?.atZone(ZoneId.systemDefault()),
        mottaksKanal = journalpost.kanal,
        journalfEnhet = journalpost.journalfoerendeEnhet,
        dokumentListe = journalpost.dokumentModellList.map {
            Dokument(
                dokumentId = it.dokumentId,
                dokumentTypeId = it.dokumentType, //?
                navSkjemaId = it.brevkode,
                tittel = it.tittel,
                dokumentKategori = it.dokumentkategori,
                variant = it.dokumentVarianter?.map { variant ->
                    Variant(
                        arkivFilType = variant.filType.name,
                        variantFormat = variant.variantFormat?.name
                    )
                }
            )
        }
    )
}

data class HentJournalpostResponse(
    val journalTilstand: JournalTilstand? = null,
    val avsender: Avsender? = null,
    val brukerListe: List<Bruker> = mutableListOf(),
    val arkivSak: ArkivSak? = null,
    val tema: String? = null,
    val tittel: String? = null,
    val kanalReferanseId: String? = null,
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "UTC")
    val forsendelseMottatt: ZonedDateTime? = null,
    val mottaksKanal: String? = null,
    val journalfEnhet: String? = null,
    val dokumentListe: List<Dokument> = mutableListOf()
)

data class Avsender(
    val avsenderType: BrukerType? = null,
    val identifikator: String? = null,
    val navn: String? = null
)

data class Dokument(
    val dokumentId: String? = null,
    val dokumentTypeId: String? = null,
    val navSkjemaId: String? = null,
    val tittel: String? = null,
    val dokumentKategori: String? = null,
    val variant: List<Variant>? = listOf()
)

data class Variant(
    val arkivFilType: String? = null,
    val variantFormat: String? = null
)

data class ArkivSak(
    val arkivSakSystem: String? = null,
    val arkivSakId: String? = null
)

data class Bruker(
    val brukerType: BrukerType? = BrukerType.PERSON,
    val identifikator: String? = null
)

enum class JournalTilstand {
    ENDELIG, MIDLERTIDIG, UTGAAR
}

enum class BrukerType {
    PERSON, ORGANISASJON
}
