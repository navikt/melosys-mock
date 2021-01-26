package no.nav.melosys.melosysmock.abac

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/asm-pdp/authorize")
class AbacApi {

    @PostMapping
    fun evaluate() = mapOf(
        "Response" to mapOf(
            "Decision" to "Permit",
            "Obligations" to emptyList<Any>(),
            "AssociatedAdvice" to emptyList<Any>()
        )
    )
}
