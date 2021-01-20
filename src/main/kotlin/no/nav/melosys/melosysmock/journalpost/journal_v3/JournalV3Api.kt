package no.nav.melosys.melosysmock.journalpost.journal_v3

import no.nav.melosys.melosysmock.journalpost.JournalpostRepo.repo
import no.nav.melosys.melosysmock.journalpost.intern_modell.JournalStatus
import no.nav.melosys.melosysmock.journalpost.intern_modell.Journalposttype
import no.nav.melosys.melosysmock.journalpost.intern_modell.VariantFormat
import no.nav.melosys.melosysmock.utils.tilXmlGregorianCalendar
import no.nav.tjeneste.virksomhet.journal.v3.*
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.*
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.*
import org.springframework.context.annotation.Configuration
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import org.springframework.core.io.ClassPathResource

import org.springframework.xml.xsd.SimpleXsdSchema

import org.springframework.xml.xsd.XsdSchema

import org.springframework.context.annotation.Bean

import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition


@Endpoint
class JournalV3Api {

    @PayloadRoot(namespace = "http://nav.no/tjeneste/virksomhet/journal/v3", localPart = "hentDokument")
    @ResponsePayload
    fun hentDokument(@RequestPayload hentDokument: HentDokument): HentDokumentResponse {
        val dokument = (repo[hentDokument.request.journalpostId]
            ?: throw HentDokumentJournalpostIkkeFunnet("Ingen journalpost med id ${hentDokument.request.journalpostId}"))
            .let { it.dokumentModellList.find { d -> d.dokumentId == hentDokument.request.dokumentId } }
            ?: throw HentDokumentDokumentIkkeFunnet("Ingen dokument med id ${hentDokument.request.dokumentId}")

        if (!dokument.harVariantArkiv()) {
            throw HentDokumentDokumentIkkeFunnet("Ingen arkivvarianter finnes for journalpost ${hentDokument.request.journalpostId}, dokument ${hentDokument.request.dokumentId}")
        }

        return dokument.dokumentVarianter?.first { it.variantFormat == VariantFormat.ARKIV }
            .let {
                HentDokumentResponse()
                    .withResponse(
                        no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentDokumentResponse()
                            .withDokument(it!!.dokumentInnhold)
                    )
            }!!
    }

    @PayloadRoot(namespace = "http://nav.no/tjeneste/virksomhet/journal/v3", localPart = "hentKjerneJournalpostListe")
    @ResponsePayload
    fun hentKjerneJournalpostListe(@RequestPayload hentKjerneJournalpostListe: HentKjerneJournalpostListe): HentKjerneJournalpostListeResponse {
        val arkivsaker = hentKjerneJournalpostListe.request.arkivSakListe.map { it.arkivSakId }

        return HentKjerneJournalpostListeResponse().apply {
            response = no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentKjerneJournalpostListeResponse().apply {
                this.journalpostListe.addAll(
                    repo.values.filter { arkivsaker.contains(it.sakId) }
                        .map {
                            Journalpost()
                                .withBrukerListe(Person().withIdent(it.bruker?.ident ?: ""))
                                .withForsendelseJournalfoert(
                                    it.journalfoertDato?.let { jfrDato -> tilXmlGregorianCalendar(jfrDato) }
                                )
                                .withForsendelseMottatt(
                                    it.mottattDato?.let { localDate ->  tilXmlGregorianCalendar(localDate) }
                                )
                                .withGjelderArkivSak(ArkivSak().withArkivSakSystem("GSAK").withArkivSakId(it.sakId))
                                .withHoveddokument(it.dokumentModellList[0].let { hovedDokument ->
                                    DetaljertDokumentinformasjon()
                                        .withDokumentId(hovedDokument.dokumentId)
                                        .withDokumentInnholdListe(hovedDokument.dokumentVarianter?.map { variant ->
                                            DokumentInnhold()
                                                .withArkivfiltype(Arkivfiltyper().withValue(variant.filType.name))
                                                .withVariantformat(Variantformater().withValue(variant.variantFormat?.name))
                                        })
                                        .withDokumentkategori(Dokumentkategorier().withValue(hovedDokument.dokumentkategori))
                                        .withTittel(hovedDokument.tittel)
                                })
                                .withTema(Tema().withValue(it.arkivtema?.name))
                                .withJournalpostId(it.journalpostId)
                                .withJournalposttype(Journalposttyper().withValue(
                                    if (it.journalposttype == Journalposttype.INNGAAENDE) "I" else "U"
                                ))
                                .withJournaltilstand(
                                    if (it.journalStatus == JournalStatus.J) Journaltilstand.ENDELIG else Journaltilstand.MIDLERTIDIG
                                )
                                .withKorrespondansePart(
                                    KorrespendansePart()
                                        .withKorrespondansepartId(it.avsenderMottaker.id)
                                        .withKorrespondansepartNavn(it.avsenderMottaker.navn)
                                        .withKorrespondansepartType(it.avsenderMottaker.type?.name)
                                )
                                .withVedleggListe(it.dokumentModellList.mapIndexedNotNull { i, dokument ->
                                    if (i != 0) DetaljertDokumentinformasjon()
                                        .withTittel(dokument.tittel)
                                        .withDokumentkategori(Dokumentkategorier().withValue(dokument.dokumentkategori))
                                        .withDokumentId(dokument.dokumentId)
                                    else null
                                })
                                .withMottakskanal(Mottakskanaler().withValue(it.kanal))
                        }
                )
            }
        }
    }
}

@Configuration
class JournalV3Config {
    @Bean(name = ["journalv3"])
    fun journalV3Wsdl11Definition(hentDokumentSchema: XsdSchema): DefaultWsdl11Definition {
        val wsdl11Definition = DefaultWsdl11Definition()
        wsdl11Definition.setPortTypeName("Journal_v3")
        wsdl11Definition.setLocationUri("/soap/services/journal/v3")
        wsdl11Definition.setTargetNamespace("http://nav.no/tjeneste/virksomhet/journal/v3")
        wsdl11Definition.setSchema(hentDokumentSchema)
        return wsdl11Definition
    }

    @Bean
    fun hentDokumentSchema(): XsdSchema {
        return SimpleXsdSchema(ClassPathResource("wsdl/no/nav/tjeneste/virksomhet/journal/v3/meldinger/meldinger.xsd"))
    }
}
