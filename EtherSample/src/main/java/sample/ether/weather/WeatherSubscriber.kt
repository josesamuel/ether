package sample.ether.weather

import android.widget.TextView
import arch.ether.Ether
import arch.ether.observableOf
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * Update the weather views based on  [WeatherData] received from [Ether]
 *
 * Other ways to subscribe is [Ether.subscriberOf]
 */
fun initWeatherSubscriber(maxTemp: TextView, minTemp: TextView) =

        Ether.observableOf(WeatherData::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    maxTemp.text = it.highest.toString()
                    minTemp.text = it.lowest.toString()
                }
