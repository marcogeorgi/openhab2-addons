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
package org.openhab.binding.selfsatip.internal.satip;

/**
 * Class represents TV station specific data like title, TV guide ID and a logo.
 *
 * @author Marco Georgi - Initial contribution
 */
public class Station
{
    /**
     * TV station title
     */
    private String title;

    /**
     * A TV guide ID for the TV station
     */
    private String tvgId;

    /**
     * A TV guide logo file name
     */
    private String tvgLogo;

    /**
     * A TV station logo URL
     */
    private String logoUrl;

    /**
     * Constructor accepting all TV station data.
     *
     * @param title   the title
     * @param tvgId   the TV guide ID
     * @param tvgLogo the TV Guide logo
     * @param logoUrl the logo URL
     */
    public Station( String title, String tvgId, String tvgLogo, String logoUrl )
    {
        this.title = title;
        this.tvgId = tvgId;
        this.tvgLogo = tvgLogo;
        this.logoUrl = logoUrl;
    }

    /**
     * Returns the TV station title.
     *
     * @return the TV station title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Returns the TV guide ID.
     *
     * @return the TV guide ID
     */
    public String getTvgId()
    {
        return tvgId;
    }

    /**
     * Returns the TV station logo.
     *
     * @return the TV station logo
     */
    public String getTvgLogo()
    {
        return tvgLogo;
    }

    /**
     * Returns the TV station album art as specified in the playlist.
     *
     * @return the TV station album art
     */
    public String getLogoUrl()
    {
        return logoUrl;
    }
}
