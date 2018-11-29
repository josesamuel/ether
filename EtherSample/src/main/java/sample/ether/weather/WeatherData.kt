package sample.ether.weather

import arch.ether.annotation.EtherData
import sample.ether.city.CityDataTrigger


/**
 * Weather data model
 */
@EtherData(triggerType = CityDataTrigger::class)
data class WeatherData(val highest: Float, val lowest: Float)