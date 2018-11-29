package sample.mvp.school.presenter

import sample.mvp.base.Presenter
import sample.mvp.repo.Repository
import sample.mvp.school.SchoolData
import sample.mvp.school.view.SchoolView
import sample.mvp.school.view.WeatherView

/**
 * Presenter for [WeatherView]
 */
class SchoolPresenter(view: SchoolView, val repository: Repository) : Presenter<SchoolView>(view) {


    fun onAustinSelected() {
        displayCityData("austin")
    }

    fun onDallasSelected() {
        displayCityData("dallas")
    }

    private fun displayCityData(city: String) {
        val schoolData: SchoolData = repository.getSchoolData(city)
        view.showSchoolData(schoolData)
    }

}