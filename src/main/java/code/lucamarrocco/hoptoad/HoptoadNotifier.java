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

		String yaml = new Yaml(notice).toString();

		try {
			postMethod.setRequestBody(yaml);
			statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != 201) System.out.println(postMethod.getResponseBodyAsString());

		} catch (Exception e) {
			// DO NOT log this exception!
			System.out.println(yaml);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		return statusCode;
	}
}
