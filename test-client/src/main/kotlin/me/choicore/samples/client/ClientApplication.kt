package me.choicore.samples.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ClientApplication

fun main(args: Array<String>) {
    val ac = runApplication<ClientApplication>(*args)
    val webClient =
        ac
            .getBean(WebClient.Builder::class.java)
            .baseUrl("http://localhost:8080")
            .build()

    val list = mutableListOf<String>()
    while (true) {
        Thread.sleep(50)
        webClient
            .get()
            .uri("/services")
            .retrieve()
            .bodyToMono(Map::class.java)
            .subscribe {
                val port = it["servicePort"] as String
                val indexOf = list.indexOf(port) + 1
                if (indexOf == 0) {
                    list.add(port)
                }
                val repeat = "\t\t".repeat(indexOf)
                println(repeat + port)
            }
    }
}
