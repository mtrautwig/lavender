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

import java.net.URI;

public class UsernamePassword {
    public static final UsernamePassword ANONYMOUS = new UsernamePassword("", "");

    public final String username;
    public final String password;

    public UsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public URI add(URI uri) {
        if (this == ANONYMOUS) {
            return uri;
        }

        if (uri.getPort() != -1) {
            throw new IllegalStateException("TODO: " + uri);
        }
        if (uri.getQuery() != null) {
            throw new IllegalStateException("TODO: " + uri);
        }
        return URI.create(uri.getScheme() + "://" + username + ":" + password + "@" + uri.getHost() + "/" + uri.getPath());
    }
}
