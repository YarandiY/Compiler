package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

import java.util.ArrayList;

@Data
public class LocalArrDSCP extends LocalDSCP {
    protected ArrayList<Integer> dimList;

    public LocalArrDSCP(Type type, boolean isValid, int index, ArrayList<Integer> dimList) {
        super(type, isValid, index);
        this.dimList = dimList;
    }

    public LocalArrDSCP(Type type, boolean isValid, int index) {
        super(type, isValid, index);
    }
}
