package arch.ether.processor.builder


import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

/**
 * Builds the abstract data publisher classes
 */
open class BindingManager(private val processingEnvironment: ProcessingEnvironment) {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    /**
     * Generates the abstract publisher class
     */
    fun buildPubSubClasses(element: Element) {
        val kaptKotlinGeneratedDir = processingEnvironment.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.replace("kaptKotlin", "kapt")
        EtherStubsBuilder(element, this).build().writeTo(File(kaptKotlinGeneratedDir))
    }
}
