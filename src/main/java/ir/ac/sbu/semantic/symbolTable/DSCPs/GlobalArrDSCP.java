package ir.ac.sbu.semantic.symbolTable.DSCPs;

import ir.ac.sbu.semantic.AST.expression.Expression;
import lombok.Data;
import org.objectweb.asm.Type;
import java.util.List;

@Data
public class GlobalArrDSCP extends GlobalDSCP {

    protected List<Expression> dimList;

    public GlobalArrDSCP(Type type, boolean isValid, List<Expression> dimList) {
        super(type, isValid);
        this.dimList = dimList;
    }

    public GlobalArrDSCP(Type type, boolean isValid) {
        super(type, isValid);
    }
}
