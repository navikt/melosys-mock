package no.nav.melosys.melosysmock.kafka



import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.*

object SedMottattProducer {

    private val producer: Producer<String, String> = producer()
    private val log = LoggerFactory.getLogger(javaClass)

    private fun producer(): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = listOf("localhost:9092")
        props["key.serializer"] = StringSerializer::class.java.canonicalName
        props["value.serializer"] = StringSerializer::class.java.canonicalName
        return KafkaProducer(props)
    }

    fun produserTestMelding() {
        val res = producer.send(ProducerRecord("eessi-basis-sedMottatt-v1", ObjectMapper().writeValueAsString(SedHendelse())))
        val offset = res.get().offset()
        log.info("Publisert på kafka med offset: $offset")
    }
}

data class SedHendelse (
    val id: Long = 0,
    val sedId: String = "100485_93f022ea50e54c08bbdb85290a5fb23d_1",
    val sektorKode: String = "LA",
    val bucType: String = "LA_BUC_04",
    val rinaSakId: String = "100485",
    val avsenderId: String = "SE:123",
    val avsenderNavn: String = "Svensk Trygdemyndighet",
    val mottakerId: String = "NO:1",
    val mottakerNavn: String = "NAV",
    val rinaDokumentId: String = "93f022ea50e54c08bbdb85290a5fb23d",
    val rinaDokumentVersjon: String = "1",
    val sedType: String = "A009",
    val navBruker: String = ""
)
