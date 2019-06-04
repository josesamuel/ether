package arch.ether.processor.builder

import arch.ether.*
import arch.ether.annotation.EtherData
import com.squareup.kotlinpoet.*
import io.reactivex.Flowable
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * Builds the DataPublisher for the class marked as [EtherData]
 */
internal class EtherObservableStubsBuilder(element: Element, bindingManager: BindingManager) : EtherBuilder(element, bindingManager) {


    val observablePublisherClassName = "${className}Producer"

    /**
     * Returns the file spec to generate
     */
    fun build(): FileSpec {


        val fileSpecBuilder = FileSpec.builder(packageName, observablePublisherClassName)

        var triggerType: TypeMirror? = null
        try {
            element.getAnnotation(EtherData::class.java).triggerType
        } catch (ex: MirroredTypeException) {
            triggerType = ex.typeMirror
        }

        if (triggerType != null) {
            val producerBuilder = TypeSpec.classBuilder(observablePublisherClassName)
                    .addKdoc("Publisher of [%T]\nExtend this class to generate [%T] based on the triggers to [onPublisherTrigger]\n\n" +
                            "@param context Specify the  [%T] assosiated with this\n", ClassName(packageName, className), ClassName(packageName, className), EtherContext::class)
                    .addSuperinterface(
                            ClassName(IDataProducer::class.java.`package`.name, IDataProducer::class.java.simpleName).parameterizedBy(
                                    ClassName.bestGuess(className),
                                    triggerType.asTypeName()
                            ))
                    .primaryConstructor(FunSpec.constructorBuilder()
                            .addParameter(ParameterSpec.builder("context", EtherContext::class, KModifier.PRIVATE).defaultValue("arch.ether.GLOBAL_ETHER_CONTEXT").build()).build())
                    .addProperty(PropertySpec.builder("context", EtherContext::class, KModifier.PRIVATE).initializer("context").build())
                    .addSuperinterface(IProducer::class)
                    .addProperty(PropertySpec.builder("publisher",
                            ClassName(IDataPublisher::class.java.`package`.name, IDataPublisher::class.java.simpleName).parameterizedBy(
                                    ClassName.bestGuess(className)
                            ),
                            KModifier.PRIVATE)
                            .initializer("%T.publisherOf<%T>(%T::class.java, context)", Ether::class, ClassName(packageName, className), ClassName(packageName, className))
                            .build()
                    )

                    .addFunction(FunSpec.builder("triggerObservable")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(
                                    ClassName(Flowable::class.java.`package`.name, Flowable::class.java.simpleName).parameterizedBy(
                                            triggerType.asTypeName()
                                    ))
                            .addStatement("return Ether.observableOf(%T::class.java, context)", triggerType.asTypeName())
                            .addKdoc("Sends the data to the [Ether]\n")
                            .build())

                    .addFunction(FunSpec.builder("publish")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("data", ClassName.bestGuess(className))
                            .addStatement("publisher.publish(data)")
                            .addKdoc("Override to handle the production triggers.\nIf any data is produced, use [publish] to publish them\n")
                            .build())



            fileSpecBuilder.addImport("arch.ether", "observableOf").addType(producerBuilder.build())

        }

        return fileSpecBuilder.build()
    }

}