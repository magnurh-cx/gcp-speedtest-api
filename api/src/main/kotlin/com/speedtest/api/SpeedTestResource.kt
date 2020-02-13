package com.speedtest.api

import com.google.gson.Gson
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
@RequestMapping("/speedtest")
class SpeedTestResource(val pubSub: PubSubTemplate) {

    data class Speeds(
        val download: Float,
        val upload: Float
    )

    data class Client(
        val ip: String,
        val lat: Float,
        val lon: Float,
        val isp: String,
        val country: String
    )

    data class Server(
            val host: String,
            val lat: Float,
            val lon: Float,
            val country: String,
            val distance: Int,
            val ping: Int,
            val id: String
    )

    data class SpeedTestData(
        val speeds: Speeds,
        val client: Client,
        val server: Server
    )

    data class SpeedTest(
            val user: String,
            val device: Int,
            val timestamp: Timestamp,
            val data: SpeedTestData)

    val gson: Gson = Gson();

    @PostMapping
    fun createUser(@RequestBody speedTest: SpeedTest) {
        this.pubSub.publish("speedtest", gson.toJson(speedTest))
        println(speedTest.user)
    }
}