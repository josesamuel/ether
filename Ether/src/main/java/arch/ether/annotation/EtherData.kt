package arch.ether.annotation

import kotlin.reflect.KClass

/**
 * Marks a class that can be published and subscribed through ether
 *
 * @param triggerType The base class of different triggers for the producer of this data
 */
@Target(AnnotationTarget.CLASS)
annotation class EtherData(val triggerType: KClass<*>)