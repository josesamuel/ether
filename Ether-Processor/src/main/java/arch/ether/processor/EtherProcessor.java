package arch.ether.processor;


import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import arch.ether.annotation.EtherData;
import arch.ether.processor.builder.BindingManager;

/**
 * Process the annotations for [EtherData]
 *
 * @author js
 */
@AutoService(Processor.class)
public class EtherProcessor extends AbstractProcessor {

    private BindingManager bindingManager;
    private Messager messager;


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.messager = env.getMessager();
        bindingManager = new BindingManager(env);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(EtherData.class);
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(EtherData.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                bindingManager.buildPubSubClasses(element);
            }
        }
        return false;
    }

    /**
     * Prints a debug message
     */
    protected void printMessage(String message) {
        messager.printMessage(Diagnostic.Kind.WARNING, message);
    }
}
