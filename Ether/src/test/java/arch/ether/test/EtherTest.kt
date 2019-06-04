package arch.ether.test

import arch.ether.Ether
import arch.ether.EtherContext
import arch.ether.IDataSubscriber
import org.junit.Assert
import org.junit.Test

/**
 * Test functionality of [Ether]
 */
class EtherTest {

    @Test
    fun testSameInstance() {
        val stringProducer1 = Ether.publisherOf(String::class.java)
        val stringProducer2 = Ether.publisherOf(String::class.java)

        val intProducer1 = Ether.publisherOf(Int::class.java)
        val intProducer2 = Ether.publisherOf(Int::class.java)

        Assert.assertEquals(stringProducer1, stringProducer2)
        Assert.assertEquals(intProducer1, intProducer2)
        Assert.assertNotEquals(stringProducer1, intProducer1)

        val stringSubscriber1 = Ether.subscriberOf(String::class.java)
        val stringSubscriber2 = Ether.subscriberOf(String::class.java)

        val intSubscriber1 = Ether.subscriberOf(Int::class.java)
        val intSubscriber2 = Ether.subscriberOf(Int::class.java)

        Assert.assertEquals(stringSubscriber1, stringSubscriber2)
        Assert.assertEquals(intSubscriber1, intSubscriber2)
        Assert.assertNotEquals(stringSubscriber1, intProducer1)
    }

    @Test
    fun testDifferentInstance() {
        val context2 = object : EtherContext{}
        val context3 = object : EtherContext{}
        val stringProducer1 = Ether.publisherOf(String::class.java)
        val stringProducer2 = Ether.publisherOf(String::class.java, context2)
        val stringProducer3 = Ether.publisherOf(String::class.java, context3)


        Assert.assertNotEquals(stringProducer1, stringProducer2)
        Assert.assertNotEquals(stringProducer1, stringProducer3)
        Assert.assertNotEquals(stringProducer2, stringProducer3)

        val stringSubscriber1 = Ether.subscriberOf(String::class.java)
        val stringSubscriber2 = Ether.subscriberOf(String::class.java, context2)
        val stringSubscriber3  = Ether.subscriberOf(String::class.java, context3)


        Assert.assertNotEquals(stringSubscriber1, stringSubscriber2)
        Assert.assertNotEquals(stringSubscriber1, stringSubscriber3)
        Assert.assertNotEquals(stringSubscriber2, stringSubscriber3)
    }


    @Test
    fun testPubSub() {
        val stringProducer1 = Ether.publisherOf(String::class.java)
        val stringProducer2 = Ether.publisherOf(String::class.java)

        val stringSubscriber1 = Ether.subscriberOf(String::class.java)
        val stringSubscriber2 = Ether.subscriberOf(String::class.java)

        val publishedData = arrayOf("1", "2", "3", "4", "5")
        var expectedData = ""
        var totalReceived = 0;

        stringSubscriber1.subscribe(IDataSubscriber {
            Assert.assertEquals(expectedData, it)
            totalReceived++
        })
        stringSubscriber2.subscribe(IDataSubscriber {
            Assert.assertEquals(expectedData, it)
            totalReceived++
        })

        publishedData.forEach {
            expectedData = it
            stringProducer1.publish(it)
        }

        expectedData = ""

        publishedData.forEach {
            expectedData = it
            stringProducer2.publish(it)
        }

        Assert.assertEquals(publishedData.size * 4, totalReceived)

        Ether.clear()
    }

    @Test
    fun testPubSubContexts() {
        val stringProducer1 = Ether.publisherOf(String::class.java)
        val stringProducer2 = Ether.publisherOf(String::class.java)

        val stringSubscriber1 = Ether.subscriberOf(String::class.java)
        val stringSubscriber2 = Ether.subscriberOf(String::class.java)

        val publishedData = arrayOf("1", "2", "3", "4", "5")
        var expectedData = ""
        var totalReceived = 0

        val context2 = object : EtherContext{}

        val stringProducerContext1 = Ether.publisherOf(String::class.java, context2)
        val stringProducerContext2 = Ether.publisherOf(String::class.java, context2)

        val stringSubscriberContext1 = Ether.subscriberOf(String::class.java, context2)
        val stringSubscriberContext2 = Ether.subscriberOf(String::class.java, context2)

        val publishedDataWithContext = arrayOf("10", "20", "30", "40", "50")
        var expectedDataWithContext = ""
        var totalReceivedWithContext = 0


        stringSubscriber1.subscribe(IDataSubscriber {
            Assert.assertEquals(expectedData, it)
            totalReceived++
        })
        stringSubscriber2.subscribe(IDataSubscriber {
            Assert.assertEquals(expectedData, it)
            totalReceived++
        })


        stringSubscriberContext1.subscribe(IDataSubscriber {
            Assert.assertEquals(expectedDataWithContext, it)
            totalReceivedWithContext++
        })
        stringSubscriberContext2.subscribe(IDataSubscriber {
            Assert.assertEquals(expectedDataWithContext, it)
            totalReceivedWithContext++
        })


        publishedData.forEach {
            expectedData = it
            stringProducer1.publish(it)
        }

        expectedData = ""

        publishedData.forEach {
            expectedData = it
            stringProducer2.publish(it)
        }


        publishedDataWithContext.forEach {
            expectedDataWithContext = it
            stringProducerContext1.publish(it)
        }

        expectedDataWithContext = ""

        publishedDataWithContext.forEach {
            expectedDataWithContext = it
            stringProducerContext2.publish(it)
        }

        Assert.assertEquals(publishedData.size * 4, totalReceived)
        Assert.assertEquals(publishedDataWithContext.size * 4, totalReceivedWithContext)

        Ether.clear()
        Ether.clear(context2)
    }

    @Test
    fun testClear() {
        val stringProducer1 = Ether.publisherOf(String::class.java)
        val stringProducer2 = Ether.publisherOf(String::class.java)

        val stringSubscriber1 = Ether.subscriberOf(String::class.java)
        val stringSubscriber2 = Ether.subscriberOf(String::class.java)

        var expectingData = true
        var totalReceived = 0

        stringSubscriber1.subscribe(IDataSubscriber {
            Assert.assertTrue(expectingData)
            totalReceived++
        })
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