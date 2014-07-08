package nordlang;

import nordlang.compiler.Compiler;
import nordlang.compiler.CompilerImpl;
import nordlang.exceptions.LangException;
import nordlang.exceptions.ParserException;
import nordlang.parser.api.MethodInfo;
import nordlang.parser.api.MethodReader;
import nordlang.parser.impl.MethodReaderImpl;
import nordlang.vm.api.Engine;
import nordlang.vm.impl.EngineImpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Runner {

	public static void main(String[] args) {
		System.out.println("Nordlang (c) 2014");
		if (args.length >= 1) {
			String sourceLocation = args[0];
			readSourceAndProcess(sourceLocation);
		} else {
			System.out.println("Please, specify source file location");
		}
	}

	private static void readSourceAndProcess(String sourceLocation) {
		File sourceFile = new File(sourceLocation);
		if (sourceFile.exists()) {
			try {
				String source = readFile(sourceLocation, Charset.forName("UTF-8"));
				processSource(source);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File does not exists");
		}
	}

	private static void processSource(String source) throws LangException {
		List<MethodInfo> parsedMethods = readMethods(source);

		Compiler compiler = new CompilerImpl();
		Engine engine = new EngineImpl();
		engine.setProgram(compiler.compile(parsedMethods));
		engine.run();
	}

	private static List<MethodInfo> readMethods(String source) throws ParserException {
		MethodReader reader = new MethodReaderImpl(source);
		List<MethodInfo> parsedMethods = new ArrayList<MethodInfo>();
		while (true) {
			MethodInfo parsedMethod = reader.readMethod();
			if(parsedMethod == null) {
				break;
			} else {
				parsedMethods.add(parsedMethod);
			}
		}
		return parsedMethods;
	}

	private static String readFile(String path, Charset encoding)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
