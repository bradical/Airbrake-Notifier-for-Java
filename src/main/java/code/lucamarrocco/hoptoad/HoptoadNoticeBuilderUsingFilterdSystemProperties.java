package code.lucamarrocco.hoptoad;

public class HoptoadNoticeBuilderUsingFilterdSystemProperties extends HoptoadNoticeBuilder {

	public HoptoadNoticeBuilderUsingFilterdSystemProperties(String apiKey, Throwable throwable, String env) {
		super(apiKey, throwable, env);
		environment(System.getProperties());
		standardEnvironmentFilters();
		ec2EnvironmentFilters();
	}

}
