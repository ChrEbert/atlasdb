/*
 * Copyright 2017 Palantir Technologies, Inc. All rights reserved.
 *
 * Licensed under the BSD-3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.palantir.atlasdb.ete;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.palantir.timestamp.TimestampManagementService;
import com.palantir.timestamp.TimestampService;

public class ServiceExposureEteTest {
    @Test
    public void shouldExposeATimestampServer() {
        TimestampService timestampClient = EteSetup.createClientToAllNodes(TimestampService.class);

        assertThat(timestampClient.getFreshTimestamp(), is(not(nullValue())));
    }

    @Test
    public void shouldExposeATimestampManagementServer() {
        TimestampService timestampClient = EteSetup.createClientToAllNodes(TimestampService.class);
        TimestampManagementService timestampManagementClient =
                EteSetup.createClientToAllNodes(TimestampManagementService.class);

        assertThat(timestampClient.getFreshTimestamp(), is(not(nullValue())));

        long newts = timestampClient.getFreshTimestamp() + 10000000;
        timestampManagementClient.fastForwardTimestamp(newts);

        assertThat(timestampClient.getFreshTimestamp(), is(greaterThan(newts)));
    }
}
