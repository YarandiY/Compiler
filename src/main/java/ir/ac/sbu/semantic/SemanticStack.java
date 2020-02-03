package ir.ac.sbu.semantic;

import java.util.ArrayDeque;

public class SemanticStack extends ArrayDeque<Object> {


    @Override
    public Object pop(){
        System.out.println("poped : " + this.getFirst().getClass().getName());
        return this.removeFirst();
    }

    @Override
    public void push(Object var1) {
        System.out.println("pushed : " + var1.getClass().getName());
        this.addFirst(var1);
    }
}
