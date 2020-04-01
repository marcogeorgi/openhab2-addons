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

import java.net.InetAddress;

import com.google.gson.annotations.SerializedName;

/**
 * Tuner Info data container.
 *
 * @author Marco Georgi - Initial contribution
 */
public class TunerInfo
{
    /**
     * Correspond to Java boolean value <code>true</code>
     **/
    private static final String YES = "yes";

    @SerializedName("name")
    public String name = null;

    @SerializedName("tt")
    public String tunerType = null;

    @SerializedName("tm")
    public String mode = null;

    @SerializedName("ip")
    public String clientIpAddress = null;

    @SerializedName("playing")
    public String playing = null;

    @SerializedName("ls")
    public String lockStatus = null;

    @SerializedName("ber")
    public float signalQuality = 0f;

    @SerializedName("sq")
    public float signalLevel = 0f;

    @SerializedName("fq")
    public float frequency = 0f;

    @SerializedName("sr")
    public int symbolRate = 0;

    @SerializedName("pol")
    public String polarisation = null;

    @SerializedName("rs")
    public String receptionType = null;

    @SerializedName("mod")
    public String modulation = null;

    @SerializedName("pids")
    public String pids = null;

    /**
     * Calculates the percentage value of the signal quality from the received raw {@link #signalQuality} value.
     *
     * @return the percentage of the signal quality
     */
    public float getTranslatedSignalQuality()
    {
        return signalQuality * 20 / 3;
    }

    /**
     * Calculates the value of the signal level from the received raw {@link #signalLevel} value.
     *
     * @return the calculated value of the signal level
     */
    public float getTranslatedSignalLevel()
    {
        return signalLevel > 0f ? ((signalLevel * 10) - 3440) / 48 : 0f;
    }

    /**
     * Resolves the received {@link #clientIpAddress} into a host name.
     *
     * @return the resolved host name, can be <code>null</code> if resolving failed
     */
    public String getTranslatedIpAddress()
    {
        if( clientIpAddress != null && !clientIpAddress.isEmpty() )
        {
            try
            {
                InetAddress hostAddress = InetAddress.getByName( clientIpAddress );
                return hostAddress.getCanonicalHostName();
            }
            catch( Exception e )
            {
                // ignore, we just accept not being able to reverse resolve the IP
            }
        }
        return null;
    }

    /**
     * Returns <code>true</code> if {@link #playing} value equals {@link #YES}.
     *
     * @return the boolean representation of the playing flag
     */
    public boolean isPlaying()
    {
        return YES.equalsIgnoreCase( playing );
    }

    /**
     * Returns <code>true</code> if {@link #lockStatus} value equals {@link #YES}.
     *
     * @return the boolean representation of the lock status flag
     */
    public boolean isLocked()
    {
        return YES.equalsIgnoreCase( lockStatus );
    }
}
