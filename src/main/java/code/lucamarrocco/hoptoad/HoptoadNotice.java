// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package code.lucamarrocco.hoptoad;

import static java.util.Arrays.*;

import java.util.*;

public class HoptoadNotice {

	private static final String RAILS_ENV = "RAILS_ENV";

	private final String apiKey;

	private final String errorMessage;

	private Backtrace backtrace = new Backtrace(asList("backtrace is empty"));

	private final Map<String, Object> environment = new TreeMap<String, Object>();

	private Map<String, Object> request = new TreeMap<String, Object>();

	private Map<String, Object> session = new TreeMap<String, Object>();

	public HoptoadNotice(final String apiKey, final String errorMessage, final Backtrace backtrace, final Map<String, Object> request, final Map<String, Object> session, final Map<String, Object> environment, final List<String> environmentFilters) {
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

	private void filter(final Map<String, Object> environment, final List<String> environmentFilters) {
		for (final String key : environment.keySet()) {
			if (!matches(environmentFilters, key)) {
				this.environment.put(key, environment.get(key));
			}
		}
	}

	private boolean matches(final List<String> environmentFilters, final String key) {
		for (final String filter : environmentFilters) {
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
