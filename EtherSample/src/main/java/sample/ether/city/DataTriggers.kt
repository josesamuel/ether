package sample.ether.city


/**
 * Triggers to produce [SchoolData] and [WeatherData]
 */
sealed class CityDataTrigger

object Austin : CityDataTrigger()

object Dallas : CityDataTrigger()

