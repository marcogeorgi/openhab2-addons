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
package org.openhab.binding.selfsatip.internal.http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gson.Gson;

/**
 * A {@link HTTPResponseConsumer} implementation that parses a JSON response.
 *
 * @author Marco Georgi - Initial contribution
 */
public class JsonResponse<T> extends HTTPResponseConsumer implements Supplier<T>
{
    /**
     * The parsed JSON response
     */
    private T responseObject;

    /**
     * The expected class of the response
     */
    private Class<T> classOfResponse;

    /**
     * Constructor accepting the expected JSON response class.
     *
     * @param classOfResponse the expected JSON response class
     */
    public JsonResponse( Class<T> classOfResponse )
    {
        this.classOfResponse = classOfResponse;
    }

    /**
     * Returns a JSON parsing stream consumer.
     *
     * @return a JSON parsing stream consumer
     */
    @Override
    public Consumer<InputStream> getResponseStreamConsumer()
    {
        return
            inputStream ->
            {
                Gson jsonParser = new Gson();

                InputStreamReader reader = new InputStreamReader( inputStream );
                set( jsonParser.fromJson( reader, classOfResponse ) );
            };
    }

    /**
     * Sets the parsed response object.
     *
     * @param responseObject the parsed response object
     */
    private void set( T responseObject )
    {
        this.responseObject = responseObject;
    }

    /**
     * Returns the parsed JSON. This can be <code>null</code> if the consumer was not invoked.
     *
     * @return the parsed JSON object, can be <code>null</code>
     */
    public T get()
    {
        return responseObject;
    }
}
