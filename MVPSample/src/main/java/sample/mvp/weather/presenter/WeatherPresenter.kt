package sample.mvp.school.presenter

import sample.mvp.base.Presenter
import sample.mvp.repo.Repository
import sample.mvp.school.view.WeatherView
import sample.mvp.weather.WeatherData

/**
 * Presenter for [WeatherView]
 */
class WeatherPresenter(view: WeatherView, val repository: Repository) : Presenter<WeatherView>(view) {


    fun onAustinSelected() {
        displayCityData("austin")
    }

    fun onDallasSelected() {
        displayCityData("dallas")
    }

    private fun displayCityData(city: String) {
        val weatherlData: WeatherData = repository.getWeatherData(city)
        view.showWeatherData(weatherlData)
    }

}