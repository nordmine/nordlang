package ru.nordmine.nordlang;

import ru.nordmine.nordlang.exceptions.LangException;
import ru.nordmine.nordlang.machine.Machine;
import ru.nordmine.nordlang.machine.Program;
import ru.nordmine.nordlang.syntax.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Runner {

    public static void main(String[] args) {
        System.out.println();
        if (args.length >= 1) {
            String sourceLocation = args[0];
            new Runner().run(sourceLocation);
        } else {
            System.out.println("Please, specify source file location");
        }
        System.out.println();
    }

    private void run(String sourceLocation) {
        try {
            String source = readSource(sourceLocation);
            processSource(source);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LangException e) {
            System.out.println(e.getMessage());
        }
    }

    private String readSource(String sourceLocation) throws IOException {
        File sourceFile = new File(sourceLocation);
        if (sourceFile.exists()) {
                return readFile(sourceLocation, StandardCharsets.UTF_8);
        } else {
            throw new IOException("File does not exists");
        }
    }

    private String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private void processSource(String source) throws LangException {
        Parser parser = new Parser(source);
        Program program = parser.createProgram();
        Machine machine = new Machine(System.out);
        machine.execute(program);
    }
}
