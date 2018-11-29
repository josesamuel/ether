package sample.ether

import android.util.Log
import arch.ether.Ether
import arch.ether.observableOf
import sample.ether.school.SchoolData
import sample.ether.weather.WeatherData


/**
 * Example of a subscriber that can be plugged in easily and that can listen go
 * multiple data of different types
 */
fun initLoggerSubscriber() =

        Ether.observableOf(WeatherData::class.java)
                .map { "WeatherData ${it.highest} ${it.lowest}" }
                .mergeWith(Ether.observableOf(SchoolData::class.java).map { "SchoolData ${it.schoolCount} ${it.studentCount}" })
                .subscribe {
                    Log.v("TestLog", it)
                }
