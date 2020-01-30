package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public class GlobalVarDSCP extends GlobalDSCP {

    public GlobalVarDSCP(Type type, boolean hasValue) {
        super(type, hasValue);
    }
}
