package ir.ac.sbu.semantic.symbolTable;


import ir.ac.sbu.semantic.symbolTable.DSCPs.DSCP;
import lombok.Data;

import java.util.HashMap;

@Data
public class SymbolTable extends HashMap<String, DSCP> {

    private int index = 0;
    private Scope typeOfScope;

    public void addIndex(int add){
        index++;
    }

    public int getIndex (){
        return index;
    }
}
