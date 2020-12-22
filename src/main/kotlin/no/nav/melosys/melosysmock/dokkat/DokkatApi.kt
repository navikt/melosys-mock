package no.nav.melosys.melosysmock.dokkat

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DokkatApi {


    @GetMapping("/dokumenttypeinfo/v4/{eksternDokumenttypeId}/{eksternIdType}")
    fun getTypeId(@RequestParam("eksternIdType") eksternIdType: String): Map<String, String> {
        return mapOf("dokumenttypeId" to eksternIdType)
    }

    @GetMapping("/dokumenttypeinfo/v4{dokumenttypeId}")
    fun getTypeInfo(@RequestParam("dokumenttypeId") dokumenttypeId: String): Map<String, Any?> {
        return mapOf(
                "dokumenttypeId" to dokumenttypeId,
                "dokumentTittel" to "tittel for ${dokumenttypeId}",
                "dokumentType" to "SED",
                "dokumentKategori" to "SED",
                "sensitivt" to false,
                "tema" to "UFM",
                "behandlingstema" to null,
                "arkivSystem" to null,
                "artifaktId" to null
        )
    }
}
