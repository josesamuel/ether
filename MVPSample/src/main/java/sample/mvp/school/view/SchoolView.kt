package sample.mvp.school.view

import sample.mvp.base.MVPView
import sample.mvp.school.SchoolData
import sample.mvp.weather.WeatherData

/**
 * Interface for the school view
 */
interface SchoolView : MVPView {

    fun showSchoolData(data: SchoolData)

}