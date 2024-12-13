import java.util.LinkedList;
import java.util.Queue;
import java.io.IOException;

public class Interpreter {
	private static int tape[] = new int[256];
	private static int pointer = 0;
	private static Queue<Integer> bracketQueue = new LinkedList<>();

	public static void run(String sourceCode) throws IOException {
		for (int sourceIndex = 0; sourceIndex < sourceCode.length(); sourceIndex++) {
			switch (sourceCode.charAt(sourceIndex)) {
			    case '+': increment(); break;
			    case '-': decrement(); break;
			    case '<': moveLeft(); break;
			    case '>': moveRight(); break;
			    case '.': output(); break;
			    case ',': input(); break;
				case '[': startBranch(sourceIndex); break;
				case ']': sourceIndex = endBranch(sourceIndex); break;
			}
		}
		if (!bracketQueue.isEmpty()) { throw new IOException("Branch mismatch: No ']' available at pointer " + pointer); }
		System.exit(1);
	}

	// instructions: + -
	public static void increment() {
		if (tape[pointer] == 255) { tape[pointer] = 0; }
		else { tape[pointer]++; }
	}

	public static void decrement() {
		if (tape[pointer] == 0) { tape[pointer] = 255; }
		else { tape[pointer]--; }
	}

	// instructions: < >
	public static void moveLeft() {
		if (pointer == 0) { pointer = 255; } // Tape wrapping
		else { pointer--; }
	}

	public static void moveRight() {
		if (pointer == 255) { pointer = 0; } // Tape wrapping
		else { pointer++; }
	}

	// instructions: . ,
	public static void output() { System.out.print((char)tape[pointer]); }
	public static void input() { tape[pointer] = Parser.requestChar(); }

	// instructions: [ ]
	private static void startBranch(int leftBracketIndex) { bracketQueue.add(leftBracketIndex); }
	private static int endBranch(int sourceIndex) throws IOException {
		if (bracketQueue.isEmpty()) { throw new IOException("Branch mismatch: No '[' available at pointer " + pointer); }
		if (tape[pointer] == 0) { bracketQueue.remove(); return sourceIndex; }
		else { return bracketQueue.peek(); }
	}
}