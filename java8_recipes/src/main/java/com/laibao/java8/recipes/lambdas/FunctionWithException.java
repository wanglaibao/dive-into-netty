package com.laibao.java8.recipes.lambdas;

@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {
    R apply(T t) throws E;
}
