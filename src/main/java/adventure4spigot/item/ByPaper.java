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
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;

import static adventure4spigot.util.GameVersion.ocbClass;

/**
 * Accesses Paper's native support for Adventure.
 * <p>
 * Unfortunately, we can't access methods without using reflection, as we can't
 * build against Paper. This may be improved in the future
 */
final class ByPaper implements AdventureItem {

    private static final boolean IS_SUPPORTED;

    private static MethodCaller getDisplayName, setDisplayName;
    private static MethodCaller getLore, setLore;

    static {
        boolean isSupported = false;
        try {
            Method method = ItemMeta.class.getDeclaredMethod("displayName");
            if (method.getReturnType().getSimpleName().equals("Component"))
                isSupported = true;
        } catch (NoSuchMethodException ignored) {
        }
        IS_SUPPORTED = isSupported;
    }

    public static boolean isSupported() {
        return IS_SUPPORTED;
    }

    @Override public Component getDisplayName(ItemMeta meta) {
        return (Component) getDisplayName.call(meta);
    }

    @Override public void setDisplayName(ItemMeta meta, Component displayName) {
        setDisplayName.call(meta, displayName);
    }

    @Override public List<Component> getLore(ItemMeta meta) {
        return (List<Component>) getLore.call(meta);
    }

    @Override public void setLore(ItemMeta meta, List<Component> lore) {
        setLore.call(meta, lore);
    }

    static {
        try {
            getDisplayName = MethodCaller.wrap(ItemMeta.class.getDeclaredMethod("displayName"));
            setDisplayName = MethodCaller.wrap(ItemMeta.class.getDeclaredMethod("displayName", Component.class));
            getLore = MethodCaller.wrap(ItemMeta.class.getDeclaredMethod("lore"));
            setLore = MethodCaller.wrap(ItemMeta.class.getDeclaredMethod("lore", List.class));
        } catch (Throwable ignored) {}
    }

}
