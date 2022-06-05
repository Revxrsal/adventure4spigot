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
package adventure4spigot.item;

import adventure4spigot.reflect.MethodCaller;
import adventure4spigot.reflect.MethodCaller.BoundMethodCaller;
import adventure4spigot.reflect.ReflectField;
import adventure4spigot.util.GameVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static adventure4spigot.reflect.ReflectField.getterAndSetter;
import static adventure4spigot.util.AdventureUtils.fromAdventure;
import static adventure4spigot.util.AdventureUtils.toAdventure;
import static adventure4spigot.util.GameVersion.nmsClass;
import static adventure4spigot.util.GameVersion.ocbClass;

/**
 * Versions 1.14 - 1.16.4 store display name and lore as IChatBaseComponent.
 * Therefore, we must convert our strings to components
 */
final class ByComponent implements AdventureItem {

    private static final GsonComponentSerializer SERIALIZER = GameVersion.supportsHexColors() ?
            GsonComponentSerializer.gson() : GsonComponentSerializer.colorDownsamplingGson();

    private static final Class<?> ITEM_META = ocbClass("inventory.CraftMetaItem");
    private static final Class<?> COMPONENT = nmsClass("IChatBaseComponent");
    private static final Class<?> CHAT_SERIALIZER = nmsClass("IChatBaseComponent$ChatSerializer");

    private static final BoundMethodCaller C_TO_S;
    private static final BoundMethodCaller S_TO_C;

    static {
        try {
            C_TO_S = MethodCaller.wrap(CHAT_SERIALIZER.getDeclaredMethod("a", COMPONENT)).bindTo(null);
            S_TO_C = MethodCaller.wrap(CHAT_SERIALIZER.getDeclaredMethod("a", String.class)).bindTo(null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ReflectField<Object> DISPLAY_NAME = getterAndSetter(ITEM_META, "displayName");
    private static final ReflectField<List<Object>> LORE = getterAndSetter(ITEM_META, "lore");

    @Override public Component getDisplayName(ItemMeta meta) {
        Object/*IChatBaseComponent*/ displayName = DISPLAY_NAME.get(meta);
        if (displayName == null)
            return null;
        String displayNameJson = (String) C_TO_S.call(displayName);
        return SERIALIZER.deserialize(displayNameJson);
    }

    @Override public void setDisplayName(ItemMeta meta, Component displayName) {
        if (displayName == null) {
            DISPLAY_NAME.set(meta, null);
            return;
        }
        String displayNameJson = SERIALIZER.serialize(displayName);
        Object component/*IChatBaseComponent*/ = S_TO_C.call(displayNameJson);
        DISPLAY_NAME.set(meta, component);
    }

    @Override public List<Component> getLore(ItemMeta meta) {
        List<Object> lore = LORE.get(meta);
        if (lore == null)
            return null;
        List<Component> list = new ArrayList<>();
        for (Object line : lore) {
            String lineJson = (String) C_TO_S.call(line);
            list.add(SERIALIZER.deserialize(lineJson));
        }
        return list;
    }

    @Override public void setLore(ItemMeta meta, List<Component> lore) {
        if (lore == null) {
            LORE.set(meta, null);
            return;
        }
        List<Object> componentsLore = new ArrayList<>();
        for (Component line : lore) {
            String json = SERIALIZER.serialize(line);
            Object component = S_TO_C.call(json);
            componentsLore.add(component);
        }
        LORE.set(meta, componentsLore);
    }
}
