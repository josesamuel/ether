package arch.ether

import arch.ether.EtherObservable.Companion.observableOf
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

/**
 * An extension of [Ether] that also exposes a [Flowable] for the data send to this [Ether]
 *
 * @see [observableOf]
 */
class EtherObservable<T> private constructor() : Ether<T>() {

    companion object {
        @JvmStatic
        fun <T : Any> observableOf(type: Class<T>, context: EtherContext = GLOBAL_ETHER_CONTEXT): Flowable<T> {
            val subscriber = subscriberOf(type, context)
            val publishSubject = PublishSubject.create<T>()
            val dataListener = IDataSubscriber<T> { publishSubject.onNext(it) }
            return publishSubject.doOnSubscribe { subscriber.subscribe(dataListener) }
                    .doOnDispose { subscriber.unsubscribe(dataListener) }
                    .toFlowable(BackpressureStrategy.BUFFER)
        }
    }
}

/**
 * Returns a [Flowable] of type [T] to listen for new data of [T] that are published
 */
fun <T : Any> Ether.Companion.observableOf(type: Class<T>, context: EtherContext = GLOBAL_ETHER_CONTEXT) = EtherObservable.observableOf(type, context)