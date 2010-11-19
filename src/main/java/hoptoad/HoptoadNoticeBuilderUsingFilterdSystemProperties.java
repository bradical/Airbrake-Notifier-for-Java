// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package hoptoad;

import java.util.*;

import org.apache.log4j.*;

public class HoptoadNoticeBuilderUsingFilterdSystemProperties extends HoptoadNoticeBuilder {

	public HoptoadNoticeBuilderUsingFilterdSystemProperties(final String apiKey, final Backtrace backtraceBuilder, final Throwable throwable, final String env) {
		super(apiKey, backtraceBuilder, throwable, env);
		environment(System.getProperties());
		addMDCToSession();
		standardEnvironmentFilters();
		ec2EnvironmentFilters();
	}

	private void addMDCToSession() {
		Map<String, Object> map = MDC.getContext();
		if (map != null) {
			Map<String, Object> sessionWrapper = new HashMap<String, Object>();
			sessionWrapper.put(":key", Integer.toString(map.hashCode()));
			sessionWrapper.put(":data", map);

			session(sessionWrapper);
		}
	}
}
