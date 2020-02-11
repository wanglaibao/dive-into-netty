package com.laibao.java8.recipes.concurrency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Ignore;
import org.junit.Test;

public class CompletableFutureTests {

    @Test(expected = CancellationException.class)
    public void joinAfterCancel() {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.cancel(false);
        System.out.println(future.join());
    }

    @Test
    public void completeWithGet() {
        CompletableFuture<String> future = new CompletableFuture<>();
        boolean finished = future.complete("I'm done");
        assertTrue(finished);
        try {
            assertEquals("I'm done", future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completeWithJoin() {
        CompletableFuture<String> future = new CompletableFuture<>();
        boolean finished = future.complete("I'm done");
        assertTrue(finished);
        assertEquals("I'm done", future.join());
    }

    @Test(expected = ExecutionException.class)
    public void completeExceptionally() throws Exception {
        parseNumber("abc").get();
    }

    @Test
    public void completeExceptionallyWithCause() {
        try {
            parseNumber("abc").get();
            fail("Should not get here");
        } catch (Exception e) {
            assertEquals(ExecutionException.class, e.getClass());
            assertEquals(NumberFormatException.class, e.getCause().getClass());
        }
    }

    @Test
    public void completeLong() throws Exception {
        assertTrue(parseNumber("42").get() == 42);
    }

    private CompletableFuture<Long> parseNumber(String arg) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        try {
            future.complete(Long.parseLong(arg));
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Test @Ignore("problems with CI server")
    public void supplyThenAccept() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        CompletableFuture.supplyAsync(() -> "42")
                .thenApply(Integer::parseInt)
                .thenApply(x -> 2 * x)
                .thenAccept(System.out::println);
        System.out.println("Running...");

        String result = baos.toString();
        System.out.println(result);
        assertTrue(result.contains("84"));
        assertTrue(result.contains("Running..."));
    }

    @Test @Ignore("problems with CI server")
    public void supplyThenAcceptAsyncWithExecutor() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        ExecutorService service = Executors.newFixedThreadPool(4);
        CompletableFuture.supplyAsync(() -> "42", service)
                .thenApply(Integer::parseInt)
                .thenApply(x -> 2 * x)
                .thenAccept(System.out::println);
        System.out.println("Running...");

        String result = baos.toString();
        System.out.println(result);
        assertTrue(result.contains("84"));
        assertTrue(result.contains("Running..."));

    }

    @Test
    public void compose() throws Exception {
        int x = 2;
        int y = 3;
        CompletableFuture<Integer> completableFuture =
                CompletableFuture.supplyAsync(() -> x)
                        .thenCompose(n -> CompletableFuture.supplyAsync(() -> n + y));

        assertTrue(5 == completableFuture.get());
    }

    @Test
    public void combine() throws Exception {
        int x = 2;
        int y = 3;
        CompletableFuture<Integer> completableFuture =
                CompletableFuture.supplyAsync(() -> x)
                        .thenCombine(CompletableFuture.supplyAsync(() -> y),
                                (n1, n2) -> n1 + n2);

        assertTrue(5 == completableFuture.get());
    }

    private CompletableFuture<Integer> getIntegerCompletableFuture(String num) {
        return CompletableFuture.supplyAsync(() -> Integer.parseInt(num))
                .handle((val, exc) -> val != null ? val : 0);
    }

    @Test
    public void handleWithException() throws Exception {
        String num = "abc";
        CompletableFuture<Integer> value = getIntegerCompletableFuture(num);
        assertTrue(value.get() == 0);
    }

    @Test
    public void handleWithoutException() throws Exception {
        String num = "42";
        CompletableFuture<Integer> value = getIntegerCompletableFuture(num);
        assertTrue(value.get() == 42);
    }

}
