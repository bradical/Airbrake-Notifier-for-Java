package code.lucamarrocco.hoptoad;

public class HoptoadNoticeBuilderUsingFilterdSystemProperties extends HoptoadNoticeBuilder {

	public HoptoadNoticeBuilderUsingFilterdSystemProperties(String apiKey, Backtrace backtraceBuilder, Throwable throwable, String env) {
		super(apiKey, backtraceBuilder, throwable, env);
		environment(System.getProperties());
		standardEnvironmentFilters();
		ec2EnvironmentFilters();
	}

}
