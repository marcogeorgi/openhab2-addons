/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal.satip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.selfsatip.internal.http.HTTPRequest;
import org.openhab.binding.selfsatip.internal.http.HTTPResponseConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A station mapper provides mapping capabilities to resolve TV station stream URLs
 * into user friendly TV station names.
 * <p/>
 * As the mapping source a <code>EXTM3U</code> formatted SAT-IP playlist is used.
 * <p/>
 * Finally, the actual mapping is based on the provided TV station specific
 * data like frequency, polarisation, symbol rate and PIDs. That data is being used to identify
 * a particular playlist entry. The TV {@link Station} of the playlist entry gets returned as the mapping result.
 *
 * @author Marco Georgi - Initial contribution
 */
public class StationMapper
{
    /**
     * Logger
     **/
    private final Logger logger = LoggerFactory.getLogger( StationMapper.class );

    /**
     * Internal mapping structure, extracted from the SAT-IP playlist
     **/
    private HashMap<String, Station> stations;

    /**
     * Constructor
     */
    public StationMapper()
    {
        stations = new HashMap<>();
    }

    /**
     * Loads a playlist from the given URL into this mapper instance.
     *
     * @param playlistUrl the SAT-IP playlist URL
     * @throws IOException thrown if the playlist can't be read from the specified URL
     */
    public void load( String playlistUrl )
        throws IOException
    {
        logger.debug( "attempt to load playlist from: {}", playlistUrl );

        HTTPRequest.get( playlistUrl, new HTTPResponseConsumer()
        {
            @Override
            public Consumer<InputStream> getResponseStreamConsumer()
            {
                return new PlaylistInputStreamReader();
            }
        } );
    }

    /**
     * Loads a playlist from the given file into this mapper instance.
     *
     * @param playlistFile the SAT-IP playlist file
     * @throws IOException thrown if the playlist can't be read from the specified URL
     */
    public void load( File playlistFile )
        throws IOException
    {
        if( playlistFile == null )
        {
            throw new IllegalArgumentException( "Playlist file must not bu null" );
        }

        logger.debug( "attempt to load playlist from: {}", playlistFile.getCanonicalPath() );

        try( InputStream inputStream = new FileInputStream( playlistFile ) )
        {
            new PlaylistInputStreamReader().accept( inputStream );
        }
    }

    /**
     * Consumer class that parses a playlist input stream.
     */
    private class PlaylistInputStreamReader implements Consumer<InputStream>
    {
        /**
         * EXTINF regex pattern to parse TV station title
         */
        private final Pattern extinfoTitlePattern   = Pattern.compile( "^#EXTINF:(?:.*)?,([^,]+)$" );

        /**
         * EXTINF regex pattern to parse TV Guide ID
         */
        private final Pattern extinfoTVGIdPattern   = Pattern.compile( "^#EXTINF:(?:.*tvg-id=\"([^\"]+)\".*)?,[^,]+$" );

        /**
         * EXTINF regex pattern to parse TV Guide ID Logo
         */
        private final Pattern extinfoTVGLogoPattern = Pattern.compile( "^#EXTINF:(?:.*tvg-logo=\"([^\"]+)\".*)?,[^,]+$" );

        /**
         * EXTALBUMARTURL regex pattern
         */
        private final Pattern albumArtPattern = Pattern.compile( "^#EXTALBUMARTURL:(.*)$" );

        @Override
        public void accept( InputStream inputStream )
        {
            BufferedReader playlistReader = new BufferedReader( new InputStreamReader( inputStream ) );
            try
            {
                String line = playlistReader.readLine();

                // first things first
                if( !line.startsWith( "#EXTM3U" ) )
                {
                    throw new RuntimeException( "Unknown playlist format" );
                }

                logger.debug( "start reading playlist content" );

                String title = null;
                String tvgId = null;
                String logo = null;
                String albumArt = null;

                while( line != null )
                {
                    logger.trace( line );

                    Matcher extInfTitleMatcher = extinfoTitlePattern.matcher( line );
                    Matcher extInfTVGIdMatcher = extinfoTVGIdPattern.matcher( line );
                    Matcher extInfTVGLogoMatcher = extinfoTVGLogoPattern.matcher( line );
                    Matcher albumArtMatcher = albumArtPattern.matcher( line );

                    if( extInfTitleMatcher.find() )
                    {
                        title = extInfTitleMatcher.group( 1 );
                    }

                    if( extInfTVGIdMatcher.find() )
                    {
                        tvgId = extInfTVGIdMatcher.group( 1 );
                    }

                    if( extInfTVGLogoMatcher.find() )
                    {
                        logo = extInfTVGLogoMatcher.group( 1 );
                    }

                    if( albumArtMatcher.find() )
                    {
                        albumArt = albumArtMatcher.group( 1 );
                    }

                    if( !line.startsWith( "#" ) )
                    {
                        try
                        {
                            // parse frequency etc...
                            // src=1&freq=11494&pol=h&ro=0.35&msys=dvbs2&mtype=8psk&plts=on&sr=22000&fec=23&pids=0,17,18,5100,5101,5102,5104
                            Map<String, String> parameters = splitQuery( (new URL( line )).getQuery() );

                            String frequency = parameters.get( "freq" );
                            String polarisation = parameters.get( "pol" );
                            String symbolRate = parameters.get( "sr" );
                            String pids = parameters.get( "pids" );

                            if( title != null && !title.trim().isEmpty()
                                && frequency != null && polarisation != null && symbolRate != null && pids != null )
                            {
                                stations.put( keyFor( Double.parseDouble( frequency ), polarisation, Integer.parseInt( symbolRate ), pids ), new Station( title
                                    .trim(), tvgId, logo, albumArt ) );

                                logger.debug( "registered station: {}", title );

                                title = null;
                                tvgId = null;
                                logo = null;
                                albumArt = null;
                            }
                        }
                        catch( MalformedURLException | NumberFormatException exception )
                        {
                            logger.info( "error parsing stream URL {}: {}", line, exception.getMessage() );
                        }
                    }
                    line = playlistReader.readLine();
                }
            }
            catch( IOException ioException )
            {
                throw new RuntimeException( "Error reading playlist", ioException );
            }
        }
    }

    /**
     * Resolves the TV station name based on the given tuner information.
     *
     * @param frequency    TV station frequency
     * @param polarisation TV station polarisation
     * @param symbolRate   TV station symbol rate
     * @param pids         TV station PIDs
     * @return the found TV station name or <code>null</code> if resolving was not successful
     */
    public Station map( double frequency, String polarisation, int symbolRate, String pids )
    {
        return stations.get( keyFor( frequency, polarisation, symbolRate, pids ) );
    }

    /**
     * Returns a collection of known (mapped) TV stations.
     *
     * @return a collection of TV stations
     */
    public Collection<Station> getStations()
    {
        return stations.values();
    }

    /**
     * Creates a mapping key using the specified data.
     *
     * @param frequency    TV station frequency
     * @param polarisation TV station polarisation
     * @param symbolRate   TV station symbol rate
     * @param pids         TV station PIDs
     * @return a calculated mapping key
     */
    private String keyFor( double frequency, String polarisation, int symbolRate, String pids )
    {
        // check input
        if( polarisation == null )
        {
            throw new IllegalArgumentException( "Polarization must not be null or empty" );
        }

        if( pids == null )
        {
            throw new IllegalArgumentException( "PIDs must not be null or empty" );
        }

        // normalize data
        String frequencyNorm = "" + frequency;
        String polarizationNorm = polarisation.toLowerCase();

        // construct key
        return frequencyNorm + "#" + polarizationNorm + "#" + symbolRate + "#" + pids;
    }

    /**
     * Convenience method for splitting a HTTP query string.
     *
     * @param query the HTTP query t split
     * @return a map containing the query parameter
     * @throws UnsupportedEncodingException if an error occurred while parsing the query string
     */
    private Map<String, String> splitQuery( String query ) throws UnsupportedEncodingException
    {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = query.split( "&" );
        for( String pair : pairs )
        {
            int idx = pair.indexOf( "=" );
            if (idx > 0)
            {
                query_pairs.put( URLDecoder.decode( pair.substring( 0, idx ), "UTF-8" ), URLDecoder.decode( pair.substring( idx + 1 ), "UTF-8" ) );
            }
        }
        return query_pairs;
    }
}
