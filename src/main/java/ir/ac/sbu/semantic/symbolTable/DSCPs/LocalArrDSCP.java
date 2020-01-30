package ir.ac.sbu.semantic.symbolTable.DSCPs;

import lombok.Data;
import org.objectweb.asm.Type;

import java.util.ArrayList;

@Data
public class LocalArrDSCP extends LocalDSCP {
    protected ArrayList<Integer> dimList;

    public LocalArrDSCP(Type type, boolean hasValue, int index, ArrayList<Integer> dimList) {
        super(type, hasValue, index);
        this.dimList = dimList;
    }

    public LocalArrDSCP(Type type, boolean hasValue, int index) {
        super(type, hasValue, index);
    }
}
