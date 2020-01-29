package ir.ac.sbu.semantic.AST;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public interface Node {
    void codegen(MethodVisitor mv, ClassWriter cw);
}
