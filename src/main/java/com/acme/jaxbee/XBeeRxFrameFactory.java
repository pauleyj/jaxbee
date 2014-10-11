/*
 * Copyright 2014 John C. Pauley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acme.jaxbee;

import com.acme.jaxbee.api.RxFrame;
import com.acme.jaxbee.api.RxFrameFactory;

/**
 * The interface X bee rx frame factory.
 */
public interface XBeeRxFrameFactory {
    /**
     * New rx frame for api id.
     *
     * @param apiId the api id
     * @return the rx frame
     * @throws XBeeException the x bee exception
     */
    RxFrame newRxFrameForApiId(final byte apiId) throws XBeeException;

    /**
     * Add rx frame factory for api id.
     *
     * @param apiId the api id
     * @param factory the factory
     */
    void addRxFrameFactoryForApiId(final byte apiId, final RxFrameFactory factory);
}
