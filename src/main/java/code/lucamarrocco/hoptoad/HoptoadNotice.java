package code.lucamarrocco.hoptoad;

import java.util.*;

public class HoptoadNotice {

	private static final String RAILS_ENV = "RAILS_ENV";

	private String apiKey;

	private String errorMessage;

	private String[] backtrace = new String[] { "backtrace is null" };

	private Map<String, Object> environment = new HashMap();

	private Map<String, Object> request;

	private Map<String, Object> session;

	public HoptoadNotice(String apiKey, String errorMessage, String[] backtrace, Map<String, Object> request, Map<String, Object> session, Map<String, Object> environment,
			List<String> environmentFilters) {
		this.apiKey = apiKey;
		this.errorMessage = errorMessage;
		this.backtrace = backtrace;
		this.request = request;
		this.session = session;
		filter(environment, environmentFilters);
	}

	public String apiKey() {
		return apiKey;
	}

	public String[] backtrace() {
		return backtrace;
	}

	public String env() {
		return (String) environment.get(RAILS_ENV);
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

	private void filter(Map<String, Object> environment, List<String> environmentFilters) {
		for (String key : environment.keySet()) {
			if (!matches(environmentFilters, key)) {
				this.environment.put(key, environment.get(key));
			}
		}
	}

	private boolean matches(List<String> environmentFilters, String key) {
		for (String filter : environmentFilters) {
			if (key.matches(filter)) return true;
		}
		return false;
	}

	public Map request() {
		return request;
	}

	public Map session() {
		return session;
	}
}
