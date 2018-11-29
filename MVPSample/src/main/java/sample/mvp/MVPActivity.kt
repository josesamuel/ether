package sample.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import sample.mvp.repo.Repository
import sample.mvp.school.SchoolData
import sample.mvp.school.presenter.SchoolPresenter
import sample.mvp.school.presenter.WeatherPresenter
import sample.mvp.school.view.SchoolView
import sample.mvp.school.view.WeatherView
import sample.mvp.weather.WeatherData


class MVPActivity : AppCompatActivity(), SchoolView, WeatherView {

    private lateinit var weatherPresenter: WeatherPresenter
    private lateinit var schoolPresenter: SchoolPresenter
    private val repo = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create presenter
        weatherPresenter = WeatherPresenter(this, repo)
        schoolPresenter = SchoolPresenter(this, repo)


        //set up listeners
        austin.setOnClickListener {
            weatherPresenter.onAustinSelected()
            schoolPresenter.onAustinSelected()
        }

        dallas.setOnClickListener {
            weatherPresenter.onDallasSelected()
            schoolPresenter.onDallasSelected()
        }
    }

    override fun showSchoolData(data: SchoolData) {
        schoolCount.text = data.schoolCount.toString()
        studentCount.text = data.studentCount.toString()
    }

    override fun showWeatherData(data: WeatherData) {
        maxTemp.text = data.highest.toString()
        minTemp.text = data.lowest.toString()
    }
}
