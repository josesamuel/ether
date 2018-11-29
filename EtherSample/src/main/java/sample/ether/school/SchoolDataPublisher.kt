package sample.ether.school

import sample.ether.city.Austin
import sample.ether.city.CityDataTrigger
import sample.ether.city.Dallas

/**
 * Publisher of [SchoolData] by extending the auto generated [AbstractSchoolDataPublisher]
 *
 * A Publisher can also be created using the auto generated [SchoolDataPublisher]
 */
class SchoolDataPublisher : AbstractSchoolDataPublisher() {

    /**
     * Gets called when the trigger for [SchoolData] is sent to the [Ether]
     */
    override fun onPublisherTrigger(trigger: CityDataTrigger) {
        when (trigger) {
            Austin -> publish(SchoolData(20, 800))
            Dallas -> publish(SchoolData(45, 1900))
        }
    }
}