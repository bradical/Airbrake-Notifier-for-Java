// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package code.lucamarrocco.hoptoad;

import static java.util.Arrays.*;

import java.util.*;

public class HoptoadNoticeBuilder {

	private String apiKey;

	private String errorMessage;

	private Backtrace backtrace = new Backtrace(asList("backtrace is empty"));

	private final Map<String, Object> environment = new TreeMap<String, Object>();

	private Map<String, Object> request = new TreeMap<String, Object>();

	private Map<String, Object> session = new TreeMap<String, Object>();

	private final List<String> environmentFilters = new LinkedList<String>();

	private Backtrace backtraceBuilder = new Backtrace();

	public HoptoadNoticeBuilder(final String apiKey, final Backtrace backtraceBuilder, final Throwable throwable, final String env) {
		this(apiKey, throwable.getMessage(), env);
		this.backtraceBuilder = backtraceBuilder;
		backtrace(throwable);
	}

	public HoptoadNoticeBuilder(final String apiKey, final String errorMessage) {
		this(apiKey, errorMessage, "test");
	}

	public HoptoadNoticeBuilder(final String apiKey, final String errorMessage, final String env) {
		apiKey(apiKey);
		errorMessage(errorMessage);
		env(env);
	}

	public HoptoadNoticeBuilder(final String apiKey, final Throwable throwable) {
		this(apiKey, new Backtrace(), throwable, "test");
	}

	public HoptoadNoticeBuilder(final String apiKey, final Throwable throwable, final String env) {
		this(apiKey, new Backtrace(), throwable, env);
	}

	private void apiKey(final String apiKey) {
		if (notDefined(apiKey)) {
			error("The API key for the project this error is from (required). Get this from the project's page in Hoptoad.");
		}
		this.apiKey = apiKey;
	}

	/** An array where each element is a line of the backtrace (required, but can be empty). */
	protected void backtrace(final Backtrace backtrace) {
		this.backtrace = backtrace;
	}

	private void backtrace(final Throwable throwable) {
		backtrace(backtraceBuilder.newBacktrace(throwable));
	}

	protected void ec2EnvironmentFilters() {
		environmentFilter("AWS_SECRET");
		environmentFilter("EC2_PRIVATE_KEY");
		environmentFilter("AWS_ACCESS");
		environmentFilter("EC2_CERT");
	}

	private void env(final String env) {
		this.environment.put("RAILS_ENV", env);
	}

	/** A hash of the environment data that existed when the error occurred (required, but can be empty). */
	protected void environment(final Map environment) {
		this.environment.putAll(environment);
	}

	public void environmentFilter(final String filter) {
		environmentFilters.add(filter);
	}

	private void error(final String message) {
		throw new RuntimeException(message);
	}

	private void errorMessage(final String errorMessage) {
		if (notDefined(errorMessage)) {
			error("The message that describes the error (ie. \"undefined method `password' for nil:NilClass\").");
		}
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

	private boolean notDefined(final Object object) {
		return object == null;
	}

	/** A hash of the request parameters that were given when the error occurred (required, but can be empty). */
	protected void request(final Map request) {
		this.request = request;
	}

	/** A hash of the session data that existed when the error occurred (required, but can be empty). */
	protected void session(final Map session) {
		this.session = session;
	}

	protected void standardEnvironmentFilters() {
		environmentFilter("java.awt.*");
		environmentFilter("java.vendor.*");
		environmentFilter("java.class.path");
		environmentFilter("java.vm.specification.*");
	}
}
