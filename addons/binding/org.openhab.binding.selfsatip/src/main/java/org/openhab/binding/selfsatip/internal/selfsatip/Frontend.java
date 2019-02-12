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
 * Frontend data container.
 *
 * @author Marco Georgi - Initial contribution
 */
public class Frontend
{
    @SerializedName("frontend")
    public TunerInfo tunerInfo = null;
}
