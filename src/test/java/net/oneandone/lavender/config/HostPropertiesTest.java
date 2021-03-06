/*
 * Copyright 1&1 Internet AG, https://github.com/1and1/
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
package net.oneandone.lavender.config;

import net.oneandone.sushi.fs.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Host properties, secrets, and network. */
public class HostPropertiesTest {
    @Test
    public void load() throws IOException, URISyntaxException {
        World world;
        HostProperties properties;
        Network network;
        Cluster cluster;

        world = World.create(false);
        properties = HostProperties.load(world.guessProjectHome(Network.class).join("src/test/config/host.properties"), false);
        network = Network.load(world.node(properties.network));
        cluster = network.lookup("local");
        assertNotNull(cluster);
        assertEquals("local", cluster.getName());
        assertNotNull(cluster.docroot("web"));
    }
}
