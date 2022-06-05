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

import adventure4spigot.reflect.ReflectField;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static adventure4spigot.reflect.ReflectField.getterAndSetter;
import static adventure4spigot.util.AdventureUtils.*;
import static adventure4spigot.util.GameVersion.ocbClass;

/**
 * Versions 1.16.5+ store display name and lore as JSON strings,
 * therefore we have to serialize but luckily no need to convert to a
 * medium nms.IChatBaseComponent
 */
final class ByComponentString implements AdventureItem {

    private static final GsonComponentSerializer SERIALIZER = GsonComponentSerializer.gson();

    private static final Class<?> ITEM_META = ocbClass("inventory.CraftMetaItem");
    private static final ReflectField<String> DISPLAY_NAME = getterAndSetter(ITEM_META, "displayName");
    private static final ReflectField<List<String>> LORE = getterAndSetter(ITEM_META, "lore");

    @Override public Component getDisplayName(ItemMeta meta) {
        String displayName = DISPLAY_NAME.get(meta);
        return displayName == null ? null : SERIALIZER.deserialize(displayName);
    }

    @Override public void setDisplayName(ItemMeta meta, Component displayName) {
        DISPLAY_NAME.set(meta, displayName == null ? null : SERIALIZER.serialize(displayName));
    }

    @Override public List<Component> getLore(ItemMeta meta) {
        List<String> lore = LORE.get(meta);
        return lore != null ? toAdventure(SERIALIZER, lore) : null;
    }

    @Override public void setLore(ItemMeta meta, List<Component> lore) {
        LORE.set(meta, lore != null ? fromAdventure(SERIALIZER, lore) : null);
    }
}
