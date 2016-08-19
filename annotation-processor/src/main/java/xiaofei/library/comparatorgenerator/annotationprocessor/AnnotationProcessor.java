/**
 *
 * Copyright 2016 Xiaofei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package xiaofei.library.comparatorgenerator.annotationprocessor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;

import xiaofei.library.comparatorgenerator.Criterion;

/**
 * Created by Xiaofei on 16/8/19.
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
                    if (element instanceof ExecutableElement) {
                        ExecutableElement executableElement = (ExecutableElement) element;
                        messager.printMessage(Diagnostic.Kind.NOTE, executableElement.getReturnType().toString());//.equals("java.lang.String")
                        if (executableElement.getParameters().size() == 0) {
                            messager.printMessage(Diagnostic.Kind.NOTE, "Method in " + ((TypeElement) element.getEnclosingElement()).getQualifiedName());
                            messager.printMessage(Diagnostic.Kind.NOTE, "Name " + element.getSimpleName());
                            Criterion annotation = element.getAnnotation(Criterion.class);
                            messager.printMessage(Diagnostic.Kind.NOTE, "Order = " + annotation.order() + " priority " + annotation.priority());
                        } else {
                            messager.printMessage(Diagnostic.Kind.ERROR, "ERROR1");
                        }
                    } else {
                        messager.printMessage(Diagnostic.Kind.ERROR, "ERROR2");
                    }
                    break;
                case FIELD:
                    if (element instanceof VariableElement) {
                        messager.printMessage(Diagnostic.Kind.NOTE, "Field in " + ((TypeElement) element.getEnclosingElement()).getQualifiedName());
                        messager.printMessage(Diagnostic.Kind.NOTE, "Name " + element.getSimpleName());
                        Criterion annotation = element.getAnnotation(Criterion.class);
                        messager.printMessage(Diagnostic.Kind.NOTE, "Order = " + annotation.order() + " priority " + annotation.priority());
                        messager.printMessage(Diagnostic.Kind.NOTE, "type = " + ((VariableElement) element).asType().toString());
                    } else {
                        messager.printMessage(Diagnostic.Kind.ERROR, "ERROR4");
                    }
                    break;
                default:
                    messager.printMessage(Diagnostic.Kind.ERROR, "This is a strange annotated element.");
                    break;
            }
        }
        return true;
    }
}
