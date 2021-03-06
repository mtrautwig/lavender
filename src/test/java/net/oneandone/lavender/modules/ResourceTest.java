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
package net.oneandone.lavender.modules;

import net.oneandone.lavender.index.Hex;
import net.oneandone.lavender.index.Util;
import net.oneandone.sushi.fs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourceTest {
    private static final World WORLD = World.createMinimal();
    private Resource resource;

    @BeforeEach
    public void setup() throws IOException {
        resource = NodeResource.forBytes(WORLD,  "modules/x/img/close.gif", (byte) 0x00, (byte) 0x01, (byte) 0x7F, (byte) 0x80, (byte) 0x81, (byte) 0xFF);
    }

    @Test
    public void testData() throws IOException {
        ByteArrayOutputStream buffer;
        byte[] data;

        buffer = new ByteArrayOutputStream();
        resource.writeTo(buffer);
        data = buffer.toByteArray();
        assertNotNull(data);
        assertEquals(6, data.length);
        assertEquals(0x00, data[0]);
        assertEquals(0x01, data[1]);
        assertEquals(0x7F, data[2]);
        assertEquals((byte) 0x80, data[3]);
        assertEquals((byte) 0x81, data[4]);
        assertEquals((byte) 0xFF, data[5]);
    }

    @Test
    public void testMd5() throws IOException {
        ByteArrayOutputStream buffer;
        byte[] data;
        byte[] md5;

        buffer = new ByteArrayOutputStream();
        resource.writeTo(buffer);
        data = buffer.toByteArray();
        md5 = Util.md5(data);
        assertNotNull(md5);
        assertEquals(16, md5.length);
        assertEquals("852e7d76cdb8af7395cd039c0ecc293a", Hex.encodeString(md5));
    }

    @Test
    public void testToString() {
        String string = resource.toString();
        assertNotNull(string);
    }

}
