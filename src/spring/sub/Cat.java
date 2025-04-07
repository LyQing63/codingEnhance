package spring.sub;

import spring.Autowired;
import spring.Component;
import spring.PostConstructor;

@Component
public class Cat {
    @Autowired
    private Dog dog;

    @PostConstructor
    public void init() {
        System.out.println("Cat创建了 cat，里面有一个属性" + dog);
    }
}
