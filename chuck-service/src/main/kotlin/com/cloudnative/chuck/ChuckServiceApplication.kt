package com.cloudnative.chuck

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class ChuckServiceApplication {
    @Bean
    @LoadBalanced
    fun restTemplate() = RestTemplate()
}

fun main(args: Array<String>) {
    SpringApplication.run(ChuckServiceApplication::class.java, *args)
}

@RestController
class ChuckController(val chuckRepoClient: ChuckRepoClient) {
    @GetMapping("/joke")
    fun joke() = chuckRepoClient.joke()
}

@RibbonClient("chuck-repo")
class ChuckRepoClient(val restTemplate: RestTemplate) {
    fun joke(): JokeResponse =
            restTemplate.getForObject(
                    "http://chuck-repo/jokes/random",
                    JokeResponse::class.java)
}

data class JokeResponse(
        val joke: String? = null,
        val repoName: String? = null
)
