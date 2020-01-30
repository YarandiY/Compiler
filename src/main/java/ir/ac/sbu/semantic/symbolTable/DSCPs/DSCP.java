package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public abstract class DSCP {
    protected Type type;
    protected boolean hasValue;

    public DSCP(Type type, boolean hasValue) {
        this.type = type;
        this.hasValue = hasValue;
    }
}
