// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package code.lucamarrocco.hoptoad;

public class HoptoadNoticeBuilderUsingFilterdSystemProperties extends HoptoadNoticeBuilder {

	public HoptoadNoticeBuilderUsingFilterdSystemProperties(final String apiKey, final Backtrace backtraceBuilder, final Throwable throwable, final String env) {
		super(apiKey, backtraceBuilder, throwable, env);
		environment(System.getProperties());
		standardEnvironmentFilters();
		ec2EnvironmentFilters();
	}

}
