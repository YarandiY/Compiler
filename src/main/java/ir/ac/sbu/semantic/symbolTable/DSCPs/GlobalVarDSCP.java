package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public class GlobalVarDSCP extends GlobalDSCP {


    public GlobalVarDSCP(Type type, boolean isValid, boolean constant) {
        super(type, isValid);
        this.constant = constant;
    }

}
