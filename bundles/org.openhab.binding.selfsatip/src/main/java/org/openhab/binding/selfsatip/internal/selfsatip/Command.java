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
 * SELFSAT-IP command.
 *
 * @author Marco Georgi - Initial contribution
 */
public class Command<T>
{
    /**
     * Login command
     */
    static final Command LOGIN = new Command<>( "/cgi-bin/login.cgi", "login", null );

    /**
     * Logout command
     */
    static final Command LOGOUT = new Command<>( "/cgi-bin/index.cgi", "logout", null );

    /**
     * Basic Info command
     */
    public static final Command<BasicInfo> BASIC_INFO = new Command<>( "/cgi-bin/index.cgi", "basic_info", BasicInfo.class );

    /**
     * GMI software version command
     */
    public static final Command GMI_FIRMWARE_VERSION = new Command<>( "/gmi_sw_ver.txt", null, null );

    /**
     * GMI hardware version command
     */
    public static final Command GMI_HARDWARE_VERSION = new Command<>( "/gmi_hw_ver.txt", null, null );

    /**
     * The path of the command
     */
    private String path;

    /**
     * The command
     */
    private String cmd;

    /**
     * The expected response class
     */
    private Class<T> responseClass;

    /**
     * Constructor.
     *
     * @param path         the path of the command
     * @param cmd          the command
     * @param responseType the expected response type
     */
    private Command( String path, String cmd, Class<T> responseType )
    {
        this.path = path;
        this.cmd = cmd;
        this.responseClass = responseType;
    }

    /**
     * Returns the path of the command.
     *
     * @return the path of the command
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Returns the command as string.
     *
     * @return the command string
     */
    public String getCmd()
    {
        return cmd;
    }

    /**
     * Returns the expected response class.
     *
     * @return the expected response class
     */
    public Class<T> getResponseClass()
    {
        return responseClass;
    }
}
