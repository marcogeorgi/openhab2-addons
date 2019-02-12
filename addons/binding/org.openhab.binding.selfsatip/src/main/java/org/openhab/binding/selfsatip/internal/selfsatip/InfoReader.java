/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal.selfsatip;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;

import org.openhab.binding.selfsatip.internal.http.HTTPRequest;
import org.openhab.binding.selfsatip.internal.http.JsonResponse;

/**
 * Executes a request in order to retrieve information from the antenna, using a particular SELFSAT-IP {@link Command}.
 *
 * @author Marco Georgi - Initial contribution
 */
public class InfoReader<T>
{
    /**
     * The antenna session
     */
    private Session session;

    /**
     * The command to execute
     */
    private Command<T> command;

    /**
     * Constructor accepting a antenna session and the command to be executed.
     *
     * @param session the session to use
     * @param command the command t execute
     */
    public InfoReader( Session session, Command<T> command )
    {
        if( session == null )
            throw new IllegalArgumentException( "Session must no be null" );

        this.session = session;
        this.command = command;
    }

    /**
     * Executes the request and return the received response.
     *
     * @return the response
     * @throws IOException if something went wrong while executing the command
     */
    public T read()
        throws IOException
    {
        if( session.hasExpired() )
            throw new IllegalStateException( "Session has expired" );

        Stream<Map.Entry<String, String>> parameters =
            Stream.of(
                new AbstractMap.SimpleEntry<>( "cmd", command.getCmd() ) );

        JsonResponse<T> jsonResponse = new JsonResponse<>( command.getResponseClass() );
        HTTPRequest.post( session, command.getPath(), parameters, jsonResponse );

        return jsonResponse.get();
    }
}
