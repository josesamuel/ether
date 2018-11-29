package sample.ether

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import arch.ether.Ether
import arch.ether.IProducer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import sample.ether.city.Austin
import sample.ether.city.CityDataTrigger
import sample.ether.city.Dallas
import sample.ether.school.SchoolDataPublisher
import sample.ether.school.initSchoolSubscriber
import sample.ether.weather.WeatherDataPublisher
import sample.ether.weather.initWeatherSubscriber

/**
 * Sample activity that shows how events trigger data and that results in changes to the UI
 */
class EtherSampleMainActivity : AppCompatActivity() {


    private val publishers = mutableListOf<IProducer>()
    private val subscribers = mutableListOf<Disposable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPublishers()
        initSubscribers()
        initTriggers()
    }


    /**
     * Initialize the publishers. Could be defined and injected
     */
    private fun initPublishers() {
        publishers.add(SchoolDataPublisher())
        publishers.add(WeatherDataPublisher())
    }

    /**
     * Initialize the subscribers, Could be defined and injected
     */
    private fun initSubscribers() {
        subscribers.add(initWeatherSubscriber(maxTemp, minTemp))
        subscribers.add(initSchoolSubscriber(schoolCount, studentCount))
        subscribers.add(initLoggerSubscriber())
    }

    /**
     * Sets up the triggers to [Ether]
     */
    private fun initTriggers() {
        austin.setOnClickListener { Ether.publisherOf(CityDataTrigger::class.java).publish(Austin) }
        dallas.setOnClickListener { Ether.publisherOf(CityDataTrigger::class.java).publish(Dallas) }
    }

    override fun onDestroy() {
        publishers.forEach { it.destroy() }
        subscribers.forEach { it.dispose() }
        super.onDestroy()
    }


}
