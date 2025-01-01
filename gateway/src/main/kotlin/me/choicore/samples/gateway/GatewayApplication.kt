package me.choicore.samples.gateway

import com.netflix.discovery.EurekaEvent
import com.netflix.discovery.EurekaEventListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.configuration.TlsProperties
import org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration
import org.springframework.cloud.netflix.eureka.http.WebClientDiscoveryClientOptionalArgs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import java.io.IOException
import java.security.GeneralSecurityException
import java.time.Duration
import java.time.LocalDateTime

@SpringBootApplication
@EnableDiscoveryClient
class GatewayApplication

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}

@Configuration(proxyBeanMethods = false)
class EurekaConfiguration {
    @Bean
    @Throws(GeneralSecurityException::class, IOException::class)
    fun webClientDiscoveryClientOptionalArgs(
        tlsProperties: TlsProperties,
        webClientBuilder: WebClient.Builder,
    ): WebClientDiscoveryClientOptionalArgs {
        val result =
            WebClientDiscoveryClientOptionalArgs { webClientBuilder }
                .apply {
                    setEventListeners(setOf(EurekaDiscoveryEventListener()))
                }
        DiscoveryClientOptionalArgsConfiguration.setupTLS(result, tlsProperties)
        return result
    }

    class EurekaDiscoveryEventListener : EurekaEventListener {
        @Volatile
        private var lastRetrievedAt = LocalDateTime.now()

        override fun onEvent(event: EurekaEvent) {
            val retrievedAt = LocalDateTime.now()
            val duration = Duration.between(lastRetrievedAt, retrievedAt)
            println("====================================================================================================")
            println("Event: $event")
            println("Duration: ${duration.toSeconds()} seconds")
            println("====================================================================================================")
            lastRetrievedAt = retrievedAt
        }
    }
}
