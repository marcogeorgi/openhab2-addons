/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal.selfsatip;

import static org.openhab.binding.selfsatip.internal.selfsatip.Command.LOGIN;
import static org.openhab.binding.selfsatip.internal.selfsatip.Command.LOGOUT;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.openhab.binding.selfsatip.internal.http.HTTPRequest;
import org.openhab.binding.selfsatip.internal.http.HTTPResponseConsumer;

/**
 * A simple SELFSAT-IP session manager.
 *
 * @author Marco Georgi - Initial contribution
 */
public class SessionManager
{
    /**
     * Initiates the login process with the antenna.
     *
     * @param hostAndPort host and port of the antenna
     * @param user        user name
     * @param password    password
     * @return the newly created session if authentication was successful, <code>null</code> otherwise
     * @throws IOException             thrown if something went wrong while open the session
     * @throws AuthenticationException thrown if user authentication failed
     */
    public Session start( String hostAndPort, String user, String password )
        throws IOException, AuthenticationException
    {
        // check parameters
        if( hostAndPort == null || hostAndPort.trim().length() == 0 )
        {
            throw new IllegalArgumentException( "URL must not be null or empty" );
        }

        if( user == null || user.trim().length() == 0 )
        {
            throw new IllegalArgumentException( "User must not be null or empty" );
        }

        if( password == null )
        {
            throw new IllegalArgumentException( "Password must not be null" );
        }

        // prepare auth call
        Stream<Map.Entry<String, String>> parameters =
            Stream.of(
                new AbstractMap.SimpleEntry<>( "cmd", LOGIN.getCmd() ),
                new AbstractMap.SimpleEntry<>( "username", user ),
                new AbstractMap.SimpleEntry<>( "password", password ) );

        // create response consumer and invoke service
        HTTPResponseConsumer responseConsumer = new HTTPResponseConsumer();
        HTTPRequest.post( hostAndPort, LOGIN.getPath(), parameters, responseConsumer );

        // check received cookie
        Optional<HttpCookie> sessionCookie = responseConsumer.getCookie( "ticket" );
        if( !sessionCookie.isPresent() )
        {
            throw new AuthenticationException( user );
        }
        if( sessionCookie.get().getValue() == null || sessionCookie.get().getValue().isEmpty() )
        {
            throw new AuthenticationException( user );
        }

        // all OK, store the remote host and the cookie in our session object
        return new Session( hostAndPort, sessionCookie.get() );
    }

    /**
     * Closes the given antenna session.
     *
     * @param session the session to close
     * @throws IOException thrown if something went wrong while closing the session
     */
    public void close( Session session )
        throws IOException
    {
        if( session == null )
        {
            throw new IllegalArgumentException( "Session must not be null" );
        }

        if( session.hasExpired() )
        {
            return;
        }

        Stream<Map.Entry<String, String>> parameters =
            Stream.of(
                new AbstractMap.SimpleEntry<>( "cmd", LOGOUT.getCmd() ) );

        // execute logout command
        HTTPRequest.post( session, LOGOUT.getPath(), parameters, new HTTPResponseConsumer() );
    }
}
