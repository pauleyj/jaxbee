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

package com.acme.jaxbee.api.core;

import com.acme.jaxbee.api.core.RxFrame;

/**
 * The interface X bee listener.
 */
public interface XBeeListener {
    /**
     * On receive frame.
     *
     * @param frame the frame
     */
    void onReceiveFrame(final RxFrame frame);
}
