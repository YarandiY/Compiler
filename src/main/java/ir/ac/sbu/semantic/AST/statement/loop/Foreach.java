package ir.ac.sbu.semantic.AST.statement.loop;

import ir.ac.sbu.semantic.AST.block.Block;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Foreach extends Loop {

    public Foreach(Block block) {
        super(block);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {

    }
}
