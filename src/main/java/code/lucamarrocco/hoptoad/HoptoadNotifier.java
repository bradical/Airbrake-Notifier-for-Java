package code.lucamarrocco.hoptoad;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

public class HoptoadNotifier {

	public int notify(HoptoadNotice notice) {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod("http://hoptoadapp.com/notices/");
		postMethod.setRequestHeader("Content-type", "application/x-yaml");
		postMethod.setRequestHeader("Accept", "text/xml, application/xml");

		int statusCode = 0;

		try {
			String yaml = new Yaml(notice).toString();
			postMethod.setRequestBody(yaml);
			System.out.println(yaml);
			statusCode = httpClient.executeMethod(postMethod);
			System.out.println(postMethod.getResponseBodyAsString());
		} catch (Exception e) {
			// DO NOT log this exception!
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		return statusCode;
	}
}
