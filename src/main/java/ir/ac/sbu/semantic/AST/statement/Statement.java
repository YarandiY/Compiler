package ir.ac.sbu.semantic.AST.statement;

import ir.ac.sbu.semantic.AST.Operation;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Statement implements Operation {

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cv) {

    }
}
