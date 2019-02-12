/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal.selfsatip;

import java.util.Comparator;
import java.util.stream.Stream;

import com.google.gson.annotations.SerializedName;

/**
 * Basic info data container received through {@link Command#BASIC_INFO} command.
 *
 * @author Marco Georgi - Initial contribution
 */
public class BasicInfo
{
    @SerializedName("status")
    public String status = null;

    @SerializedName("server_info")
    public ServerInfo serverInfo = null;

    @SerializedName("frontends")
    public Frontend[] frontends = null;

    /**
     * Calculates the maximum signal quality over all currently {@link TunerInfo#isPlaying()} frontends.
     *
     * @return the maximum signal quality
     */
    public float getMaxSignalQuality()
    {
        return Stream.of( frontends )
            .filter( frontend -> frontend.tunerInfo.isPlaying() )
            .max( Comparator.comparing( frontend -> frontend.tunerInfo.getTranslatedSignalQuality() ) )
            .map( frontend -> frontend.tunerInfo.getTranslatedSignalQuality() ).orElse( 0f );
    }

    /**
     * Calculates the minimum signal quality over all currently {@link TunerInfo#isPlaying()} frontends.
     *
     * @return the minimum signal quality
     */
    public float getMinSignalQuality()
    {
        return Stream.of( frontends )
            .filter( frontend -> frontend.tunerInfo.isPlaying() )
            .min( Comparator.comparing( frontend -> frontend.tunerInfo.getTranslatedSignalQuality() ) )
            .map( frontend -> frontend.tunerInfo.getTranslatedSignalQuality() ).orElse( 0f );
    }

    /**
     * Calculates the maximum signal level over all currently {@link TunerInfo#isPlaying()} frontends.
     *
     * @return the maximum signal level
     */
    public float getMaxSignalLevel()
    {
        return Stream.of( frontends )
            .filter( frontend -> frontend.tunerInfo.isPlaying() )
            .max( Comparator.comparing( frontend -> frontend.tunerInfo.getTranslatedSignalLevel() ) )
            .map( frontend -> frontend.tunerInfo.getTranslatedSignalLevel() ).orElse( 0f );
    }

    /**
     * Calculates the minimum signal level over all currently {@link TunerInfo#isPlaying()} frontends.
     *
     * @return the minimum signal level
     */
    public float getMinSignalLevel()
    {
        return Stream.of( frontends )
            .filter( frontend -> frontend.tunerInfo.isPlaying() )
            .min( Comparator.comparing( frontend -> frontend.tunerInfo.getTranslatedSignalLevel() ) )
            .map( frontend -> frontend.tunerInfo.getTranslatedSignalLevel() ).orElse( 0f );
    }
}
