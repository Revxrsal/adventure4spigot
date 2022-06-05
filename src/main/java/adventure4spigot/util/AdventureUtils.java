/*
 * This file is part of adventure4spigot, licensed under the MIT License.
 *
 *  Copyright (c) Revxrsal <reflxction.github@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package adventure4spigot.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for converting Strings to Components and vice-versa.
 */
@SuppressWarnings("rawtypes")
public final class AdventureUtils {

    private AdventureUtils() {}

    public static List<Component> toAdventure(ComponentSerializer serializer, List<String> strings) {
        final List<Component> adventures = new ArrayList<>(strings.size());
        for (final String json : strings) {
            adventures.add(serializer.deserialize(json));
        }
        return adventures;
    }

    public static List<String> fromAdventure(ComponentSerializer<Component, ?, String> serializer, List<Component> adventures) {
        final List<String> jsons = new ArrayList<>(adventures.size());
        for (final Component component : adventures) {
            jsons.add(serializer.serialize(component));
        }
        return jsons;
    }
}
