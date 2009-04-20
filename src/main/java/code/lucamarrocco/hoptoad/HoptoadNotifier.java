package code.lucamarrocco.hoptoad;

import java.io.*;
import java.net.*;

public class HoptoadNotifier {

	private void addingProperties(HttpURLConnection connection) throws ProtocolException {
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-type", "application/x-yaml");
		connection.setRequestProperty("Accept", "text/xml, application/xml");
		connection.setRequestMethod("POST");
	}

	private HttpURLConnection createConnection() throws IOException, MalformedURLException {
		HttpURLConnection connection = (HttpURLConnection) new URL("http://hoptoadapp.com/notices/").openConnection();
		return connection;
	}

	public int notify(HoptoadNotice notice) {
		try {
			HttpURLConnection toHoptoad = createConnection();
			addingProperties(toHoptoad);
			return send(new Yaml(notice).toString(), toHoptoad);
		} catch (Exception e) {
			err(notice, e);
		}
		return 0;
	}

	private void err(HoptoadNotice notice, Exception e) {
		System.out.println(notice.toString());
		e.printStackTrace();
	}

	private int send(String yaml, HttpURLConnection connection) throws IOException {
		int statusCode;
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(yaml);
		writer.close();

		statusCode = connection.getResponseCode();
		return statusCode;
	}

}