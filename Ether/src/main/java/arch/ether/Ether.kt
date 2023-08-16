package arch.ether

import java.util.*
import java.util.concurrent.ConcurrentHashMap


/**
 * Ether is a PubSub data bus where data can be published and subscribed.
 *
 * @see [publisherOf]
 * @see [subscriberOf]
 *
 * @author js
 */
open class Ether<T> protected constructor() : IDataPublisher<T>, IDataObservable<T> {

    private val subscribers:MutableSet<IDataSubscriber<T>> = Collections.newSetFromMap(ConcurrentHashMap())
    private var data: T? = null
    private var initialized = false

    /**
     * Publishes the data of type [T] to [Ether]
     */
    override fun publish(data: T) {
        this.data = data
        initialized = true
        subscribers.forEach {
            try {
                it.onData(data)
            } catch (ex: Exception) {
            }
        }
    }

    /**
     * Subscribe for the data of type [T] send to [Ether]
     */
    override fun subscribe(subscriber: IDataSubscriber<T>) {
        subscribers.add(subscriber)
        if (initialized) {
            subscriber.onData(data)
        }

    }

    /**
     * Removes the subscription if any
     */
    override fun unsubscribe(subscriber: IDataSubscriber<T>) {
        subscribers.remove(subscriber)
    }

    /**
     * Close the ether. removes the subscribers
     */
    private fun close() {
        data = null
        subscribers.clear()
        initialized = false
    }

    override fun getCurrentData() = data


    @Suppress("UNCHECKED_CAST")
    companion object {

        private val etherMap = ConcurrentHashMap<EtherContext, MutableMap<Any, Ether<*>>>()

        /**
         * Returns an [Ether] for Class of type [T]
         */
        private fun <T> of(type: Class<T>, context: EtherContext = GLOBAL_ETHER_CONTEXT): Ether<T> {
            synchronized(etherMap) {
                var pubSubDataBuses: MutableMap<Any, Ether<*>>? = etherMap[context]
                if (pubSubDataBuses == null) {
                    pubSubDataBuses = mutableMapOf()
                    etherMap[context] = pubSubDataBuses
                }
                synchronized(pubSubDataBuses) {
                    var dataBus: Ether<T>? = pubSubDataBuses[type] as Ether<T>?
                    if (dataBus == null) {
                        dataBus = Ether()
                        pubSubDataBuses[type] = dataBus
                    }
                    return dataBus
                }
            }
        }

        /**
         * Returns an [IDataPublisher] for type [T]
         *
         * @param type The [Class] of the type this Ether publishes
         * @param context The [EtherContext] where this exist
         */
        @JvmStatic
        fun <T : Any> publisherOf(type: Class<T>, context: EtherContext = GLOBAL_ETHER_CONTEXT): IDataPublisher<T> = of(type, context)

        /**
         * Returns an [IDataObservable] for type [T]
         *
         * @param type The [Class] of the type this Ether emits
         * @param context The [EtherContext] where this exist
         */
        @JvmStatic
        fun <T : Any> subscriberOf(type: Class<T>, context: EtherContext = GLOBAL_ETHER_CONTEXT): IDataObservable<T> = of(type, context)

        /**
         * Clear all the publishers and subscribers
         *
         * @param context The [EtherContext] where this exist
         */
        @JvmStatic
        fun clear(context: EtherContext = GLOBAL_ETHER_CONTEXT) {
            synchronized(etherMap) {
                val pubSubDataBuses: MutableMap<Any, Ether<*>>? = etherMap[context]
                if (pubSubDataBuses != null) {
                    pubSubDataBuses.values.forEach {
                        it.close()
                    }
                    pubSubDataBuses.clear()
                }
                etherMap.remove(context)
            }
        }
    }

}