package com.laibao.java8.recipes.defaults;

public interface Company {
    default String getName() {
        return "Initech";
    }

    // String getName();
}
