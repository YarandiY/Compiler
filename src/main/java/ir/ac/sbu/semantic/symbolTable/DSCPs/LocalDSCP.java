package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public abstract class LocalDSCP extends DSCP {

    protected int index;

    public LocalDSCP(Type type, boolean hasValue, int index) {
        super(type, hasValue);
        this.index = index;
    }
}
