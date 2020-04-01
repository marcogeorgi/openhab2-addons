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

import java.io.InputStream;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Base class for HTTP response handlers that consume the data stream as well as cookies.
 *
 * @author Marco Georgi - Initial contribution
 */
public class HTTPResponseConsumer
{
    /**
     * A map storing the response headers.
     */
    private Map<String, List<String>> responseHeader;

    /**
     * Constructor
     */
    public HTTPResponseConsumer()
    {
    }

    /**
     * Returns a response stream {@link Consumer}.
     * This base implementation just returns <code>null</code>.
     * Implementing classes should return a proper {@link InputStream} consumer in order to
     * receive the response.
     *
     * @return <node>null</node>
     */
    public Consumer<InputStream> getResponseStreamConsumer()
    {
        return null;
    }

    /**
     * This method will be called in order to apply the received HTTP headers.
     *
     * @param responseHeader map of received response headers
     */
    final void setHeader( Map<String, List<String>> responseHeader )
    {
        this.responseHeader = responseHeader;
    }

    /**
     * Returns a optional cookie for the specified name.
     *
     * @param name the name of the cookie
     * @return the cookie if found
     */
    public Optional<HttpCookie> getCookie( String name )
    {
        return
            responseHeader.entrySet().stream()
                .filter( header -> header.getKey() != null && header.getKey().equals( "Set-Cookie" ) )
                .flatMap( header -> header.getValue().stream() )
                .flatMap( headerLine -> HttpCookie.parse( headerLine ).stream() )
                .filter( cookie -> cookie.getName().equals( name ) )
                .findFirst();
    }
}
