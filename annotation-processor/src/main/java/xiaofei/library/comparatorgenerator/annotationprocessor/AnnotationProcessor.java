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

import java.io.IOException;
import java.io.Writer;
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
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import xiaofei.library.comparatorgenerator.Criterion;
import xiaofei.library.comparatorgenerator.Order;

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
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() > 1) {
            messager.printMessage(Diagnostic.Kind.ERROR, "The size of the annotation set is larger than 1.");
            return false;
        }
        if (annotations.isEmpty()) {
            return false;
        }
        try {
            JavaFileObject javaFileObject = filer.createSourceFile("xiaofei.library.comparatorgenerator.CriterionManager");
            Writer writer = javaFileObject.openWriter();
            writeFileBeginning(writer);
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Criterion.class);
            for (Element element : elements) {
                switch (element.getKind()) {
                    case METHOD:
                        if (element instanceof ExecutableElement) {
                            ExecutableElement executableElement = (ExecutableElement) element;
                            if (executableElement.getParameters().size() == 0) {
                                String className = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
                                String methodName = element.getSimpleName().toString();
                                Criterion annotation = element.getAnnotation(Criterion.class);
                                Order order = annotation.order();
                                int priority = annotation.priority();
                                writer.write("        putCriterionForMethod(" + className + ".class, \"" + methodName + "\", " + priority + ", Order." + order +");\n");
                            } else {
                                messager.printMessage(Diagnostic.Kind.ERROR, "ERROR1");
                            }
                        } else {
                            messager.printMessage(Diagnostic.Kind.ERROR, "ERROR2");
                        }
                        break;
                    case FIELD:
                        if (element instanceof VariableElement) {
                            String className = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
                            String fieldName = element.getSimpleName().toString();
                            Criterion annotation = element.getAnnotation(Criterion.class);
                            Order order = annotation.order();
                            int priority = annotation.priority();
                            writer.write("        putCriterionForField(" + className + ".class, \"" + fieldName + "\", " + priority + ", Order." + order +");\n");
                        } else {
                            messager.printMessage(Diagnostic.Kind.ERROR, "ERROR4");
                        }
                        break;
                    default:
                        messager.printMessage(Diagnostic.Kind.ERROR, "This is a strange annotated element.");
                        break;
                }
            }
            writeFileEnd(writer);
            writer.close();
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "IO error!");
            return false;
        }
        return true;
    }

    private void writeFileBeginning(Writer writer) throws IOException {
        writer.write("package xiaofei.library.comparatorgenerator;\n\n");
        writer.write("import java.util.concurrent.ConcurrentHashMap;\n\n");
        writer.write("import xiaofei.library.comparatorgenerator.Criterion;\n");
        writer.write("import xiaofei.library.comparatorgenerator.Order;\n");
        writer.write("import xiaofei.library.comparatorgenerator.internal.FieldMember;\n");
        writer.write("import xiaofei.library.comparatorgenerator.internal.MethodMember;\n");
        writer.write("import xiaofei.library.comparatorgenerator.internal.SortingCriterion;\n\n");
        writer.write("public class CriterionManager {\n\n");
        writer.write("    private static ConcurrentHashMap<Class<?>, ConcurrentHashMap<Integer, SortingCriterion>> maps = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<Integer, SortingCriterion>>();\n\n");
        writer.write("    static {\n");
    }

    private void writeFileEnd(Writer writer) throws IOException {
        writer.write("    }\n\n");
        writer.write("    private static void putCriterionForMethod(Class<?> clazz, String methodName, int priority, Order order) {\n");
        writer.write("        ConcurrentHashMap<Integer, SortingCriterion> map = maps.putIfAbsent(clazz, new ConcurrentHashMap<Integer, SortingCriterion>());\n");
        writer.write("        map.put(priority, new SortingCriterion(new MethodMember(clazz, methodName), order));\n");
        writer.write("    }\n\n");
        writer.write("    private static void putCriterionForField(Class<?> clazz, String fieldName, int priority, Order order) {\n");
        writer.write("        ConcurrentHashMap<Integer, SortingCriterion> map = maps.putIfAbsent(clazz, new ConcurrentHashMap<Integer, SortingCriterion>());\n");
        writer.write("        map.put(priority, new SortingCriterion(new FieldMember(clazz, fieldName), order));\n");
        writer.write("    }\n\n");
        writer.write("    public static ConcurrentHashMap<Integer, SortingCriterion> getCriteriaIn(Class<?> clazz) {\n");
        writer.write("        return maps.get(clazz);\n");
        writer.write("    }\n\n");
        writer.write("}\n");
    }
}
