package net.milosvasic.factory.api.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerFactoryApiApplication

fun main(args: Array<String>) {
	runApplication<ServerFactoryApiApplication>(*args)
}
