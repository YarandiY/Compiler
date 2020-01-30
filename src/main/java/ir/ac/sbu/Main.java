package ir.ac.sbu;

import ir.ac.sbu.lexical.Scanner;
import ir.ac.sbu.semantic.AST.Node;
import ir.ac.sbu.semantic.CodeGenerator;
import ir.ac.sbu.syntax.Parser;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new FileReader("test.txt"));
        CodeGenerator codeGenerator = new CodeGenerator(scanner);
        parseInput(scanner, codeGenerator);
    }

    private static void parseInput(Scanner lexicalAnalyzer, CodeGenerator codeGenerator) {
        Parser parser = new Parser(lexicalAnalyzer, codeGenerator, "src/main/java/ir/ac/sbu/syntax/table.npt");
        Node result;
        try {
            // Parse given file
            parser.parse();
            // Get Root of AST
            result = codeGenerator.getResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        // Create a Test class to put expression java bytecode inside it.
        // In java, every code must be put inside a class.
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "$Main", null, "java/lang/Object", null);

        // Create constructor of Test class to call it's super class.
        // Every class has a default constructor which call super constructor. (In this example, Object constructor)
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        // Create a public static main method to put out expression code inside it.
        methodVisitor = classWriter.visitMethod(Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC,
                "main", "([Ljava/lang/String;)V", null, null);
        methodVisitor.visitCode();

        // We want to print expression at then end of main method.
        // So we load "out" object to call it's "println" function at the end.
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // Call AST root function to generate expression code inside main method
        result.codegen(methodVisitor, classWriter);

        // Call "println" function to print current value on top of operand stack.
        // Our program support integer expression only so we use appropriate function from all "println"s.
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(I)V", false);
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        // Generate class file
        try (FileOutputStream fos = new FileOutputStream("Test.class")) {
            fos.write(classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Code compiled successfully");
    }

}