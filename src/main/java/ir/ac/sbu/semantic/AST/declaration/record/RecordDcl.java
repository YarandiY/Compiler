package ir.ac.sbu.semantic.AST.declaration.record;

import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.AST.declaration.Declaration;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

@Data
public class RecordDcl implements Declaration {

    private String name;



    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {

    }
}
