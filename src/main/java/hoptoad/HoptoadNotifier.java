// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package hoptoad;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HoptoadNotifier
{

    private void addingProperties(final HttpURLConnection connection) throws ProtocolException
    {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-type", "text/xml");
        connection.setRequestProperty("Accept", "text/xml, application/xml");
        connection.setRequestMethod("POST");
    }

    private HttpURLConnection createConnection() throws IOException
    {
        final HttpURLConnection connection = (HttpURLConnection) new URL("http://api.airbrake.io/notifier_api/v2/notices").openConnection();
        return connection;
    }

    private void err(final HoptoadNotice notice, final Exception e)
    {
        System.out.println(notice.toString());
        e.printStackTrace();
    }

    public int notify(final HoptoadNotice notice)
    {
        try
        {
            final HttpURLConnection toHoptoad = createConnection();
            addingProperties(toHoptoad);
            String toPost = new NoticeApi2(notice).toString();
            return send(toPost, toHoptoad);
        }
        catch (final Exception e)
        {
            err(notice, e);
        }
        return 0;
    }

    private int send(final String yaml, final HttpURLConnection connection) throws IOException
    {
        int statusCode;
        final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(yaml);
        writer.close();

        statusCode = connection.getResponseCode();
        return statusCode;
    }

}
