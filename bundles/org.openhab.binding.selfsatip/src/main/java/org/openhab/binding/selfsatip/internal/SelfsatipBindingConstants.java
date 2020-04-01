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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link SelfsatipBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Marco Georgi - Initial contribution
 */
@NonNullByDefault
class SelfsatipBindingConstants
{
    /* SELFSAT-IP Binding ID: <code>selfsatip</code> */
    static final String BINDING_ID = "selfsatip";

    // List of all Thing Type UIDs
    static final ThingTypeUID THING_TYPE_ID_ANTENNA = new ThingTypeUID( BINDING_ID, "antenna" );

    /* Property key: <code>deviceId</code> */
    static final String PROPERTY_DEVICE_ID = "deviceId";

    /* Property key: <code>deviceName</code> */
    static final String PROPERTY_DEVICE_NAME = "deviceName";

    /* Property key: <code>macAddress</code> */
    static final String PROPERTY_MAC_ADDRESS = "macAddress";

    /* Property key: <code>firmwareVersion</code> */
    static final String PROPERTY_FIRMWARE_VERSION = "firmwareVersion";

    /* Property key: <code>hardwareVersion</code> */
    static final String PROPERTY_HARDWARE_VERSION = "hardwareVersion";

    /* Property key: <code>gmiFirmwareVersion</code> */
    static final String PROPERTY_GMI_FIRMWARE_VERSION = "gmiFirmwareVersion";

    /* Property key: <code>gmiHardwareVersion</code> */
    static final String PROPERTY_GMI_HARDWARE_VERSION = "gmiHardwareVersion";

    /* Property key: <code>frontends</code> */
    static final String PROPERTY_FRONTENDS = "frontends";

    /* Property key: <code>sessionTimeout</code> */
    static final String PROPERTY_SESSION_TIMEOUT = "sessionTimeout";

    /* Channel Group: <code>generalInfo</code> */
    static final String CHANNEL_GROUP_GENERAL_INFO = "generalInfo";

    /* Channel Group base name: <code>tuner</code> */
    static final String CHANNEL_GROUP_BASE_TUNER = "tuner";

    /* Channel: <code>status</code> */
    static final String CHANNEL_STATUS = "status";

    /* Channel: <code>uptime</code> */
    static final String CHANNEL_UPTIME = "uptime";

    /* Channel: <code>cpuLoad</code> */
    static final String CHANNEL_CPU_LOAD = "cpuLoad";

    /* Channel: <code>memoryUtilization</code> */
    static final String CHANNEL_MEMORY_UTILIZATION = "memoryUtilization";

    /* Channel: <code>noOfClients</code> */
    static final String CHANNEL_NOOF_CLIENTS = "noOfClients";

    /* Channel: <code>minSignalQuality</code> */
    static final String CHANNEL_MIN_SIGNAL_QUALITY = "minSignalQuality";

    /* Channel: <code>maxSignalQuality</code> */
    static final String CHANNEL_MAX_SIGNAL_QUALITY = "maxSignalQuality";

    /* Channel: <code>minSignalLevel</code> */
    static final String CHANNEL_MIN_SIGNAL_LEVEL = "minSignalLevel";

    /* Channel: <code>maxSignalLevel</code> */
    static final String CHANNEL_MAX_SIGNAL_LEVEL = "maxSignalLevel";

    /* Channel: <code>name</code> */
    static final String CHANNEL_NAME = "name";

    /* Channel: <code>tunerType</code> */
    static final String CHANNEL_TUNER_TYPE = "tunerType";

    /* Channel: <code>mode</code> */
    static final String CHANNEL_MODE = "mode";

    /* Channel: <code>ipAddress</code> */
    static final String CHANNEL_IP_ADDRESS = "ipAddress";

    /* Channel: <code>ipAddressResolved</code> */
    static final String CHANNEL_IP_ADDRESS_RESOLVED = "ipAddressResolved";

    /* Channel: <code>playing</code> */
    static final String CHANNEL_PLAYING = "playing";

    /* Channel: <code>lock</code> */
    static final String CHANNEL_LOCK = "lock";

    /* Channel: <code>signalQuality</code> */
    static final String CHANNEL_SIGNAL_QUALITY = "signalQuality";

    /* Channel: <code>signalQualityNorm</code> */
    static final String CHANNEL_SIGNAL_QUALITY_NORM = "signalQualityNorm";

    /* Channel: <code>signalLevel</code> */
    static final String CHANNEL_SIGNAL_LEVEL = "signalLevel";

    /* Channel: <code>signalLevelNorm</code> */
    static final String CHANNEL_SIGNAL_LEVEL_NORM = "signalLevelNorm";

    /* Channel: <code>frequency</code> */
    static final String CHANNEL_FREQUENCY = "frequency";

    /* Channel: <code>symbolRate</code> */
    static final String CHANNEL_SYMBOL_RATE = "symbolRate";

    /* Channel: <code>polarisation</code> */
    static final String CHANNEL_POLARISATION = "polarisation";

    /* Channel: <code>receptionType</code> */
    static final String CHANNEL_RECEPTION_TYPE = "receptionType";

    /* Channel: <code>modulation</code> */
    static final String CHANNEL_MODULATION = "modulation";

    /* Channel: <code>pids</code> */
    static final String CHANNEL_PIDS = "pids";

    /* Channel: <code>station</code> */
    static final String CHANNEL_STATION = "station";

    /* Channel: <code>tvgId</code> */
    static final String CHANNEL_TVGID = "tvgId";

    /* Channel: <code>logo</code> */
    static final String CHANNEL_LOGO = "logo";

    /* Channel: <code>logoUrl</code> */
    static final String CHANNEL_LOGO_URL = "logoUrl";
}
