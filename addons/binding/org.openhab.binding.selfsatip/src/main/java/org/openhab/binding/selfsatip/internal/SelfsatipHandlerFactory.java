/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.selfsatip.internal;

import static org.openhab.binding.selfsatip.internal.SelfsatipBindingConstants.THING_TYPE_ID_ANTENNA;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This factory is responsible for creating Things and Thing handlers.
 *
 * @author Marco Georgi - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.selfsatip", service = ThingHandlerFactory.class)
public class SelfsatipHandlerFactory extends BaseThingHandlerFactory
{
    /** Logger **/
    private final Logger logger = LoggerFactory.getLogger( SelfsatipHandlerFactory.class );

    /**
     * Currently only one Thing is supported by the binding: {@link SelfsatipBindingConstants#THING_TYPE_ID_ANTENNA}.
     */
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton( THING_TYPE_ID_ANTENNA );

    @Override
    public boolean supportsThingType( ThingTypeUID thingTypeUID )
    {
        return SUPPORTED_THING_TYPES_UIDS.contains( thingTypeUID );
    }

    @Override
    protected @Nullable ThingHandler createHandler( Thing thing )
    {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if( THING_TYPE_ID_ANTENNA.equals( thingTypeUID ) )
        {
            logger.debug( "creating new Thing handler now for (thing type {}): {}", thingTypeUID.toString(), thing.getUID() );
            return new SelfsatipHandler( thing );
        }

        return null;
    }
}
