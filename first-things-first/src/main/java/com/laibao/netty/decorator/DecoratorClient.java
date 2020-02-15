package com.laibao.netty.decorator;

public class DecoratorClient {

    public static void main(String[] args) {

        /**
         * 引用装饰对象
         */
        Component component = new ConcreteDecorator2(
                                    new ConcreteDecorator1(
                                            new ConcreteComponent()));
        component.doSomething();
    }
}
