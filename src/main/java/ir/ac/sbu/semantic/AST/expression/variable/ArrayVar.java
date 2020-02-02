package ir.ac.sbu.semantic.AST.expression.variable;

import ir.ac.sbu.semantic.AST.expression.Expression;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.IALOAD;


@Data
public class ArrayVar extends Variable {

    private List<Expression> dimensions;

    public ArrayVar(String name, List<Expression> dimensions) {
        this.name = name;
        this.dimensions = dimensions;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {

        new SimpleVar(name).codegen(mv, cw);

        for (int i = 0; i < dimensions.size() - 1; i++) {
            dimensions.get(i).codegen(mv, cw);
            if (dimensions.get(i).getType().equals(Type.INT_TYPE))
                throw new RuntimeException("Index should be an integer number");
            mv.visitInsn(AALOAD);
        }
        // must load the last index separately
        dimensions.get(dimensions.size() - 1).codegen(mv, cw);
        if(type.getDescriptor().endsWith(";")) // we have array of records
            mv.visitInsn(AALOAD);
        else
            mv.visitInsn(type.getOpcode(IALOAD));
    }
}
