package sample.mvp.repo

import sample.mvp.school.SchoolData
import sample.mvp.weather.WeatherData

/**
 * Created by Anu Joseph on 11/27/2018.
 */
class Repository {

    fun getSchoolData(city : String) : SchoolData{
        when(city){
            "austin" -> return SchoolData(10,2000)
            "dallas" -> return SchoolData(20,5000)
            else ->{
                return SchoolData(-1,-1);
            }
        }
    }

    fun getWeatherData(city : String) : WeatherData{
        when(city){
            "austin" -> return WeatherData(70.0f,32f)
            "dallas" -> return WeatherData(60f,29f)
            else ->{
                return WeatherData(-1f,-1f);
            }
        }
    }
}