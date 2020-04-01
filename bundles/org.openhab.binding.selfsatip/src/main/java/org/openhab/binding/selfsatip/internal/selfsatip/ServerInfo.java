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

import com.google.gson.annotations.SerializedName;

/**
 * Server Info data container.
 *
 * @author Marco Georgi - Initial contribution
 */
public class ServerInfo
{
    @SerializedName("fwv")
    public String firmwareVersion = null;

    @SerializedName("hwv")
    public String hardwareVersion = null;

    @SerializedName("sdi")
    public String deviceId = null;

    @SerializedName("sfn")
    public String deviceName = null;

    @SerializedName("nsf")
    public int numberOfFrontends = 0;

    //unknown
    public String ntf;
    // unknown
    public String ncf;

    @SerializedName("nca")
    public int numberOfClients = 0;

    @SerializedName("top")
    public int sessionTimeout = 0;

    @SerializedName("dnt")
    public String time = null;

    @SerializedName("cpu")
    public int cpuLoad = 0;

    @SerializedName("mem")
    public int memoryUtilization = 0;

    @SerializedName("upt")
    public String uptime = null;

    @SerializedName("mac")
    public String macAddress = null;
}
