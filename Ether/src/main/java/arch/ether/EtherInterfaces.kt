package arch.ether


/**
 * Represents a generic producer
 */
interface IProducer {

    /**
     * Called to initialize
     */
    fun init() {}

    /**
     * Called to destroy
     */
    fun destroy() {}
}


/**
 * Interface to publish a data of type [T] to the [Ether]
 */
interface IDataPublisher<T> {

    /**
     * Publish the data to the [Ether]
     */
    fun publish(data: T)
}

/**
 * An observable that can be used to subscribe to new data of type [T] from the [Ether]
 */
interface IDataObservable<T> {

    /**
     * Subscribes a [IDataSubscriber] to receive new data
     */
    fun subscribe(subscriber: IDataSubscriber<T>)

    /**
     * Un subscribe the given subscriber
     */
    fun unsubscribe(subscriber: IDataSubscriber<T>)
}


/**
 * A context to which the [Ether] is created with
 */
interface EtherContext

/**
 * Default context in which Ether is created if none specified
 **/
val GLOBAL_ETHER_CONTEXT = object : EtherContext {}
