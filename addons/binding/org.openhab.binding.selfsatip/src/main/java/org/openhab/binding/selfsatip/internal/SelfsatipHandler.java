/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal;

import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_CPU_LOAD;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_FREQUENCY;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_GROUP_BASE_TUNER;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_GROUP_GENERAL_INFO;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_IP_ADDRESS;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_IP_ADDRESS_RESOLVED;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_LOCK;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_LOGO;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_LOGO_URL;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_MAX_SIGNAL_LEVEL;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_MAX_SIGNAL_QUALITY;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_MEMORY_UTILIZATION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_MIN_SIGNAL_LEVEL;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_MIN_SIGNAL_QUALITY;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_MODE;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_MODULATION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_NAME;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_NOOF_CLIENTS;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_PIDS;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_PLAYING;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_POLARISATION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_RECEPTION_TYPE;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_SIGNAL_LEVEL;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_SIGNAL_LEVEL_NORM;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_SIGNAL_QUALITY;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_SIGNAL_QUALITY_NORM;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_STATION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_STATUS;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_SYMBOL_RATE;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_TUNER_TYPE;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_TVGID;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.CHANNEL_UPTIME;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_DEVICE_ID;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_DEVICE_NAME;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_FIRMWARE_VERSION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_FRONTENDS;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_GMI_FIRMWARE_VERSION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_GMI_HARDWARE_VERSION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_HARDWARE_VERSION;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_MAC_ADDRESS;
import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.PROPERTY_SESSION_TIMEOUT;
import static org.openhab.binding.selfsatip.internal.selfsatip.Command.BASIC_INFO;
import static org.openhab.binding.selfsatip.internal.selfsatip.Command.GMI_FIRMWARE_VERSION;
import static org.openhab.binding.selfsatip.internal.selfsatip.Command.GMI_HARDWARE_VERSION;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.selfsatip.internal.http.HTTPRequest;
import org.openhab.binding.selfsatip.internal.satip.Station;
import org.openhab.binding.selfsatip.internal.satip.StationMapper;
import org.openhab.binding.selfsatip.internal.selfsatip.AuthenticationException;
import org.openhab.binding.selfsatip.internal.selfsatip.BasicInfo;
import org.openhab.binding.selfsatip.internal.selfsatip.Frontend;
import org.openhab.binding.selfsatip.internal.selfsatip.InfoReader;
import org.openhab.binding.selfsatip.internal.selfsatip.ServerInfo;
import org.openhab.binding.selfsatip.internal.selfsatip.Session;
import org.openhab.binding.selfsatip.internal.selfsatip.SessionManager;
import org.openhab.binding.selfsatip.internal.selfsatip.TunerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler is responsible for polling status information from a
 * SELFSAT-IP antenna and put them into respective communication channels.
 * <p/>
 * The channels, provided by this handler/binding, are all read only.
 * Thus, all received update commands will be kindly ignored.
 *
 * @author Marco Georgi - Initial contribution
 */
public class SelfsatipHandler extends BaseThingHandler
{
    /**
     * Logger
     **/
    private final Logger logger = LoggerFactory.getLogger( SelfsatipHandler.class );

    /**
     * Delay in seconds before the background jobs run the first time
     **/
    private final static int INITIAL_REFRESH_JOB_DELAY_SEC = 5;

    /**
     * A empty antenna information instance
     **/
    private final static BasicInfo EMPTY_BASIC_INFO;

    static
    {
        // init empty antenna information
        EMPTY_BASIC_INFO = new BasicInfo();
        EMPTY_BASIC_INFO.serverInfo = new ServerInfo();
        EMPTY_BASIC_INFO.frontends = new Frontend[8];
        Frontend emptyFrontend = new Frontend();
        emptyFrontend.tunerInfo = new TunerInfo();
        Arrays.fill( EMPTY_BASIC_INFO.frontends, emptyFrontend );
    }

    /**
     * A optional {@link StationMapper}, that - if configured - will be used to map various
     * {@link TunerInfo} antenna data to data read from a SAT-IP playlist,
     * in order to extract user friendly TV station names.
     */
    private StationMapper stationMapper;

    /**
     * Job for refreshing the antenna information.
     */
    private ScheduledFuture<?> refreshAntennaInfoJob;

    /**
     * Job for refreshing the playlist for the station mapping.
     */
    private ScheduledFuture<?> refreshStationMappingJob;

    /**
     * Constructor accepting the {@link Thing} instance to care about.
     *
     * @param thing the antenna Thing to care about
     */
    SelfsatipHandler( Thing thing )
    {
        super( thing );
    }

    /**
     * Starts the background jobs for this Thing handler.
     */
    @Override
    public void initialize()
    {
        logger.debug( "start initializing!" );
        updateStatus( ThingStatus.UNKNOWN );

        try
        {
            // get and log config
            SelfsatipConfiguration config = getConfigAs( SelfsatipConfiguration.class );

            logger.debug( "using host:        {}", config.host );
            logger.debug( "sign in as:        {}", config.user );
            logger.debug( "password provided: {}", config.password != null && !config.password.isEmpty() );
            logger.debug( "refresh:           {}", config.refresh );
            logger.debug( "playlist address:  {}", config.playlistAddress );
            logger.debug( "playlist refresh:  {}", config.playlistRefresh );

            startJobs( config );
        }
        catch( IllegalArgumentException illegalArgumentException )
        {
            logger.error( "invalid configuration: {}", illegalArgumentException.getMessage() );
            updateStatus( ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, illegalArgumentException.getMessage() );
        }

        logger.debug( "finished initializing!" );
    }

    /**
     * Stops the background jobs of this Thing handler
     * and sets the Thing to {@link ThingStatus#OFFLINE} state.
     */
    @Override
    public void dispose()
    {
        // stop jobs
        stopJobs();

        // set antenna offline
        updateStatus( ThingStatus.OFFLINE );

        super.dispose();
    }

    /**
     * Since all antenna information are considered read only, all update requests will be ignored
     * and logged.
     */
    @Override
    public void handleCommand( ChannelUID channelUID, Command command )
    {
        logger.debug( "not implemented / command not handled: {} for channel: {}", command, channelUID );
    }

    /**
     * Starts the background jobs for this Thing handler.
     * <p/>
     * The antenna update job gets started if a {@link SelfsatipConfiguration#refresh} interval
     * greater than 0 (seconds) is configured.
     * <p/>
     * The job for updating the SAT-IP playlist gets started if a {@link SelfsatipConfiguration#playlistRefresh} interval
     * greater than 0 (hours) as well as a {@link SelfsatipConfiguration#playlistAddress} is configured.
     *
     * @param config the Thing configuration
     */
    private void startJobs( SelfsatipConfiguration config )
    {
        // start job to pull antenna information
        if( config.refresh > 0 )
        {
            // check job not yet exists or was cancelled previously
            if( (refreshAntennaInfoJob == null || refreshAntennaInfoJob.isCancelled()) )
            {
                refreshAntennaInfoJob = scheduler.scheduleWithFixedDelay(
                    new RetrieveAntennaInformation( config.host, config.user, config.password ),
                    INITIAL_REFRESH_JOB_DELAY_SEC,
                    config.refresh, TimeUnit.SECONDS );
            }
            else
            {
                logger.debug( "job for refreshing antenna information seems already running" );
            }
        }
        else
        {
            logger.info( "job for refreshing antenna information not started as its refresh interval is configured to {}", config.refresh );
        }

        // start job to refresh station playlist
        boolean playlistConfigured = config.playlistAddress != null && !config.playlistAddress.isEmpty();
        if( playlistConfigured && config.playlistRefresh > 0 )
        {
            // check job not yet exists or was cancelled previously
            if( (refreshStationMappingJob == null || refreshStationMappingJob.isCancelled()) )
            {
                refreshStationMappingJob = scheduler.scheduleWithFixedDelay(
                    new RetrieveSatIpPlaylist( config.playlistAddress ),
                    INITIAL_REFRESH_JOB_DELAY_SEC,
                    config.playlistRefresh * 60 * 60, TimeUnit.SECONDS );
            }
            else
            {
                logger.debug( "job for refreshing station playlist seems already running" );
            }
        }
        else
        {
            logger.info( "job for refreshing station playlist not started as its not properly configured (playlist: {}, refresh: {})", playlistConfigured, config.refresh );
        }
    }

    /**
     * Stops the running background jobs.
     */
    private void stopJobs()
    {
        // stop antenna info job
        if( refreshAntennaInfoJob != null && !refreshAntennaInfoJob.isCancelled() )
        {
            logger.debug( "stopping antenna info update job" );

            refreshAntennaInfoJob.cancel( true );
            refreshAntennaInfoJob = null;
        }

        // stop playlist refresh job
        if( refreshStationMappingJob != null && !refreshStationMappingJob.isCancelled() )
        {
            logger.debug( "stopping playlist update job" );

            refreshStationMappingJob.cancel( true );
            refreshStationMappingJob = null;
        }
    }

    /**
     * Sets the {@link StationMapper} that is being used by this Thing for resolving
     * TV station coordinates into user friendly station names.
     *
     * @param stationMapper the station mapper to use going forward
     */
    private void setStationMapper( StationMapper stationMapper )
    {
        this.stationMapper = stationMapper;
    }

    /**
     * Convenience method to assert NOT <code>null</code> and NOT empty conditions.
     *
     * @param input input to check
     * @param name a name used when raising a exception
     * @return the input if valid
     * @throws IllegalArgumentException if input was <code>null</code> or empty
     */
    private String assertNotEmptyArgument( String input, String name )
    {
        if( input == null || input.isEmpty() )
        {
            throw new IllegalArgumentException( name + " configuration must not be null or empty" );
        }

        return input;
    }

    /**
     * Job that pulls the antenna information from the remote SELFSAT-IP antenna device.
     * If the job fails for some reason, the antenna {@link Thing} will be set {@link ThingStatus#OFFLINE}.
     */
    private class RetrieveAntennaInformation implements Runnable
    {
        /** Logger **/
        private final Logger logger = LoggerFactory.getLogger( RetrieveAntennaInformation.class );

        /**
         * Host address of the antenna.
         */
        private String host;

        /**
         * User to access the antenna.
         */
        private String user;

        /**
         * Password for the user.
         */
        private String password;

        /**
         * Constructor for the job.
         *
         * @param host the host name of the antenna device
         * @param user the user to access the antenna
         * @param password the password for the user
         */
        private RetrieveAntennaInformation( String host, String user, String password )
        {
            this.host = assertNotEmptyArgument( host, "host" );
            this.user = assertNotEmptyArgument( user, "user" );
            this.password = password != null ? password : "";
        }

        @Override
        public void run()
        {
            logger.debug( "executing antenna info update job" );

            // short circuit if thing is not initialized for some reason
            if( !isInitialized() )
            {
                logger.debug( "antenna Thing is not initialized, skipping execution" );
                return;
            }

            // create antenna URL
            String antennaUrl = "http://" + host + ":8000";
            logger.debug( "reaching put to antenna at: {}", antennaUrl );

            try
            {
                // first get firmware and hardware
                String gmiFirmwareVersion = HTTPRequest.get( antennaUrl + "/" + GMI_FIRMWARE_VERSION.getPath() );
                String gmiHardwareVersion = HTTPRequest.get( antennaUrl + "/" + GMI_HARDWARE_VERSION.getPath() );
                publishGmiVersions( emptyIfNull( gmiFirmwareVersion ), emptyIfNull( gmiHardwareVersion ) );

                // open session and pull data
                try( Session selfsatSession = new SessionManager().start( antennaUrl, user, password ) )
                {
                    logger.debug( "obtained antenna API ticket: {}", selfsatSession.getTicket() );

                    // query data from antenna
                    InfoReader<BasicInfo> infoReader = new InfoReader<>( selfsatSession, BASIC_INFO );
                    BasicInfo basicInfo = infoReader.read();

                    // some checks
                    assertNotNullState( basicInfo, "received none or invalid data (basic info)" );
                    assertNotNullState( basicInfo.serverInfo, "received invalid data (server info)" );
                    assertNotNullState( basicInfo.frontends, "received invalid data (frontends info)" );
                    Stream.of( basicInfo.frontends ).forEach( frontend ->
                        assertNotNullState( frontend.tunerInfo, "received invalid data (tuner info)" ) );

                    logger.debug( "successfully received antenna info at antenna time: {}", basicInfo.serverInfo.time );

                    // publish received antenna data
                    publish( basicInfo );

                    // if thing was offline and job succeeded, set thing online again
                    if( !ThingStatus.ONLINE.equals( getThing().getStatus() ) )
                    {
                        updateStatus( ThingStatus.ONLINE );
                    }

                    logger.debug( "successfully updated antenna info" );
                }
                catch( AuthenticationException authenticationException )
                {
                    setThingOffline( "authentication failed" );
                }
            }
            catch( UnknownHostException unknownHostException )
            {
                setThingOffline( "unknown host" );
            }

            catch( Throwable anyException )
            {
                setThingOffline( "unexpected error while accessing the antenna: " + anyException.getMessage() );
            }
        }

        /**
         * Set the antenna offline using the given error indicator.
         *
         * @param message the error indicator to log/associate
         */
        private void setThingOffline( String message )
        {
            if (!isInitialized())
            {
                return;
            }

            if( ThingStatus.OFFLINE != getThing().getStatus() )
            {
                logger.warn( "setting Thing to OFFLINE due to communication error: {}", message );

                // set offline if job failed
                updateStatus( ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, message );

                // reset all data channels
                publish( EMPTY_BASIC_INFO );
            }
            else
            {
                logger.debug( "keeping Thing OFFLINE due to communication error: {}", message );
            }
        }

        /**
         * Pushes the GMI hardware and firmware versions to respective properties.
         *
         * @param gmiFirmwareVersion gmi firmware version
         * @param gmiHardwareVersion gmi hardware version
         */
        private void publishGmiVersions( String gmiFirmwareVersion, String gmiHardwareVersion )
        {
            Map<String, String> properties = editProperties();
            properties.put( PROPERTY_GMI_FIRMWARE_VERSION, gmiFirmwareVersion );
            properties.put( PROPERTY_GMI_HARDWARE_VERSION, gmiHardwareVersion );
            updateProperties( properties );
        }

        /**
         * Pushes the received antenna date into its respective channels.
         *
         * @param basicInfo the information to publish
         */
        private void publish( BasicInfo basicInfo )
        {
            // set properties first
            logger.debug( "applying antenna properties" );
            Map<String, String> properties = editProperties();
            properties.put( PROPERTY_DEVICE_NAME, emptyIfNull( basicInfo.serverInfo.deviceName ) );
            properties.put( PROPERTY_DEVICE_ID, emptyIfNull( basicInfo.serverInfo.deviceId ) );
            properties.put( PROPERTY_MAC_ADDRESS, emptyIfNull( basicInfo.serverInfo.macAddress ) );
            properties.put( PROPERTY_FIRMWARE_VERSION, emptyIfNull( basicInfo.serverInfo.firmwareVersion ) );
            properties.put( PROPERTY_HARDWARE_VERSION, emptyIfNull( basicInfo.serverInfo.hardwareVersion ) );
            properties.put( PROPERTY_FRONTENDS, "" + basicInfo.serverInfo.numberOfFrontends );
            properties.put( PROPERTY_SESSION_TIMEOUT, "" + basicInfo.serverInfo.sessionTimeout );
            updateProperties( properties );

            // push changes into channels
            logger.debug( "applying general antenna info" );
            updateGeneral( CHANNEL_STATUS, new StringType( basicInfo.status ) );
            updateGeneral( CHANNEL_UPTIME, new StringType( basicInfo.serverInfo.uptime ) );
            updateGeneral( CHANNEL_CPU_LOAD, new DecimalType( basicInfo.serverInfo.cpuLoad ) );
            updateGeneral( CHANNEL_MEMORY_UTILIZATION, new DecimalType( basicInfo.serverInfo.memoryUtilization ) );
            updateGeneral( CHANNEL_NOOF_CLIENTS, new DecimalType( basicInfo.serverInfo.numberOfClients ) );
            updateGeneral( CHANNEL_MIN_SIGNAL_QUALITY, new DecimalType( basicInfo.getMinSignalQuality() ) );
            updateGeneral( CHANNEL_MAX_SIGNAL_QUALITY, new DecimalType( basicInfo.getMaxSignalQuality() ) );
            updateGeneral( CHANNEL_MIN_SIGNAL_LEVEL, new DecimalType( basicInfo.getMinSignalLevel() ) );
            updateGeneral( CHANNEL_MAX_SIGNAL_LEVEL, new DecimalType( basicInfo.getMaxSignalLevel() ) );

            for( int i = 1; i <= basicInfo.frontends.length; i++ )
            {
                logger.debug( "applying info for tuner: {}", i );

                TunerInfo tunerInfo = basicInfo.frontends[i - 1].tunerInfo;

                updateTuner( i, CHANNEL_NAME, new StringType( tunerInfo.name ) );
                updateTuner( i, CHANNEL_TUNER_TYPE, new StringType( tunerInfo.tunerType ) );
                updateTuner( i, CHANNEL_MODE, new StringType( tunerInfo.mode ) );
                updateTuner( i, CHANNEL_IP_ADDRESS, new StringType( tunerInfo.clientIpAddress ) );
                updateTuner( i, CHANNEL_IP_ADDRESS_RESOLVED, new StringType( emptyIfNull( tunerInfo.getTranslatedIpAddress() ) ) );
                updateTuner( i, CHANNEL_PLAYING, tunerInfo.isPlaying() ? OnOffType.ON : OnOffType.OFF );
                updateTuner( i, CHANNEL_LOCK, tunerInfo.isLocked() ? OnOffType.ON : OnOffType.OFF );
                updateTuner( i, CHANNEL_SIGNAL_QUALITY, new DecimalType( tunerInfo.signalQuality ) );
                updateTuner( i, CHANNEL_SIGNAL_LEVEL, new DecimalType( tunerInfo.signalLevel ) );
                updateTuner( i, CHANNEL_FREQUENCY, new DecimalType( tunerInfo.frequency ) );
                updateTuner( i, CHANNEL_SYMBOL_RATE, new DecimalType( tunerInfo.symbolRate ) );
                updateTuner( i, CHANNEL_POLARISATION, new StringType( tunerInfo.polarisation ) );
                updateTuner( i, CHANNEL_RECEPTION_TYPE, new StringType( tunerInfo.receptionType ) );
                updateTuner( i, CHANNEL_MODULATION, new StringType( tunerInfo.modulation ) );
                updateTuner( i, CHANNEL_PIDS, new StringType( tunerInfo.pids ) );
                updateTuner( i, CHANNEL_SIGNAL_QUALITY_NORM, new DecimalType( tunerInfo.getTranslatedSignalQuality() ) );
                updateTuner( i, CHANNEL_SIGNAL_LEVEL_NORM, new DecimalType( tunerInfo.getTranslatedSignalLevel() ) );

                // try to resolve station (if a mapper is available)
                if ( stationMapper != null )
                {
                    Station station = tunerInfo.polarisation != null && tunerInfo.pids != null
                        ? stationMapper.map( tunerInfo.frequency, tunerInfo.polarisation, tunerInfo.symbolRate, tunerInfo.pids )
                        : null;

                    // if station was found, update according channels
                    if( station != null )
                    {
                        updateTuner( i, CHANNEL_STATION, new StringType( emptyIfNull( station.getTitle() ) ) );
                        updateTuner( i, CHANNEL_TVGID, new StringType( emptyIfNull( station.getTvgId() ) ) );
                        updateTuner( i, CHANNEL_LOGO, new StringType( emptyIfNull( station.getTvgLogo() ) ) );
                        updateTuner( i, CHANNEL_LOGO_URL, new StringType( emptyIfNull( station.getLogoUrl() ) ) );
                    }
                    else
                    {
                        updateTuner( i, CHANNEL_STATION, new StringType( "" ) ) ;
                        updateTuner( i, CHANNEL_TVGID, new StringType( "" ) );
                        updateTuner( i, CHANNEL_LOGO, new StringType( "" ) );
                        updateTuner( i, CHANNEL_LOGO_URL, new StringType( "" ) );
                    }
                }
            }
        }

        /**
         * Updates a Channel in the {@link SelfsatipBindingConstants#CHANNEL_GROUP_GENERAL_INFO} group.
         *
         * @param channelId the Channel to update
         * @param state the state to set
         */
        private void updateGeneral( String channelId, State state )
        {
            logger.trace( "setting Channel {}, value: {}", channelId, state.toString() );
            updateState( new ChannelUID( thing.getUID(), CHANNEL_GROUP_GENERAL_INFO, channelId ), state );
        }

        /**
         * Updates a Channel in the {@link SelfsatipBindingConstants#CHANNEL_GROUP_BASE_TUNER} group.
         *
         * @param channelId the Channel to update
         * @param state the state to set
         */
        private void updateTuner( int tuner, String channelId, State state )
        {
            logger.trace( "setting tuner {}, Channel {}, value: {}", tuner, channelId, state.toString() );
            updateState( new ChannelUID( thing.getUID(), CHANNEL_GROUP_BASE_TUNER + tuner, channelId ), state );
        }

        /**
         * Returns an empty string in case the input was <code>null</code>, the original input otherwise.
         *
         * @param input input string
         * @return the original input or an empty string if the input was <code>null</code>
         */
        private String emptyIfNull( String input )
        {
            return input == null ? "" : input;
        }

        /**
         * Assertion for NOT <code>null</code> conditions.
         *
         * @param input input to check
         * @param message a message to use when raising a exception
         * @return the input if valid
         * @throws IllegalStateException if input was <code>null</code>
         */
        private <T> T assertNotNullState( T input, String message )
        {
            if( input == null )
            {
                throw new IllegalStateException( message );
            }

            return input;
        }
    }

    /**
     * Job that retrieves the SAT-IP playlist, which is used for station mapping.
     */
    private class RetrieveSatIpPlaylist implements Runnable
    {
        private final Logger logger = LoggerFactory.getLogger( RetrieveAntennaInformation.class );

        /**
         * Playlist URL.
         */
        private String playlistUrl;

        private RetrieveSatIpPlaylist( String playlistUrl )
        {
            this.playlistUrl = assertNotEmptyArgument( playlistUrl, "playlist URL" );
        }

        @Override
        public void run()
        {
            logger.debug( "retrieving playlist now" );

            // short circuit if thing is not initialized for some reason
            if( !isInitialized() )
            {
                logger.debug( "antenna Thing is not initialized, skipping execution" );
                return;
            }

            try
            {
                // create and load the playlist
                StationMapper stationMapper = new StationMapper();
                stationMapper.load( playlistUrl );

                // publish new station mapper
                setStationMapper( stationMapper );

                logger.debug( "done retrieving playlist" );
            }
            catch( Throwable anyException )
            {
                logger.info( "unexpected error while retrieving the SAT-IP playlist: {}", anyException.getMessage() );
            }
        }
    }
}
