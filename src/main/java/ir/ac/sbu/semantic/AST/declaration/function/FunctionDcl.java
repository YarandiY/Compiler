package ir.ac.sbu.semantic.AST.declaration.function;

import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.declaration.variable.VarDcl;
import ir.ac.sbu.semantic.symbolTable.Scope;
import ir.ac.sbu.semantic.symbolTable.SymbolTableHandler;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Objects;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

@Data
public class FunctionDcl implements Node {

   private Type type;
   private String name;
   private ArrayList<VarDcl> inputs;
   private ArrayList<Type> inputsType;
   private String signature;
   private Block block;

   public FunctionDcl(Type type, String name, Block block, ArrayList<VarDcl> inputs){
       this.type = type;
       this.name = name;
       this.block = block;
       this.inputs = inputs;
       String signature = "(";
       for(VarDcl varDcl : inputs){
           signature = signature+varDcl.getType().toString();
           inputsType.add(varDcl.getType());
       }
       signature = signature + ")";
       signature = signature + type.toString();
       this.signature = signature;
       declare();
   }

   public FunctionDcl(String name,String signature,Block block){
       this.signature = signature;
       Type[] types = Type.getArgumentTypes(signature);
       Type [] arguments = types;
       this.type = Type.getType(signature.substring(signature.indexOf(')')+1));
       this.name = name;
       inputsType = new ArrayList<>();
       for(Type t : arguments){
           inputsType.add(t);
       }
       this.block = block;
       declare();
   }

    private void declare(){
       SymbolTableHandler.getInstance().addFunction(this);
   }



    @Override
    public void codegen(MethodVisitor mv, ClassWriter cw) {
        // cv.visitMethod(ACC_PUBLIC,name,this.signature,null,null); msdn version!
       MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC,name,"",signature,null); //ab version!
       methodVisitor.visitCode();
       //TODO
       SymbolTableHandler.getInstance().addScope(Scope.FUNCTION);

    }

    @Override
    public boolean equals(Object o) {
       if(o instanceof FunctionDcl)
           return checkIfEqual(((FunctionDcl) o).name,((FunctionDcl) o).inputsType);
       return false;
    }

    public boolean checkIfEqual(String name, ArrayList<Type> inputsType){
       if(this.name != name)
           return false;
        for (Type type :
                inputsType) {
            if(!this.inputsType.contains(type))
                return false;
        }
        return true;
    }
}
