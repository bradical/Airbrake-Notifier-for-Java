package code.lucamarrocco.hoptoad;

import java.io.*;
import java.util.*;

public class Slurp {
	public static final List<String> strings(String backtraceAsString) {
		List<String> strings = new LinkedList<String>();
		Scanner scanner = new Scanner(backtraceAsString).useDelimiter("\n");
		while (scanner.hasNext()) {
			strings.add(scanner.next());
		}
		return strings;
	}

	public static InputStream read(final String file) {
		final InputStream backtraceAsStream;
		try {
			backtraceAsStream = Slurp.class.getClassLoader().getResourceAsStream(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return backtraceAsStream;
	}

	public static final String slurp(InputStream inputStream) {
		StringBuffer out = new StringBuffer();
		try {
			byte[] b = new byte[4096];
			for (int n; (n = inputStream.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return out.toString();
	}

}
