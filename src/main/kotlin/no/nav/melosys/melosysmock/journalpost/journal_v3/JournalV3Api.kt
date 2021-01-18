package no.nav.melosys.melosysmock.journalpost.journal_v3

import no.nav.melosys.melosysmock.journalpost.JournalpostRepo.repo
import no.nav.melosys.melosysmock.journalpost.intern_modell.VariantFormat
import no.nav.tjeneste.virksomhet.journal.v3.HentDokument
import no.nav.tjeneste.virksomhet.journal.v3.HentDokumentDokumentIkkeFunnet
import no.nav.tjeneste.virksomhet.journal.v3.HentDokumentJournalpostIkkeFunnet
import no.nav.tjeneste.virksomhet.journal.v3.HentDokumentResponse
import org.springframework.context.annotation.Configuration
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import org.springframework.core.io.ClassPathResource

import org.springframework.xml.xsd.SimpleXsdSchema

import org.springframework.xml.xsd.XsdSchema

import org.springframework.context.annotation.Bean

import org.springframework.ws.config.annotation.EnableWs
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition


@Endpoint
class JournalV3Api {

    @PayloadRoot(namespace = "http://nav.no/tjeneste/virksomhet/journal/v3", localPart = "hentDokument")
    @ResponsePayload
    fun hentDokument(@RequestPayload hentDokument: HentDokument): HentDokumentResponse {
        val dokument = (repo[hentDokument.request.journalpostId] ?: throw HentDokumentJournalpostIkkeFunnet("Ingen journalpost med id ${hentDokument.request.journalpostId}"))
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
}

@Configuration
class JournalV3Config {
    @Bean(name = ["hentDokument"])
    fun defaultWsdl11Definition(hentDokumentSchema: XsdSchema): DefaultWsdl11Definition {
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
