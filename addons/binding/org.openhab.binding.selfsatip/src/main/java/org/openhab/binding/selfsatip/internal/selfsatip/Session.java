/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal.selfsatip;

import java.net.HttpCookie;

/**
 * A session held with the antenna. The session is represented by a HTTP cookie.
 *
 * @author Marco Georgi - Initial contribution
 */
public class Session implements AutoCloseable
{
    /**
     * The session cookie.
     */
    private HttpCookie ticket;

    /**
     * The session host and port.
     */
    private String hostAndPort;

    /**
     * Constructor accepting the host and the associated cookie.
     *
     * @param hostAndPort the session host
     * @param ticket      the session cookie
     */
    Session( String hostAndPort, HttpCookie ticket )
    {
        this.ticket = ticket;
        this.hostAndPort = hostAndPort;
    }

    /**
     * Closes the session.
     *
     * @throws Exception if something unexpected happens
     */
    @Override
    public void close() throws Exception
    {
        new SessionManager().close( this );
    }

    /**
     * Returns the session cookie.
     *
     * @return the session cookie
     */
    public HttpCookie getTicket()
    {
        return ticket;
    }

    /**
     * Returns the flag indicating whether the session cookie has expired.
     *
     * @return flag indicating whether the session cookie has expired
     */
    public boolean hasExpired()
    {
        return ticket.hasExpired();
    }

    /**
     * Returns the remote host of this session.
     *
     * @return the remote host of this session
     */
    public String getHostAndPort()
    {
        return hostAndPort;
    }
}
