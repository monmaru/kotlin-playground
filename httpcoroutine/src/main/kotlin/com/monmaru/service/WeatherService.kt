package com.monmaru.service

import com.github.kittinunf.fuel.httpGet
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.monmaru.model.Weather
import java.util.Date


class WeatherService {

    private val weatherAdapter = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
            .adapter(Weather::class.java)!!

    fun forecast(cityCode: Int): Weather {
        val url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=$cityCode"
        val (_, _, result) = url.httpGet().responseString()
        return result.fold(
                success = {weatherAdapter.fromJson(it) ?: throw Exception("decode failed") },
                failure = { throw it })
    }
}
