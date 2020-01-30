package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

import java.util.ArrayList;

@Data
public class GlobalArrDSCP extends GlobalDSCP {

    protected ArrayList<Integer> dimList;

    public GlobalArrDSCP(Type type, boolean isValid, ArrayList<Integer> dimList) {
        super(type, isValid);
        this.dimList = dimList;
    }

    public GlobalArrDSCP(Type type, boolean isValid) {
        super(type, isValid);
    }
}
