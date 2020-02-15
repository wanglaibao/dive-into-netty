package com.laibao.netty.decorator;

public class AbstractDecorator implements Component {

    private Component component;

    public AbstractDecorator(Component component) {
        this.component = component;
    }

    public void doSomething() {
        component.doSomething();
    }

}
