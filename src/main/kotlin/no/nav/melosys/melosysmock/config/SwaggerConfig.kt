package no.nav.melosys.melosysmock.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("no.nav.melosys.melosysmock"))
            .paths(PathSelectors.any())
            .build()
            .enable(true)
    }

    fun apiInfo() = ApiInfo(
        "Melosys Mock",
        "Mock av Melosys eksterne avhengigheter",
        "1",
        "",
        Contact("", "", ""),
        "",
        "",
        emptyList()
    )

}
