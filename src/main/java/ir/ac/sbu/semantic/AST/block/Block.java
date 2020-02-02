package ir.ac.sbu.semantic.AST.block;

import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.AST.Operation;
import ir.ac.sbu.semantic.AST.statement.FuncReturn;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

public class Block implements Node {

    private ArrayList<Operation> operations;

    public Block(ArrayList<Operation> operations){
        this.operations = operations;
    }


    public void addOperation(Operation operation){
        operations.add(operation);
    }

    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        if(operations == null)
            throw new RuntimeException("No expression found!");
        for (Operation op : operations) {
            op.codegen(mv, cw);
        }
    }
}
