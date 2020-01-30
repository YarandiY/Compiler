package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public abstract class GlobalDSCP extends DSCP{

    public GlobalDSCP(Type type, boolean isValid) {
        super(type, isValid);
    }
}
