package ir.ac.sbu.semantic.AST.statement.loop;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.statement.Statement;

public abstract class Loop extends Statement{
    Block block;
    public Loop(Block block) {
        this.block = block;
    }
}
