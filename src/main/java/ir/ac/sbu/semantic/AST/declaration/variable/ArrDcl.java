package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.Operation;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Data
public class ArrDcl extends VarDCL {

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {

    }
}
