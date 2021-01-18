package no.nav.melosys.melosysmock.tps

import no.nav.melosys.melosysmock.person.PersonRepo
import no.nav.tjeneste.virksomhet.person.v3.HentPerson
import no.nav.tjeneste.virksomhet.person.v3.HentPersonResponse
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet
import no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet
import no.nav.tjeneste.virksomhet.person.v3.informasjon.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition
import org.springframework.xml.xsd.SimpleXsdSchema
import org.springframework.xml.xsd.XsdSchema
import java.time.LocalDate
import javax.xml.datatype.DatatypeFactory

@Endpoint
class TpsApi {

    init {
        PersonRepo.repo["30056928150"] = no.nav.melosys.melosysmock.person.Person(
            ident = "30056928150",
            fornavn = "KARAFFEL",
            etternavn = "TRIVIELL",
            foedselsdato = LocalDate.of(1969, 5, 30),
            statsborgerskap = "NOR",
            kjønn = "M"
        )
    }

    @PayloadRoot(namespace = "http://nav.no/tjeneste/virksomhet/person/v3", localPart = "hentPerson")
    @ResponsePayload
    fun hentPerson(@RequestPayload hentPerson: HentPerson): HentPersonResponse {
        val ident = (hentPerson.request.aktoer as PersonIdent).ident.ident
        val person = PersonRepo.repo[ident] ?: throw HentPersonPersonIkkeFunnet("Fant ikke person", PersonIkkeFunnet())

        return HentPersonResponse()
            .withResponse(no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonResponse()
                .withPerson(
                    Person()
                        .withFoedselsdato(
                            Foedselsdato()
                                .withFoedselsdato(
                                    DatatypeFactory.newInstance().newXMLGregorianCalendar(person.foedselsdato.toString())
                                )
                        )
                        .withAktoer(PersonIdent().withIdent(NorskIdent().withIdent(ident)))
                        .withPersonnavn(
                            Personnavn()
                                .withFornavn(person.fornavn)
                                .withEtternavn(person.etternavn)
                                .withSammensattNavn("${person.fornavn} ${person.etternavn}")
                        )
                        .withStatsborgerskap(Statsborgerskap().withLand(Landkoder().withValue(person.statsborgerskap)))
                        .withKjoenn(Kjoenn().withKjoenn(Kjoennstyper().withValue(person.kjønn)))
                        .withPersonstatus(Personstatus().withPersonstatus(Personstatuser().withValue("BOSA"))) //BOSA = Bosatt
                        .withPostadresse(Postadresse().withUstrukturertAdresse(
                            UstrukturertAdresse()
                                .withAdresselinje1("Adresselinje 1")
                                .withAdresselinje2("Adresselinje 2")
                                .withAdresselinje3("Adresselinje 3")
                                .withAdresselinje4("Adresselinje 4")
                                .withPostnr("0010")
                                .withPoststed("Oslo")
                                .withLandkode(Landkoder().withValue("NOR"))
                        ))
                        .withBostedsadresse(Bostedsadresse().withStrukturertAdresse(
                            Gateadresse()
                                .withGatenavn("Gatenavn")
                                .withGatenummer(1)
                                .withHusbokstav("a")
                                .withLandkode(Landkoder().withValue("NOR"))
                                .withPoststed(Postnummer().withValue("0010"))
                        ))
                )
            )
    }
}

@Configuration
class TpsApiConfiguration {
    @Bean(name = ["hentPerson"])
    fun defaultWsdl11Definition(hentPersonSchema: XsdSchema): DefaultWsdl11Definition {
        val wsdl11Definition = DefaultWsdl11Definition()
        wsdl11Definition.setPortTypeName("Person_v3")
        wsdl11Definition.setLocationUri("/soap/services/tps/v3")
        wsdl11Definition.setTargetNamespace("http://nav.no/tjeneste/virksomhet/person/v3")
        wsdl11Definition.setSchema(hentPersonSchema)
        return wsdl11Definition
    }

    @Bean
    fun hentPersonSchema(): XsdSchema {
        return SimpleXsdSchema(ClassPathResource("wsdl/no/nav/tjeneste/virksomhet/person/v3/meldinger/meldinger.xsd"))
    }
}
