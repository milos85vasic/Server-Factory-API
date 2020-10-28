package net.milosvasic.factory.api.rest

import net.milosvasic.factory.api.rest.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class ServerFactoryApiApplication

fun main(args: Array<String>) {
	runApplication<ServerFactoryApiApplication>(*args)
}
