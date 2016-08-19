package xiaofei.library.comparatorgenerator.annotationprocessor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import xiaofei.library.comparatorgenerator.Criterion;

/**
 * Created by Eric on 16/8/19.
 */
public class AnnotationProcessor extends AbstractProcessor {

    private Messager messager;

    private Filer filer;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new HashSet<String>();
        result.add(Criterion.class.getCanonicalName());
        return result;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() != 1) {
            messager.printMessage(Diagnostic.Kind.WARNING, "The size of the annotation set is not 1.");
        }
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Criterion.class);
        for (Element element : elements) {
            switch (element.getKind()) {
                case METHOD:

                case FIELD:
                default:
                    messager.printMessage(Diagnostic.Kind.ERROR, "This is a strange annotated element.");
                    break;
            }
        }
        return true;
    }
}
