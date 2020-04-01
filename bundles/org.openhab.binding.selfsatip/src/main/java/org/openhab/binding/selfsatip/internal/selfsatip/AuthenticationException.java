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
package org.openhab.binding.selfsatip.internal.selfsatip;

/**
 * Authentication exception.
 *
 * @author Marco Georgi - Initial contribution
 */
public class AuthenticationException extends Exception
{
    private static final long serialVersionUID = 6093422594625547073L;

    /**
     * The user that failed to authenticate.
     */
    private String user;

    /**
     * Constructor accepting a user for whom the authentication has failed.
     *
     * @param user user for whom the authentication has failed
     */
    public AuthenticationException( String user )
    {
        this.user = user;
    }

    /**
     * Returns the user for whom the authentication has failed.
     * 
     * @return user for whom the authentication has failed
     */
    public String getUser()
    {
        return user;
    }
}
