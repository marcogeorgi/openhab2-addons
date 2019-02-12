/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal.selfsatip;

/**
 * Authentication exception.
 *
 * @author Marco Georgi - Initial contribution
 */
public class AuthenticationException extends Exception
{
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
