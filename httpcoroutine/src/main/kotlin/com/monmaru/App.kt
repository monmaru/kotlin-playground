package com.monmaru

import java.text.SimpleDateFormat
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.experimental.*
import com.monmaru.service.GitHubService
import com.monmaru.service.OreillyService
import com.monmaru.service.WeatherService


fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        val weather = async { WeatherService().forecast(cityCode = 140010) }
        val bookCatalog = async { OreillyService().fetchBookCatalog() }
        val newEBooks = async { OreillyService().fetchNewEBooks() }
        val repositories = async { GitHubService().fetchRepositories(lang = "kotlin") }

        weather.await()
                .forecasts
                .forEach {
                    println(SimpleDateFormat("yyyy/MM/dd").format(it.date))
                    println("横浜の天気は「${it.telop}」です。")
                    println("最低気温 ${it.temperature.min?.celsius ?: "-"}")
                    println("最高気温 ${it.temperature.max?.celsius ?: "-"}")
                }
        bookCatalog.await().forEach(::println)
        newEBooks.await().forEach(::println)
        repositories.await().forEach(::println)
    }
    println("Completed in $time ms")
}