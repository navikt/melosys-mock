package no.nav.melosys.melosysmock.aareg

import no.nav.melosys.melosysmock.organisasjon.OrganisasjonRepo
import no.nav.melosys.melosysmock.person.PersonRepo
import no.nav.melosys.melosysmock.utils.tilXmlGregorianCalendar
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstaker
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstakerResponse
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerUgyldigInput
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.feil.UgyldigInput
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.*
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
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.datatype.DatatypeFactory


@Endpoint
class AaregApi {

    @PayloadRoot(
        namespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        localPart = "finnArbeidsforholdPrArbeidstaker"
    )
    @ResponsePayload
    fun finnArbeidsforholdPrArbeidstaker(
        @RequestPayload request: FinnArbeidsforholdPrArbeidstaker
    )
            : FinnArbeidsforholdPrArbeidstakerResponse {
        val person = PersonRepo.repo[request.parameters.ident.ident]
            ?: throw FinnArbeidsforholdPrArbeidstakerUgyldigInput(
                "Ingen person med ident ${request.parameters.ident}",
                UgyldigInput()
            )

        val ansettelsesperiodeFom = tilXmlGregorianCalendar(LocalDate.now().minusYears(5))
        val organisasjon = OrganisasjonRepo.repo.values.first()

        val arbeidsforhold = Arbeidsforhold().apply {
            arbeidsforholdID = "123"
            arbeidsforholdIDnav = 123
            opplysningspliktig = Organisasjon().apply {
                navn = organisasjon.navn
                orgnummer = organisasjon.orgnr
            }
            ansettelsesPeriode = AnsettelsesPeriode().apply {
                periode = Gyldighetsperiode().apply {
                    fom = ansettelsesperiodeFom
                }
            }
            arbeidsforholdstype = Arbeidsforholdstyper().apply {
                value = "Ordinært arbeidsforhold"
                kodeverksRef = "ordinaertArbeidsforholdp"
            }
            arbeidsgiver = Organisasjon().apply {
                navn = organisasjon.navn
                orgnummer = organisasjon.orgnr
            }
            arbeidstaker = Person().apply {
                ident = NorskIdent().apply { ident = person.ident }
            }
            arbeidsavtale.add(Arbeidsavtale().apply {
                fomGyldighetsperiode = ansettelsesperiodeFom
                arbeidstidsordning = Arbeidstidsordninger().apply {
                    value = "Ikke skift"
                    kodeRef = "ikkeSkift"
                }
                avloenningstype = Avloenningstyper().apply { value = "fast" }
                yrke = Yrker().apply {
                    kodeRef = "0013008"
                    value = "Konsulent"
                }
                avtaltArbeidstimerPerUke = BigDecimal("37.5")
                stillingsprosent = BigDecimal("100")
                sisteLoennsendringsdato = ansettelsesperiodeFom
                beregnetAntallTimerPrUke = BigDecimal("37.5")
                beregnetStillingsprosent = BigDecimal("100")
            })
        }

        val res = FinnArbeidsforholdPrArbeidstakerResponse()
        res.parameters = no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerResponse()
        res.parameters.arbeidsforhold.add(arbeidsforhold)
        return res
    }
}

@Configuration
class AaregSoapConfig {

    @Bean(name = ["aareg"])
    fun aktoerIdDefinition(aaregSchema: XsdSchema): DefaultWsdl11Definition {
        val wsdl11Definition = DefaultWsdl11Definition()
        wsdl11Definition.setPortTypeName("Arbeidsforhold_v3")
        wsdl11Definition.setLocationUri("/soap/services/aareg/v3")
        wsdl11Definition.setTargetNamespace("http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3")
        wsdl11Definition.setSchema(aaregSchema)
        return wsdl11Definition
    }

    @Bean
    fun aaregSchema(): XsdSchema {
        return SimpleXsdSchema(ClassPathResource("wsdl/no/nav/tjeneste/virksomhet/arbeidsforhold/v3/meldinger/meldinger.xsd"))
    }
}
