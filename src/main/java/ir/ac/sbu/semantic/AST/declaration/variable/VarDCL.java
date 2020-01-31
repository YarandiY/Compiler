package ir.ac.sbu.semantic.AST.declaration.variable;

import ir.ac.sbu.semantic.AST.Operation;
import ir.ac.sbu.semantic.AST.statement.loop.InitExp;
import lombok.Data;
import org.objectweb.asm.Type;

@Data
public abstract class VarDCL implements Operation, InitExp {
    protected String name;
    protected Type type = null;
    protected boolean constant = false;
    protected boolean global = true;
}
