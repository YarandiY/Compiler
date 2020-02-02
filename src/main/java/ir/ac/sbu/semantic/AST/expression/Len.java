package ir.ac.sbu.semantic.AST.expression;

import ir.ac.sbu.semantic.AST.Operation;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Len extends Expression implements Operation{
    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
    }
}
