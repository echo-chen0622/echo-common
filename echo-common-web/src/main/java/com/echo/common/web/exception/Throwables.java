package com.echo.common.web.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

public class Throwables {
    private static final AnalyzerBuilder defaultAnalyzerBuilder = new AnalyzerBuilder();

    public Throwables() {
    }

    public static AnalyzerBuilder builder() {
        return new AnalyzerBuilder();
    }

    public static AnalyzerBuilder builder(Class<? extends Throwable> throwableType, Function<Throwable, Throwable> extractor) {
        return (new AnalyzerBuilder()).registerExtractor(throwableType, extractor);
    }

    public static Throwable[] causes(Throwable throwable) {
        return defaultAnalyzerBuilder.causes(throwable);
    }

    public static Analyzer analyzer(Throwable throwable) {
        return defaultAnalyzerBuilder.build(throwable);
    }

    public static Throwable getFirstThrowableOfType(Throwable throwable, Class<? extends Throwable> throwableType) {
        return analyzer(throwable).getFirstThrowableOfType(throwableType);
    }

    public static Throwable getRootCause(Throwable throwable) {
        return analyzer(throwable).getRootCause();
    }

    public static class AnalyzerBuilder {
        public static final Function<Throwable, Throwable> DEFAULT_EXTRACTOR = (e) -> {
            return e.getCause();
        };
        public static final Function<Throwable, Throwable> INVOCATIONTARGET_EXTRACTOR = (e) -> {
            return ((InvocationTargetException) e).getTargetException();
        };
        private final Map<Class<? extends Throwable>, Function<Throwable, Throwable>> extractorMap = new TreeMap((e1, e2) -> {
            if (e1.getClass().isAssignableFrom(e2.getClass())) {
                return 1;
            } else {
                return e2.getClass().isAssignableFrom(e1.getClass()) ? -1 : e1.getClass().getName().compareTo(e2.getClass().getName());
            }
        });

        public AnalyzerBuilder() {
            this.registerExtractor(InvocationTargetException.class, INVOCATIONTARGET_EXTRACTOR);
            this.registerExtractor(Throwable.class, DEFAULT_EXTRACTOR);
        }

        public final AnalyzerBuilder registerExtractor(Class<? extends Throwable> throwableType, Function<Throwable, Throwable> extractor) {
            this.extractorMap.put(throwableType, extractor);
            return this;
        }

        public final Throwable[] causes(Throwable throwable) {
            if (throwable == null) {
                throw new IllegalArgumentException("Invalid throwable: null");
            } else {
                List<Throwable> chain = new ArrayList();
                Set extractors = this.extractorMap.entrySet();

                while (throwable != null) {
                    chain.add(throwable);
                    Iterator var4 = extractors.iterator();

                    while (var4.hasNext()) {
                        Entry<Class<? extends Throwable>, Function<Throwable, Throwable>> e = (Entry) var4.next();
                        if (e.getKey().isInstance(throwable)) {
                            throwable = (Throwable) ((Function) e.getValue()).apply(throwable);
                        }
                    }
                }

                return chain.toArray(new Throwable[chain.size()]);
            }
        }

        public Analyzer build(Throwable throwable) {
            return new Analyzer(this.causes(throwable));
        }
    }

    public static class Analyzer {
        private final Throwable[] chain;

        public Analyzer(Throwable[] chain) {
            if (chain != null && chain.length != 0) {
                this.chain = chain;
            } else {
                throw new IllegalArgumentException("Invalid cause chain");
            }
        }

        public final Throwable getFirstThrowableOfType(Class<? extends Throwable> throwableType) {
            if (this.chain != null) {
                Throwable[] var2 = this.chain;
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    Throwable t = var2[var4];
                    if (t != null && throwableType.isInstance(t)) {
                        return t;
                    }
                }
            }

            return null;
        }

        public final Throwable getRootCause() {
            return this.chain[this.chain.length - 1];
        }
    }
}
