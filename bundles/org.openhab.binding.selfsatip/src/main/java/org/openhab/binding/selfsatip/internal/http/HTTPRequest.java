/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.selfsatip.internal.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openhab.binding.selfsatip.internal.selfsatip.Session;

/**
 * This utility class provides convenience methods to execute HTTP Post and Get requests.
 *
 * @author Marco Georgi - Initial contribution
 */
public class HTTPRequest
{
    /**
     * Timeout: 3000 milliseconds
     */
    private static final int TIMEOUT_MILLIS = 3000;

    /**
     * Executes a POST request.
     *
     * @param hostAndPort       remote host and port
     * @param path              remote path
     * @param parameters        request parameters
     * @param responseConsumers consumers that are responsible for handling the response
     * @throws IOException thrown if something went wrong
     */
    public static void post( String hostAndPort, String path, Stream<Map.Entry<String, String>> parameters, HTTPResponseConsumer responseConsumers )
        throws IOException
    {
        post( hostAndPort, path, null, parameters, responseConsumers );
    }

    /**
     * Executes a POST request using the provided {@link Session}.
     *
     * @param session           the session to use
     * @param path              remote path
     * @param parameters        request parameters
     * @param responseConsumers consumers that are responsible for handling the response
     * @throws IOException thrown if something went wrong
     */
    public static void post( Session session, String path, Stream<Map.Entry<String, String>> parameters, HTTPResponseConsumer responseConsumers )
        throws IOException
    {
        post(
            session.getHostAndPort(),
            path,
            Stream.of( new AbstractMap.SimpleEntry<>( "Cookie", session.getTicket()
                .getName() + "=" + session.getTicket().getValue() ) ),
            parameters,
            responseConsumers );
    }

    /**
     * Executes a POST request.
     *
     * @param hostAndPort       remote host and port
     * @param path              remote path
     * @param header            the header values to send with the request
     * @param parameters        request parameters
     * @param responseConsumers consumers that are responsible for handling the response
     * @throws IOException thrown if something went wrong
     */
    private static void post( String hostAndPort,
                              String path,
                              Stream<Map.Entry<String, String>> header,
                              Stream<Map.Entry<String, String>> parameters,
                              HTTPResponseConsumer responseConsumers )
        throws IOException
    {
        // verify input data
        if( hostAndPort == null || hostAndPort.trim().length() == 0 )
            throw new IllegalArgumentException( "Host must not be null or empty" );

        if( path == null || path.trim().length() == 0 )
            throw new IllegalArgumentException( "Path path must not be null or empty" );

        if( responseConsumers == null )
            throw new IllegalArgumentException( "Response consumer must not be null" );

        // create the remote URL
        String url = hostAndPort + path;

        // create the URL connection
        URLConnection connection = new URL( url ).openConnection();
        connection.setConnectTimeout( TIMEOUT_MILLIS );
        connection.setReadTimeout( TIMEOUT_MILLIS );

        if( !(connection instanceof HttpURLConnection) )
        {
            throw new UnsupportedOperationException( "Unexpected connection for URL: " + url );
        }

        // set POST method
        ((HttpURLConnection) connection).setRequestMethod( "POST" );

        // write request header
        if( header != null )
        {
            header.forEach( entry -> connection.setRequestProperty( entry.getKey(), entry.getValue() ) );
        }

        // write request body
        connection.setDoOutput( true );
        try( BufferedWriter postBody = new BufferedWriter( new OutputStreamWriter( connection.getOutputStream() ) ) )
        {
            if( parameters != null )
            {
                postBody.write(
                    parameters.map( parameter -> parameter.getKey() + "=" + parameter.getValue() )
                        .collect( Collectors.joining( "&" ) ) );
            }
            postBody.flush();
        }

        // receive response ...
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        if( responseCode != 200 )
        {
            throw new IOException( "Server responded: " + responseCode );
        }

        // process headers
        responseConsumers.setHeader( connection.getHeaderFields() );

        // process response stream
        try( InputStream inputStream = connection.getInputStream() )
        {
            // consume stream
            Consumer<InputStream> streamConsumer = responseConsumers.getResponseStreamConsumer();
            if( streamConsumer != null )
            {
                streamConsumer.accept( inputStream );
            }
        }
    }

    /**
     * Executes a GET request.
     *
     * @param url               the request URL
     * @param responseConsumers consumers that are responsible for handling the response
     * @throws IOException thrown if something went wrong
     */
    public static void get( String url, HTTPResponseConsumer responseConsumers )
        throws IOException
    {
        // verify input data
        if( url == null || url.trim().length() == 0 )
            throw new IllegalArgumentException( "URL must not be null or empty" );

        // create URL connection
        URLConnection connection = new URL( url ).openConnection();

        if( !(connection instanceof HttpURLConnection) )
        {
            throw new UnsupportedOperationException( "Unexpected connection for URL: " + url );
        }

        // set GET method
        ((HttpURLConnection) connection).setRequestMethod( "GET" );

        // receive response ...
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        if( responseCode != 200 )
        {
            throw new IOException( "Server responded: " + responseCode );
        }

        // process headers
        responseConsumers.setHeader( connection.getHeaderFields() );

        // process response stream
        try( InputStream inputStream = connection.getInputStream() )
        {
            // consume stream
            Consumer<InputStream> streamConsumer = responseConsumers.getResponseStreamConsumer();
            if( streamConsumer != null )
            {
                streamConsumer.accept( inputStream );
            }
        }
    }

    /**
     * Very simple GET request, returning the first/single line response.
     *
     * @param url the request URL
     * @return the first line of the response
     * @throws IOException thrown if something went wrong
     */
    public static String get( String url )
        throws IOException
    {
        // verify input data
        if( url == null || url.trim().length() == 0 )
            throw new IllegalArgumentException( "URL must not be null or empty" );

        // create URL connection
        URLConnection connection = new URL( url ).openConnection();

        if( !(connection instanceof HttpURLConnection) )
        {
            throw new UnsupportedOperationException( "Unexpected connection for URL: " + url );
        }

        // set GET method
        ((HttpURLConnection) connection).setRequestMethod( "GET" );

        // receive response ...
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        if( responseCode != 200 )
        {
            throw new IOException( "Server responded: " + responseCode );
        }

        // return first line
        try( BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) ) )
        {
            return reader.readLine();
        }
    }
}
