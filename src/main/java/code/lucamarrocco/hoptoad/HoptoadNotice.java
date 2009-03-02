package code.lucamarrocco.hoptoad;

import java.util.*;

public class HoptoadNotice {

	private static final String RAILS_ENV = "RAILS_ENV";

	private String apiKey;

	private String errorMessage;

	private String[] backtrace = new String[] { "backtrace is null" };

	private Map environment;

	private Map request;

	private Map session;

	public HoptoadNotice(String apiKey, String errorMessage, String[] backtrace, Map request, Map session, Map environment) {
		this.apiKey = apiKey;
		this.errorMessage = errorMessage;
		this.backtrace = backtrace;
		this.request = request;
		this.session = session;
		this.environment = environment;
	}

	public String apiKey() {
		return apiKey;
	}

	public String[] backtrace() {
		return backtrace;
	}

	public Map environment() {
		return environment;
	}

	public String errorClass() {
		return getClass().getSimpleName();
	}

	public String errorMessage() {
		return errorMessage;
	}

	public Map request() {
		return request;
	}

	public Map session() {
		return session;
	}

	public String env() {
		return (String) environment.get(RAILS_ENV);
	}
}
