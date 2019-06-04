package arch.ether.processor.builder

import arch.ether.*
import arch.ether.annotation.EtherData
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * Builds the DataPublisher for the class marked as [EtherData]
 */
internal class EtherStubsBuilder(element: Element, bindingManager: BindingManager) : EtherBuilder(element, bindingManager) {

    /**
     * Returns the file spec to generate
     */
    fun build(): FileSpec {


        val fileSpecBuilder = FileSpec.builder(packageName, fileName)

        var triggerType: TypeMirror? = null
        try {
            element.getAnnotation(EtherData::class.java).triggerType
        } catch (ex: MirroredTypeException) {
            triggerType = ex.typeMirror
        }

        if (triggerType != null) {
            val producerBuilder = TypeSpec.classBuilder(producerClassName)
                    .addKdoc("Publisher of [%T]\nExtend this class to generate [%T] based on the triggers to [onPublisherTrigger]\n\n@param context Specify the  [%T] assosiated with this\n", ClassName(packageName, className), ClassName(packageName, className), EtherContext::class)
                    .addModifiers(KModifier.ABSTRACT)
                    .primaryConstructor(FunSpec.constructorBuilder()
                            .addParameter(ParameterSpec.builder("context", EtherContext::class).defaultValue("arch.ether.GLOBAL_ETHER_CONTEXT").build()).build())
                    .addSuperinterface(ClassName(IDataPublisher::class.java.`package`.name, IDataPublisher::class.java.simpleName).parameterizedBy(ClassName.bestGuess(className)))
                    .addSuperinterface(IProducer::class)
                    .addProperty(PropertySpec.builder("publisher",
                            ClassName(IDataPublisher::class.java.`package`.name, IDataPublisher::class.java.simpleName).parameterizedBy(
                                    ClassName.bestGuess(className))
                            ,
                            KModifier.PRIVATE)
                            .initializer("%T.publisherOf<%T>(%T::class.java, context)", Ether::class, ClassName(packageName, className), ClassName(packageName, className))
                            .build()
                    )
                    .addInitializerBlock(CodeBlock.of("Ether.subscriberOf<%T>(%T::class.java, context).subscribe(%T(::onPublisherTrigger))\n", triggerType.asTypeName(), triggerType.asTypeName(), IDataSubscriber::class))

                    .addFunction(FunSpec.builder("publish")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("data", ClassName.bestGuess(className))
                            .addCode("publisher.publish(data)\n")
                            .addKdoc("Sends the data to the [Ether]\n")
                            .build())

                    .addFunction(FunSpec.builder("onPublisherTrigger")
                            .addModifiers(KModifier.ABSTRACT)
                            .addParameter("trigger", triggerType.asTypeName())
                            .addKdoc("Override to handle the production triggers.\nIf any data is produced, use [publish] to publish them\n")
                            .build())

            fileSpecBuilder.addType(producerBuilder.build())

        }

        return fileSpecBuilder.build()
    }

}