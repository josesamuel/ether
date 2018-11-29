package arch.ether.processor.builder


import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

/**
 * Builds the observable producer class
 */
class ObservableBindingManager(private val processingEnvironment: ProcessingEnvironment) : BindingManager(processingEnvironment) {


    /**
     * Builds the observable producer class
     */
    fun buildPubSubObservableClasses(element: Element) {
        val kaptKotlinGeneratedDir = processingEnvironment.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.replace("kaptKotlin", "kapt")
        EtherObservableStubsBuilder(element, this).build().writeTo(File(kaptKotlinGeneratedDir))
    }
}
