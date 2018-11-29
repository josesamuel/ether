package sample.ether.school

import arch.ether.annotation.EtherData
import sample.ether.city.CityDataTrigger

/**
 * School data model.
 */
@EtherData(triggerType = CityDataTrigger::class)
data class SchoolData(val schoolCount: Int, val studentCount: Int)