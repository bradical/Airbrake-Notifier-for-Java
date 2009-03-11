package code.lucamarrocco.hoptoad;

import static java.util.Arrays.*;

import java.util.*;

public class HoptoadNotice {

	private static final String RAILS_ENV = "RAILS_ENV";

	private String apiKey;

	private String errorMessage;

	private Backtrace backtrace = new Backtrace(asList("backtrace is empty"));

	private Map<String, Object> environment = new TreeMap<String, Object>();

	private Map<String, Object> request = new TreeMap<String, Object>();

	private Map<String, Object> session = new TreeMap<String, Object>();

	public HoptoadNotice(String apiKey, String errorMessage, Backtrace backtrace, Map<String, Object> request, Map<String, Object> session, Map<String, Object> environment,
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

	public Backtrace backtrace() {
		return backtrace;
	}

	public String env() {
		return (String) environment.get(RAILS_ENV);
	}

	public Map<String, Object> environment() {
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

	public Map<String, Object> request() {
		return request;
	}

	public Map<String, Object> session() {
		return session;
	}
}
