package io.domino.lab.reflection;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ClasspathScanner {

    private static final Logger logger = LogManager.getLogger(ClasspathScanner.class);
    private ClasspathScanner(){

    }

    public static <T> Set<Class<? extends T>> scanByImplementedInterface(Class<T> implementedInterface, String... basePackages) {

        logger.debug("Scanning for classes implementing: {} in packages: {}",implementedInterface, basePackages);

        final Set<Class<? extends T>> results = new LinkedHashSet<>();

        for (String basePackage : basePackages) {
            new FastClasspathScanner(basePackage)
                    .verbose(logger.isDebugEnabled())
                    .matchClassesImplementing(implementedInterface, scannedClass -> {
                        if (!Modifier.isAbstract(scannedClass.getModifiers())) {
                            results.add(scannedClass);
                        }
                    })
                    .scan();

        }
        return results;
    }


    public static <T> Set<Class<? extends T>> scanByExtendedClass(Class<T> baseClass, String... basePackages) {

        logger.debug("Scanning for classes extending: {} in packages: {}",baseClass, basePackages);

        final Set<Class<? extends T>> results = new LinkedHashSet<>();

        for (String basePackage : basePackages) {
            new FastClasspathScanner(basePackage)
                    .verbose(logger.isDebugEnabled())
                    .matchSubclassesOf(baseClass, scannedClass -> {
                        if (!Modifier.isAbstract(scannedClass.getModifiers())) {
                            results.add(scannedClass);
                        }
                    })
                    .scan();

        }
        return results;
    }

    public static <A> Set<Class<?>> scanByAnnotation(Class<A> markerAnnotation, String... basePackages) {

        logger.debug("Scanning for classes annotated with: {} in packages: {}",markerAnnotation, basePackages);

        final Set<Class<?>> results = new LinkedHashSet<>();

        for (String basePackage : basePackages) {
            new FastClasspathScanner(basePackage)
                    .verbose(logger.isDebugEnabled())
                    .matchClassesWithAnnotation(markerAnnotation, scannedClass -> {
                        if (!Modifier.isAbstract(scannedClass.getModifiers())) {
                            results.add(scannedClass);
                        }
                    })
                    .scan();

        }
        return results;
    }
}
