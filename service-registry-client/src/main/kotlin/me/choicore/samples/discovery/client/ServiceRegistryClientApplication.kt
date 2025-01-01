package me.choicore.samples.discovery.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class ServiceRegistryClientApplication

fun main(args: Array<String>) {
    val ac = runApplication<ServiceRegistryClientApplication>(*args)
}

@RestController
@RequestMapping("/services")
class ServiceController(
    private val environment: Environment,
) {
    @GetMapping
    fun getServices(): Map<String, Any> {
        log.info("ServiceController.getServices()")
        val serverPort: String = environment.getProperty("local.server.port")!!
        return linkedMapOf(
            "servicePort" to serverPort,
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ServiceController::class.java)
    }
}
