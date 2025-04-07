package spring.sub;

import spring.Autowired;
import spring.Component;
import spring.PostConstructor;

@Component(name = "mydog")
public class Dog {

    @Autowired
    Cat cat;

    @Autowired(required = false)
    Monkey monkey;

    @PostConstructor
    public void init() {
        System.out.println("Dog创建了 dog，里面有一个属性" + cat);
    }
}
