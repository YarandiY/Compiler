package ir.ac.sbu.semantic.AST.expression;

import ir.ac.sbu.semantic.AST.Node;
import org.objectweb.asm.Type;

abstract public class Expression implements Node {
    protected Type type;

    public Type getType() {
        if(type == null)
            throw new RuntimeException("The type of expression is not set!");
        return type;
    }
}
