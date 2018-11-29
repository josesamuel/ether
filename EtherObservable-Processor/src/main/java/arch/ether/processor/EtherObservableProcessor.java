package arch.ether.processor;


import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import arch.ether.annotation.EtherData;
import arch.ether.processor.builder.ObservableBindingManager;

/**
 * Process the annotations for [EtherData]
 *
 * @author js
 */
@AutoService(Processor.class)
public class EtherObservableProcessor extends EtherProcessor {

    private ObservableBindingManager bindingManager;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        bindingManager = new ObservableBindingManager(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(EtherData.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                bindingManager.buildPubSubObservableClasses(element);
            }
        }
        return false;
    }
}
