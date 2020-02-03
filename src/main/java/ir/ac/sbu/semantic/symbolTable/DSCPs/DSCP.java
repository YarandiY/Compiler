package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public abstract class DSCP {
    protected Type type;
    protected boolean isValid;
    protected boolean constant;

    public DSCP(Type type, boolean isValid) {
        this.type = type;
        this.isValid = isValid;
    }
}
