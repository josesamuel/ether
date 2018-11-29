package arch.ether


/**
 * Ether is a PubSub data bus where data can be published and subscribed.
 *
 * @see [publisherOf]
 * @see [subscriberOf]
 *
 * @author js
 */
open class Ether<T> protected constructor() : IDataPublisher<T>, IDataObservable<T> {

    private val subscribers = mutableSetOf<IDataSubscriber<T>>()
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


    @Suppress("UNCHECKED_CAST")
    companion object {

        private val pubSubDataBuses = mutableMapOf<Any, Ether<*>>()

        /**
         * Returns an [Ether] for Class of type [T]
         */
        private fun <T> of(type: Class<T>): Ether<T> {
            synchronized(pubSubDataBuses) {
                var dataBus: Ether<T>? = pubSubDataBuses[type] as Ether<T>?
                if (dataBus == null) {
                    dataBus = Ether()
                    pubSubDataBuses[type] = dataBus
                }
                return dataBus
            }
        }

        /**
         * Returns an [IDataPublisher] for type [T]
         */
        @JvmStatic
        fun <T : Any> publisherOf(type: Class<T>): IDataPublisher<T> = of(type)

        /**
         * Returns an [IDataObservable] for type [T]
         */
        @JvmStatic
        fun <T : Any> subscriberOf(type: Class<T>): IDataObservable<T> = of(type)

        /**
         * Clear all the publishers and subscribers
         */
        @JvmStatic
        fun clear() {
            pubSubDataBuses.values.forEach {
                it.close()
            }
            pubSubDataBuses.clear()
        }
    }


}