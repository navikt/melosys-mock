package no.nav.melosys.melosysmock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MelosysMockApplication

fun main(args: Array<String>) {
    runApplication<MelosysMockApplication>(*args)
}
