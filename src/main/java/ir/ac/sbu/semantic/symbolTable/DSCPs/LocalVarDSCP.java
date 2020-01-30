package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public class LocalVarDSCP extends LocalDSCP {

    protected boolean constant;

    public LocalVarDSCP(Type type, boolean isValid, int index, boolean constant) {
        super(type, isValid, index);
        this.constant = constant;
    }

}
