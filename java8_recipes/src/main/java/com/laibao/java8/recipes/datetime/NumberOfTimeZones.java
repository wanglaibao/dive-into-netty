package com.laibao.java8.recipes.datetime;

import java.time.ZoneId;

public class NumberOfTimeZones {
    public static void main(String[] args) {
        System.out.println(ZoneId.getAvailableZoneIds().size());
        ZoneId.getAvailableZoneIds()
              .forEach(System.out::println);
    }
}
