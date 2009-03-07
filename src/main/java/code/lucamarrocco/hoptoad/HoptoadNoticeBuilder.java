package code.lucamarrocco.hoptoad;

import static java.util.Arrays.*;

import java.util.*;

public class HoptoadNoticeBuilder {

	private String apiKey;

	private String errorMessage;

	private Backtrace backtrace = new Backtrace(asList("backtrace is empty"));

	private Map environment = new HashMap();

	private Map request = new HashMap();

	private Map session = new HashMap();

	private List environmentFilters = new LinkedList<String>();

	public HoptoadNoticeBuilder(String apiKey, String errorMessage) {
		this(apiKey, errorMessage, "test");
	}

	public HoptoadNoticeBuilder(String apiKey, String errorMessage, String env) {
		apiKey(apiKey);
		errorMessage(errorMessage);
		env(env);
	}

	public HoptoadNoticeBuilder(String apiKey, Throwable throwable) {
		this(apiKey, throwable, "test");
	}

	public HoptoadNoticeBuilder(String apiKey, Throwable throwable, String env) {
		this(apiKey, throwable.getMessage(), env);
		backtrace(new Backtrace(throwable));
	}

	private void apiKey(String apiKey) {
		if (notDefined(apiKey)) error("The API key for the project this error is from (required). Get this from the project's page in Hoptoad.");
		this.apiKey = apiKey;
	}

	/** An array where each element is a line of the backtrace (required, but can be empty). */
	protected void backtrace(Backtrace backtrace) {
		this.backtrace = backtrace;
	}

	protected void ec2EnvironmentFilters() {
		environmentFilter("AWS_SECRET");
		environmentFilter("EC2_PRIVATE_KEY");
		environmentFilter("AWS_ACCESS");
		environmentFilter("EC2_CERT");
	}

	private void env(String env) {
		this.environment.put("RAILS_ENV", env);
	}

	/** A hash of the environment data that existed when the error occurred (required, but can be empty). */
	protected void environment(Map environment) {
		this.environment.putAll(environment);
	}

	public void environmentFilter(String filter) {
		environmentFilters.add(filter);
	}

	private void error(String message) {
		throw new RuntimeException(message);
	}

	private void errorMessage(String errorMessage) {
		if (notDefined(errorMessage)) error("The message that describes the error (ie. \"undefined method `password' for nil:NilClass\").");
		this.errorMessage = errorMessage;
	}

	protected void filteredSystemProperties() {
		environment(System.getProperties());
		standardEnvironmentFilters();
		ec2EnvironmentFilters();
	}

	public HoptoadNotice newNotice() {
		return new HoptoadNotice(apiKey, errorMessage, backtrace, request, session, environment, environmentFilters);
	}

	private boolean notDefined(Object object) {
		return object == null;
	}

	/** A hash of the request parameters that were given when the error occurred (required, but can be empty). */
	protected void request(Map request) {
		this.request = request;
	}

	/** A hash of the session data that existed when the error occurred (required, but can be empty). */
	protected void session(Map session) {
		this.session = session;
	}

	protected void standardEnvironmentFilters() {
		environmentFilter("java.awt.*");
		environmentFilter("java.vendor.*");
		environmentFilter("java.class.path");
		environmentFilter("java.vm.specification.*");
	}
}
