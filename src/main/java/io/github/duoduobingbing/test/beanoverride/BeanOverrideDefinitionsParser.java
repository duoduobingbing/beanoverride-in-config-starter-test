package io.github.duoduobingbing.test.beanoverride;

import io.github.duoduobingbing.test.beanoverride.definition.BeanOverrideSpecificAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.test.context.bean.override.BeanOverride;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.test.context.bean.override.BeanOverrideProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


/**
 * Class that is responsible for parsing &amp; finding {@link BeanOverride @BeanOverride} annotations in {@link Configuration @Configuration} classes.
 *
 * @see org.springframework.boot.test.mock.mockito.DefinitionsParser (deprecated but present in Spring Boot 3.4)
 * @see org.springframework.test.context.bean.override.BeanOverrideHandler#processElement(AnnotatedElement, Class, BiConsumer)
 */
public class BeanOverrideDefinitionsParser {

    private static final Logger logger = LoggerFactory.getLogger(BeanOverrideDefinitionsParser.class);

    /**
     * Class that contains all found BeanOverrideHandlers (both for class-level and field-level).
     * @param classLevelOverrideHandlers
     * @param fieldBeanOverrideHandlerMap
     */
    public static record BeanOverrideHandlersPOJO(
            List<BeanOverrideHandler> classLevelOverrideHandlers,
            Map<Field, BeanOverrideHandler> fieldBeanOverrideHandlerMap
    ) {

        public BeanOverrideHandlersPOJO() {
            this(new ArrayList<>(), new LinkedHashMap<>());
        }

        public BeanOverrideHandlersPOJO(Map<Field, BeanOverrideHandler> fieldBeanOverrideHandlerMap) {
            this(new ArrayList<>(), fieldBeanOverrideHandlerMap);
        }

        public BeanOverrideHandlersPOJO(List<BeanOverrideHandler> classLevelOverrideHandlers) {
            this(classLevelOverrideHandlers, new LinkedHashMap<>());
        }

        public void addAll(BeanOverrideHandlersPOJO beanOverrideHandlersPOJO) {
            this.classLevelOverrideHandlers.addAll(beanOverrideHandlersPOJO.classLevelOverrideHandlers);
            this.fieldBeanOverrideHandlerMap.putAll(beanOverrideHandlersPOJO.fieldBeanOverrideHandlerMap);
        }
    }


    public BeanOverrideHandlersPOJO parse(Class<?> source, Class<?> testClass) {

        BeanOverrideHandlersPOJO handlers = parseElement(source, null, testClass);


        ReflectionUtils.doWithFields(
                source,
                (element) -> {
                    BeanOverrideHandlersPOJO results = parseElement(element, source, testClass);
                    handlers.addAll(results);
                }
        );

        return handlers;
    }

    private BeanOverrideHandlersPOJO parseElement(AnnotatedElement element, Class<?> source, Class<?> testClass) {

        MergedAnnotations annotations = MergedAnnotations.from(element, SearchStrategy.SUPERCLASS);

        BeanOverrideHandlersPOJO allHandlers = new BeanOverrideHandlersPOJO();

        LinkedList<MergedAnnotation<BeanOverride>> foundMergedAnnotations = annotations
                .stream(BeanOverride.class)
                .sorted(reversedMetaDistance)
                .collect(Collectors.toCollection(LinkedList::new));

        for (MergedAnnotation<BeanOverride> mergedAnnotation : foundMergedAnnotations) {
            MergedAnnotation<?> metaSource = mergedAnnotation.getMetaSource();
            Assert.notNull(metaSource, "@BeanOverride annotation must be meta-present");
            BeanOverride beanOverrideAnnotation = mergedAnnotation.synthesize();
            Annotation composedAnnotation = metaSource.synthesize();
            logger.debug("[BOINC] {}", composedAnnotation);
            BeanOverrideHandlersPOJO results = parseBeanOverrideAnnotation(composedAnnotation, beanOverrideAnnotation, element, source, testClass);

            allHandlers.addAll(results);

        }

//        annotations.stream(MockitoBean.class)
//                .map(MergedAnnotation::synthesize)
//                .forEach((annotation) -> parseMockBeanAnnotation(annotation, element, source, handlers));
//
//        annotations.stream(MockitoSpyBean.class)
//                .map(MergedAnnotation::synthesize)
//                .forEach((annotation) -> parseSpyBeanAnnotation(annotation, element, source, handlers));

        return allHandlers;
    }

    private BeanOverrideHandlersPOJO parseBeanOverrideAnnotation(Annotation annotation, BeanOverride beanOverrideAnnotation, AnnotatedElement element, Class<?> source, Class<?> testClass) {

//        Class<?>[] valueClasses = beanOverrideSpecificAnnotationOpt.map(BeanOverrideSpecificAnnotation::getTypes).orElse(null);
//        Set<ResolvableType> affectedTypes = getOrDeduceTypes(element, valueClasses, source);

//        Assert.state(!affectedTypes.isEmpty(), () -> "Unable to deduce type to mock from " + element);

//        String name = beanOverrideSpecificAnnotationOpt.map(BeanOverrideSpecificAnnotation::getName).orElse(null);

//        if (StringUtils.hasLength(name)) {
//            Assert.state(affectedTypes.size() == 1, "The name attribute can only be used when mocking/spying a single class");
//        }

        final BeanOverrideHandler beanOverrideHandler;
        final Field beanOverrideField;
        if (element instanceof Field field) {
            beanOverrideField = field;
            beanOverrideHandler = createHandlerFromField(field, annotation, beanOverrideAnnotation, source, testClass);
            return new BeanOverrideHandlersPOJO(Map.ofEntries(Map.entry(beanOverrideField, beanOverrideHandler)));
        } else if (element instanceof Class<?> clazz) {
//            Optional<Class<?>[]> typesForClassElement = getTypesForClassElement(clazz, annotation);
//
//
//            if(typesForClassElement.isEmpty()) {
//                return new BeanOverrideHandlersPOJO();
//            }
//
//            Set<ResolvableType> types = new LinkedHashSet<>();
//            for (Class<?> type : typesForClassElement.orElseThrow()) {
//                types.add(ResolvableType.forClass(type));
//            }

//            Assert.notEmpty(types, "If declared on a class types cannot be empty");

            List<BeanOverrideHandler> handlers = createHandlersFromClass(clazz, annotation, beanOverrideAnnotation, source, testClass);

            return new BeanOverrideHandlersPOJO(handlers);
        } else {
            logger.info("[BOINC] Was neither field nor class: {} (skipping...)", element);
        }

//        for (ResolvableType typeToMock : affectedTypes) {
//            System.out.printf("affectedTypes: anno.name: %s, %s%n", name, typeToMock);
//
//        }

        return new BeanOverrideHandlersPOJO();


    }

//    Optional<Class<?>[]> getTypesForClassElement(Class<?> classElement, Annotation annotation) {
//        Optional<BeanOverrideSpecificAnnotation<Annotation>> beanOverrideSpecificAnnotationOpt = BeanOverrideSpecificAnnotation.of(annotation);
//
//        if (beanOverrideSpecificAnnotationOpt.isPresent()) { //was either @MockitoBean or @MockitoSpyBean
//            return Optional.of(beanOverrideSpecificAnnotationOpt.orElseThrow().getTypes());
//        }
//
//        //Look if it is annotated with @BeanOverrideClassLevel
//        MergedAnnotations mergedAnnotations = MergedAnnotations.search(SearchStrategy.DIRECT).from(classElement);
//        final MergedAnnotation<BeanOverrideClassLevel> mergedAnnotation = mergedAnnotations.get(BeanOverrideClassLevel.class);
//        final Optional<BeanOverrideClassLevel> beanOverrideClassLevelAnnotationOpt = mergedAnnotation.synthesize(MergedAnnotation::isPresent);
//
//        if (beanOverrideClassLevelAnnotationOpt.isEmpty()) {
//            return Optional.empty();
//        }
//
//
//        return Optional.of(beanOverrideClassLevelAnnotationOpt.orElseThrow().types());
//    }


    private static final Comparator<MergedAnnotation<? extends Annotation>> reversedMetaDistance = Comparator.<MergedAnnotation<? extends Annotation>>comparingInt(MergedAnnotation::getDistance).reversed();

    private static BeanOverrideProcessor createProcessorFromAnnotation(AnnotatedElement element, Annotation composedAnnotation, BeanOverride beanOverrideAnnotation) {
        BeanOverrideProcessor processor = BeanUtils.instantiateClass(beanOverrideAnnotation.value());
        return processor;
    }

    private static BeanOverrideHandler createHandlerFromField(Field field, Annotation composedAnnotation, BeanOverride beanOverrideAnnotation, Class<?> source, Class<?> testClass) {

        BeanOverrideProcessor processor = createProcessorFromAnnotation(field, composedAnnotation, beanOverrideAnnotation);

        Assert.state(!Modifier.isStatic(field.getModifiers()), () -> "@BeanOverride field must not be static: " + field);

        Optional<BeanOverrideSpecificAnnotation<Annotation>> beanOverrideSpecificAnnotationOpt = BeanOverrideSpecificAnnotation.of(composedAnnotation);

        if (beanOverrideSpecificAnnotationOpt.isEmpty()) {
            return processor.createHandler(composedAnnotation, source, field);
        } else {
            //In case this is a known annotation like @MockitoSpyBean or @Mockito
            //we do not use BeanOverrideProcessor.createHandler(..) which would be MockitoBeanOverrideProcessor.createHandler(..)
            //but instead create the BeanOverrideHandler ourselves because we now have full control over the ResolvableType parameter
            //that goes into the constructor of these two.

            //Logic is taken from the original org.springframework.boot.test.mock.mockito.DefinitionsParse#getOrDeduceTypes(...) (Present in Spring Boot 3.4.4)
            ResolvableType rt = (field.getGenericType() instanceof TypeVariable) ? ResolvableType.forField(field, source) : ResolvableType.forField(field);
            return beanOverrideSpecificAnnotationOpt.orElseThrow().createBeanOverrideHandler(rt, testClass, field);
        }


    }

    private static List<BeanOverrideHandler> createHandlersFromClass(Class<?> element, Annotation composedAnnotation, BeanOverride beanOverrideAnnotation, Class<?> source, Class<?> testClass) {
        BeanOverrideProcessor processor = createProcessorFromAnnotation(element, composedAnnotation, beanOverrideAnnotation);

        return processor.createHandlers(composedAnnotation, testClass);
    }


}
