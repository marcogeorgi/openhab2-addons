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
package org.openhab.binding.selfsatip.internal;

/**
 * This class contains fields mapping the Thing configuration parameters.
 *
 * @author Marco Georgi - Initial contribution
 */
public class SelfsatipConfiguration
{
    /**
     * The selfsat-ip host name in the local network.
     */
    public String host;

    /**
     * The selfsat-ip server user name for the administration interface.
     */
    public String user;

    /**
     * The selfsat-ip server user password for the administration interface.
     */
    public String password;

    /**
     * The selfsat-ip server refresh interval.
     */
    public int refresh;

    /**
     * Address of a SAT-IP playlist to be used by the station mapper.
     */
    public String playlistAddress;

    /**
     * Playlist refresh interval.
     */
    public int playlistRefresh;
}
