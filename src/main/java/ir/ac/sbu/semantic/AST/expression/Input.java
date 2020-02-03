package ir.ac.sbu.semantic.AST.expression;

import ir.ac.sbu.semantic.AST.Operation;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Input extends Expression implements Operation {


    public Input(Type type) {
        this.type = type;
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
        if(type == null){
            type = SymbolTableHandler.getTypeFromName("String");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);

        }else{
            switch (type.getDescriptor()){
                case "I":
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()" + type.getDescriptor(), false);
                    break;
                case "J":
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextLong", "()" + type.getDescriptor(), false);
                    break;
                case "F":
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextFloat", "()" + type.getDescriptor(), false);
                    break;
                case "D":
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextDouble", "()" + type.getDescriptor(), false);
                    break;
                case "Z":
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextBoolean", "()" + type.getDescriptor(), false);
                    break;

            }
        }
    }
}
