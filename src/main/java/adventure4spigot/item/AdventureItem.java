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

import adventure4spigot.util.GameVersion;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Allows using components in Minecraft across all versions, not necessarily
 * on Paper.
 */
public interface AdventureItem {

    Component getDisplayName(ItemMeta meta);

    void setDisplayName(ItemMeta meta, Component displayName);

    List<Component> getLore(ItemMeta meta);

    void setLore(ItemMeta meta, List<Component> lore);

    @SuppressWarnings("Guava")
    Supplier<AdventureItem> ITEM = Suppliers.memoize(() -> {
        if (ByPaper.isSupported())
            return new ByPaper();
        else if (GameVersion.EXACT >= 16.5)
            return new ByComponentString();
        else if (GameVersion.PROTOCOL >= 14 && GameVersion.PROTOCOL <= 16)
            return new ByComponent();
        else
            return new ByRawString();
    });

}
