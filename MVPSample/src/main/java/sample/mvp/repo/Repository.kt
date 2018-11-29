package sample.mvp.repo

import sample.mvp.school.SchoolData
import sample.mvp.weather.WeatherData

class Repository {

    fun getSchoolData(city : String) : SchoolData{
        return when(city){
            "austin" -> SchoolData(10,2000)
            "dallas" -> SchoolData(20,5000)
            else ->{
                SchoolData(-1,-1);
            }
        }
    }

    fun getWeatherData(city : String) : WeatherData{
        return when(city){
            "austin" -> WeatherData(70.0f,32f)
            "dallas" -> WeatherData(60f,29f)
            else ->{
                WeatherData(-1f,-1f);
            }
        }
    }
}