package ir.ac.sbu.semantic.AST.statement.loop;

import ir.ac.sbu.semantic.AST.block.Block;
import ir.ac.sbu.semantic.AST.statement.Statement;
import lombok.Data;
import org.objectweb.asm.Label;

@Data
public abstract class Loop extends Statement{
    protected Block block;
    Label startLoop = new Label();
    Label end = new Label();
    Loop(Block block) {
        this.block = block;
    }
}
