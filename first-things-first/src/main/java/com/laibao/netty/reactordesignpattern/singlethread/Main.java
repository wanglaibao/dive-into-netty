package com.laibao.netty.reactordesignpattern.singlethread;

public class Main {

    public static void main(String[] args) {
        try{
            SingleReactor reactor = new SingleReactor(8989);
            reactor.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
