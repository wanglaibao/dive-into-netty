package com.laibao.java8.recipes.concurrency;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class AwaitQuiesenceTest {
    private AwaitQuiesence aq = new AwaitQuiesence();

    @Test
    public void get() {
        try {
            CompletableFuture<Void> cf = aq.supplyThenAccept();
            cf.get();
            assertTrue(cf.isDone());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void join() {
        CompletableFuture<Void> cf = aq.supplyThenAccept();
        cf.join();
        assertTrue(cf.isDone());
    }

    @Test @Ignore("Causing issues with Github Action")
    public void awaitQuiesence() {
        CompletableFuture<Void> cf = aq.supplyThenAccept();
        assertFalse(cf.isDone());

        ForkJoinPool.commonPool().awaitQuiescence(1, TimeUnit.SECONDS);
        assertTrue(cf.isDone());
    }
}