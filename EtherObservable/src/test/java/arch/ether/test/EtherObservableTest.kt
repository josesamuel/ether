package arch.ether.test

import arch.ether.*
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

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

        Assert.assertNull(Ether.subscriberOf(String::class.java).getCurrentData())

        publishedData.forEach {
            expectedData = it
            stringProducer1.publish(it)
            Assert.assertEquals(expectedData, Ether.subscriberOf(String::class.java).getCurrentData())
        }

        expectedData = ""

        publishedData.forEach {
            expectedData = it
            stringProducer2.publish(it)
        }

        Assert.assertEquals(publishedData.size * 12, totalReceived)

        var gotLastItem = false
        var lock = CountDownLatch(1)
        Ether.observableOf(String::class.java).subscribe {
            //expect the last tring
            Assert.assertEquals("5", it)
            gotLastItem = true
            lock.countDown()
        }

        lock.await(1000, TimeUnit.MILLISECONDS)
        Assert.assertTrue(gotLastItem)

        Assert.assertEquals("5", Ether.subscriberOf(String::class.java).getCurrentData())

        Ether.clear()
        Assert.assertNull(Ether.subscriberOf(String::class.java).getCurrentData())
    }

    @Test
    fun testPubSubWithContext() {
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
        var totalReceived = 0


        val context = object :EtherContext{}
        val stringProducer1WithContext = Ether.publisherOf(String::class.java, context)
        val stringProducer2WithContext = Ether.publisherOf(String::class.java, context)

        val subscribersWithContext = arrayOf(
                Ether.subscriberOf(String::class.java, context),
                Ether.subscriberOf(String::class.java, context)
        )

        val observersWithContext = arrayOf(
                Ether.observableOf(String::class.java, context),
                Ether.observableOf(String::class.java, context),
                EtherObservable.observableOf(String::class.java, context),
                EtherObservable.observableOf(String::class.java, context)
        )

        val publishedDataWithContext = arrayOf("10", "20", "30", "40", "50")
        var expectedDataWithContext = ""
        var totalReceivedWithContext = 0




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


        subscribersWithContext.forEach {
            it.subscribe(IDataSubscriber {
                Assert.assertEquals(expectedDataWithContext, it)
                totalReceivedWithContext++
            })
        }

        observersWithContext.forEach {
            it.subscribe {
                Assert.assertEquals(expectedDataWithContext, it)
                totalReceivedWithContext++
            }
        }


        Assert.assertNull(Ether.subscriberOf(String::class.java).getCurrentData())
        Assert.assertNull(Ether.subscriberOf(String::class.java, context).getCurrentData())


        publishedData.forEach {
            expectedData = it
            stringProducer1.publish(it)
            Assert.assertEquals(expectedData, Ether.subscriberOf(String::class.java).getCurrentData())
        }

        expectedData = ""

        publishedData.forEach {
            expectedData = it
            stringProducer2.publish(it)
        }


        publishedDataWithContext.forEach {
            expectedDataWithContext = it
            stringProducer1WithContext.publish(it)
            Assert.assertEquals(expectedDataWithContext, Ether.subscriberOf(String::class.java, context).getCurrentData())
        }

        expectedDataWithContext = ""

        publishedDataWithContext.forEach {
            expectedDataWithContext = it
            stringProducer2WithContext.publish(it)
        }

        Assert.assertEquals(publishedData.size * 12, totalReceived)
        Assert.assertEquals(publishedDataWithContext.size * 12, totalReceivedWithContext)

        Ether.clear()
        Ether.clear(context)
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