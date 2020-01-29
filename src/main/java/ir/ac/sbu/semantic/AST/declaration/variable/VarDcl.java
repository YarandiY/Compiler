package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.Operation;
import lombok.Data;
import org.objectweb.asm.Type;

@Data
public abstract class VarDcl implements Operation {

    protected String name;
    protected Type type = null;
    protected boolean constant = false;
    protected boolean global = true;

}
