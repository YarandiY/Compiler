package ir.ac.sbu.semantic.symbolTable.DSCPs;

import ir.ac.sbu.semantic.AST.expression.Expression;
import lombok.Data;
import org.objectweb.asm.Type;
import java.util.List;

@Data
public class LocalArrDSCP extends LocalDSCP {
    protected List<Expression> dimList;

    public LocalArrDSCP(Type type, boolean isValid, int index, List<Expression> dimList) {
        super(type, isValid, index);
        this.dimList = dimList;
    }

    public LocalArrDSCP(Type type, boolean isValid, int index) {
        super(type, isValid, index);
    }
}
