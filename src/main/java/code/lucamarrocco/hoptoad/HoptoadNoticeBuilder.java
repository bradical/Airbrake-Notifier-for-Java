package code.lucamarrocco.hoptoad;

import java.text.*;
import java.util.*;

public class HoptoadNoticeBuilder {
	private String apiKey;

	private String errorMessage;

	private String[] backtrace = new String[] { "backtrace is nil" };

	private Map environment;

	private Map request;

	private Map session;

	public HoptoadNoticeBuilder(String apiKey, String errorMessage) {
		apiKey(apiKey);
		errorMessage(errorMessage);
	}

	private void errorMessage(String errorMessage) {
		if (notDefined(errorMessage)) error("The message that describes the error (ie. \"undefined method `password' for nil:NilClass\").");
		this.errorMessage = errorMessage;
	}

	private void apiKey(String apiKey) {
		if (notDefined(apiKey)) error("The API key for the project this error is from (required). Get this from the project's page in Hoptoad.");
		this.apiKey = apiKey;
	}

	public HoptoadNoticeBuilder(String apiKey, Exception exception) {
		apiKey(apiKey);
		errorMessage(exception.getMessage());
		backtrace(toBacktrace(exception.getStackTrace()));
	}

	public static String[] toBacktrace(StackTraceElement[] stackTrace) {
		List<String> backtrace = new LinkedList<String>();
		for (StackTraceElement stackTraceElement : stackTrace) {
			backtrace.add(backtrace(stackTraceElement));
		}
		return backtrace.toArray(new String[0]);
	}

	private static String backtrace(StackTraceElement stackTraceElement) {
		return MessageFormat.format("{0}.{1}({2}:{3})", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber());
	}

	public HoptoadNotice newNotice() {
		return new HoptoadNotice(apiKey, errorMessage, backtrace, request, session, environment);
	}

	private boolean notDefined(Object object) {
		return object == null;
	}

	private void error(String message) {
		throw new RuntimeException(message);
	}

	/** A hash of the environment data that existed when the error occurred (required, but can be empty). */
	protected void environment(Map environment) {
		this.environment = environment;
	}

	/** A hash of the session data that existed when the error occurred (required, but can be empty). */
	protected void session(Map session) {
		this.session = session;
	}

	/** A hash of the request parameters that were given when the error occurred (required, but can be empty). */
	protected void request(Map request) {
		this.request = request;
	}

	/** An array where each element is a line of the backtrace (required, but can be empty). */
	protected void backtrace(String[] backtrace) {
		this.backtrace = backtrace;
	}
}
