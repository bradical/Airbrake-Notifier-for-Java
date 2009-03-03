package code.lucamarrocco.hoptoad;

import java.util.*;

public class Yaml {

	private final StringBuilder yaml = new StringBuilder();

	public Yaml(HoptoadNotice notice) {
		notice();
		{
			session();

			api_key(notice.apiKey());
			error_message(notice.errorMessage());
			error_class(notice.errorClass());
			request();

			environment(notice.environment());

			backtraces();
			{
				for (String backtrace : notice.backtrace()) {
					backtrace(backtrace);
				}
			}
		};
	}

	private void api_key(String string) {
		append("  api_key: " + string + "\n");
	}

	private void append(Map<String, ?> map) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String key : map.keySet()) {
			stringBuilder.append("    " + key + ": " + map.get(key) + ",\n");
		}
		stringBuilder.append("\n");
		append(stringBuilder.toString().replaceAll(",\n\n$", "\n"));
	}

	private void append(String string) {
		yaml.append(string);
	}

	private void backtrace(String string) {
		append("  - " + string + "\n");
	}

	private void backtraces() {
		append("  backtrace: \n");
	}

	private void environment(Map map) {
		append("  environment: {\n");
		append(map);
		append("  }\n");
	}

	private void error_class(String string) {
		append("  error_class: " + string + "\n");
	}

	private void error_message(String string) {
		append("  error_message: " + string + "\n");
	}

	private void notice() {
		append("notice: \n");
	}

	private void request() {
		append("  request: {}\n\n");
	}

	private void session() {
		append("  session: {}\n\n");
	}

	@Override
	public String toString() {
		return yaml.toString();
	}
}
