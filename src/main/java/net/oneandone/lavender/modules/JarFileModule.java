/**
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

import net.oneandone.lavender.config.Filter;
import net.oneandone.sushi.fs.CreateInputStreamException;
import net.oneandone.sushi.fs.FileNotFoundException;
import net.oneandone.sushi.fs.Node;
import net.oneandone.sushi.fs.World;
import net.oneandone.sushi.fs.filter.Predicate;
import net.oneandone.sushi.fs.memory.MemoryNode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarFileModule extends Module {
    public static Object[] fromJar(Filter filter, String type, JarModuleConfig config, Node file) throws IOException {
        World world;
        ZipEntry entry;
        String path;
        ZipInputStream src;
        Node root;
        Node child;
        boolean isProperty;
        Node propertyNode;

        world = file.getWorld();
        root = world.getMemoryFilesystem().root().node(UUID.randomUUID().toString(), null).mkdir();
        src = new ZipInputStream(file.createInputStream());
        propertyNode = null;
        while ((entry = src.getNextEntry()) != null) {
            path = entry.getName();
            if (!entry.isDirectory()) {
                isProperty = WarModule.PROPERTIES.equals(path);
                if (config.isPublicResource(path) || isProperty) {
                    System.out.println("mem jar " + path);
                    child = root.join(path);
                    child.getParent().mkdirsOpt();
                    world.getBuffer().copy(src, child);
                    if (isProperty) {
                        propertyNode = child;
                    }
                }
            }
        }
        return new Object[] { new JarFileModule(filter, type, config, root), propertyNode };
    }

    private final JarModuleConfig config;
    private final Node exploded;
    private final List<Node> files;

    public JarFileModule(Filter filter, String type, JarModuleConfig config, Node exploded) throws IOException {
        super(filter, type, config.getModuleName(), true, "");
        this.config = config;
        this.exploded = exploded;
        this.files = exploded.find(exploded.getWorld().filter().includeAll().predicate(Predicate.FILE));
    }

    public Iterator<Resource> iterator() {
        return new JarFileResourceIterator(exploded, config, files);
    }

    // TODO: expensive
    public Resource probeIncluded(String path) {
        for (Resource resource : this) {
            if (path.equals(resource.getPath())) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public void saveCaches() {
        // nothing to do
    }
}
