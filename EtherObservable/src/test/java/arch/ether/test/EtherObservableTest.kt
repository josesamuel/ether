package arch.ether.test

import arch.ether.Ether
import arch.ether.EtherObservable
import arch.ether.IDataSubscriber
import arch.ether.observableOf
import org.junit.Assert
import org.junit.Test

/**
 * Test functionality of [Ether]
 */
class EtherObservableTest {


    @Test
    fun testPubSub() {
        val stringProducer1 = Ether.publisherOf(String::class.java)
        val stringProducer2 = Ether.publisherOf(String::class.java)

        val subscribers = arrayOf(
                Ether.subscriberOf(String::class.java),
                Ether.subscriberOf(String::class.java)
        )

        val observers = arrayOf(
                Ether.observableOf(String::class.java),
                Ether.observableOf(String::class.java),
                EtherObservable.observableOf(String::class.java),
                EtherObservable.observableOf(String::class.java)
        )

        val publishedData = arrayOf("1", "2", "3", "4", "5")
        var expectedData = ""
        var totalReceived = 0;

        subscribers.forEach {
            it.subscribe(IDataSubscriber {
                Assert.assertEquals(expectedData, it)
                totalReceived++
            })
        }

        observers.forEach {
            it.subscribe {
                Assert.assertEquals(expectedData, it)
                totalReceived++
            }
        }

        publishedData.forEach {
            expectedData = it
            stringProducer1.publish(it)
        }

        expectedData = ""

        publishedData.forEach {
            expectedData = it
            stringProducer2.publish(it)
        }

        Assert.assertEquals(publishedData.size * 12, totalReceived)
    }

    @Test
    fun testClear() {
        val stringProducer1 = Ether.publisherOf(String::class.java)
        val stringProducer2 = Ether.publisherOf(String::class.java)

        val stringSubscriber1 = Ether.observableOf(String::class.java)
        val stringSubscriber2 = Ether.subscriberOf(String::class.java)

        var expectingData = true
        var totalReceived = 0

        stringSubscriber1.subscribe {
            Assert.assertTrue(expectingData)
            totalReceived++
        }
        stringSubscriber2.subscribe(IDataSubscriber {
            Assert.assertTrue(expectingData)
            totalReceived++
        })

        stringProducer1.publish("1")
        stringProducer2.publish("2")

        Ether.clear()

        expectingData = false
        stringProducer1.publish("3")
        stringProducer1.publish("4")

        Assert.assertEquals(4, totalReceived)

    }


}