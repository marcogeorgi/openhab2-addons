/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
