package arch.ether

import io.reactivex.Flowable

/**
 * An interface that exposes [IDataPublisher] of type [T] and
 * a [Flowable] to listen for triggers of type [U]
 */
interface IDataProducer<T, U> : IDataPublisher<T> {

    /**
     * Observe for the triggers of type [U]
     */
    fun triggerObservable(): Flowable<U>

}