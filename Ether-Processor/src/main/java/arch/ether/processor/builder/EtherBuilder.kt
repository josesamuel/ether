package arch.ether.processor.builder

import com.google.auto.common.MoreElements.getPackage
import javax.lang.model.element.Element

/**
 * Base class of all the builders
 */
open class EtherBuilder(val element: Element, val bindingManager: BindingManager) {

    /**Package name of the data class*/
    val packageName = getPackage(element).qualifiedName.toString()
    /**Data class class*/
    val className = element.simpleName.toString()
    val fileName = "Abstract${className}Publisher"

    val producerClassName = "Abstract${className}Publisher"

}