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
package net.oneandone.lavender.filter.processor;

import java.util.Arrays;
import java.util.function.Predicate;

import static net.oneandone.lavender.filter.processor.LavenderHtmlAttribute.*;
import static net.oneandone.lavender.filter.processor.LavenderHtmlTag.*;

public enum LavenderUrlRewriteMatcher implements UrlRewriteMatcher {

    IMG_MATCHER(SRC, p -> p.getTag() == IMG, true),
    IMG_SOURCESET_MATCHER(SRCSET, p -> p.getTag() == IMG, false),
    LINK_MATCHER(HREF, p -> p.getTag() == LINK && Arrays.asList("stylesheet", "icon", "shortcut icon", "preload").contains(p.getAttribute(REL)), false),
    SCRIPT_MATCHER(SRC, p -> p.getTag() == SCRIPT && (("text/javascript".equals(p.getAttribute(TYPE))) || !p.containsAttribute(TYPE)), false),
    INPUT_MATCHER(SRC, p -> p.getTag() == INPUT && "image".equals(p.getAttribute(TYPE)), false),
    A_MATCHER(HREF, p -> p.getTag() == A, false),
    SOURCE_MATCHER(SRC, p -> p.getTag() == SOURCE, false),
    SOURCE_SOURCESET_MATCHER(SRCSET, p -> p.getTag() == SOURCE, false),
    FORM_MATCHER(ACTION, p -> p.getTag() == FORM, false),
    IFRAME_MATCHER(SRC, p -> p.getTag() == IFRAME, false),
    OBJECT_MATCHER(DATA, p -> p.getTag() == OBJECT, false),
    DATA_LAVENDER_MATCHER(DATA_LAVENDER_ATTR, p -> true, false);


    private final Predicate<HtmlElement> predicate;
    private final HtmlAttribute attributeToRewrite;
    private final boolean ignoreData;

    LavenderUrlRewriteMatcher(HtmlAttribute attributeToRewrite, Predicate<HtmlElement> rewritePredicate, boolean ignoreData) {
        this.attributeToRewrite = attributeToRewrite;
        this.predicate = rewritePredicate;
        this.ignoreData = ignoreData;
    }

    public boolean ignoreValue(String value) {
        return ignoreData && (value != null) && value.startsWith("data:");
    }

    @Override
    public boolean matches(HtmlElement htmlElement) {
        return predicate.test(htmlElement);
    }

    @Override
    public HtmlAttribute getAttributeToRewrite() {
        return attributeToRewrite;
    }

}
