package com.laibao.java8.recipes.defaults;

public interface Employee {
    String getFirst();

    String getLast();

    void convertCaffeineToCode();

    default String getName() {
        return String.format("%s %s", getFirst(), getLast());
    }
}
