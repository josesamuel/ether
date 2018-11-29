package sample.mvp.school.view

import sample.mvp.base.MVPView
import sample.mvp.school.SchoolData
import sample.mvp.weather.WeatherData

/**
 * Interface for the weather view
 */
interface WeatherView : MVPView {

    fun showWeatherData(data: WeatherData)

}