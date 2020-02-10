package com.laibao.java8.recipes;

import com.laibao.java8.recipes.Primes;

public class PrimesDemo {
    public static void main(String[] args) {
        Primes calculator = new Primes();
        System.out.println(calculator.nextPrime(1000));
    }
}
