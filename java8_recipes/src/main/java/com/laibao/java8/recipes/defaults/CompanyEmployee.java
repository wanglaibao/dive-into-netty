package com.laibao.java8.recipes.defaults;

public class CompanyEmployee implements Company, Employee {
    private String first;
    private String last;

    public CompanyEmployee() {}

    public CompanyEmployee(String first, String last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public String getName() {
        return String.format("%s works for %s",
                Employee.super.getName(), Company.super.getName());
    }

    @Override
    public void convertCaffeineToCode() {
        System.out.println("Coding...");
    }

    @Override
    public String getFirst() {
        return first;
    }

    @Override
    public String getLast() {
        return last;
    }

}
