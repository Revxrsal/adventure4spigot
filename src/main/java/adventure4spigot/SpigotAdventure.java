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
package adventure4spigot;

import adventure4spigot.item.AdventureItem;
import adventure4spigot.scoreboard.AdventureScoreboard;
import adventure4spigot.util.GameVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * A collection of utility methods for integrating Adventure into Spigot's
 * different APIs
 */
public final class SpigotAdventure {

    private SpigotAdventure() {}

    /**
     * Creates an empty inventory with the specified type and title. If the type
     * is {@link InventoryType#CHEST}, the new inventory has a size of 27;
     * otherwise the new inventory has the normal size for its type.<br>
     * It should be noted that some inventory types do not support titles and
     * may not render with said titles on the Minecraft client.
     * <br>
     * {@link InventoryType#WORKBENCH} will not process crafting recipes if
     * created with this method. Use
     * {@link Player#openWorkbench(Location, boolean)} instead.
     * <br>
     * {@link InventoryType#ENCHANTING} will not process {@link ItemStack}s
     * for possible enchanting results. Use
     * {@link Player#openEnchanting(Location, boolean)} instead.
     *
     * @param owner The holder of the inventory; can be null if there's no holder.
     * @param type  The type of inventory to create.
     * @param title The title of the inventory, to be displayed when it is viewed.
     * @return The new inventory.
     * @throws IllegalArgumentException if the {@link InventoryType} cannot be
     *                                  viewed.
     */
    public static Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull Component title) {
        return Bukkit.createInventory(owner, type, LegacyComponentSerializer.legacySection().serialize(title));
    }

    /**
     * Creates an empty inventory of type {@link InventoryType#CHEST} with the
     * specified size and title.
     *
     * @param owner the holder of the inventory, or null to indicate no holder
     * @param size  a multiple of 9 as the size of inventory to create
     * @param title the title of the inventory, displayed when inventory is
     *              viewed
     * @return a new inventory
     * @throws IllegalArgumentException if the size is not a multiple of 9
     */
    public static Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull Component title) {
        return Bukkit.createInventory(owner, size, LegacyComponentSerializer.legacySection().serialize(title));
    }

    /**
     * Gets the display name that is set.
     * <p>
     * Plugins should check that hasDisplayName() returns <code>true</code>
     * before calling this method.
     *
     * @param meta item meta to get from
     * @return the display name that is set
     */
    public static Component getDisplayName(@NotNull ItemMeta meta) {
        return ITEM.getDisplayName(meta);
    }

    /**
     * Sets the display name.
     *
     * @param meta        item meta to set for
     * @param displayName the name to set
     */
    public static void setDisplayName(@NotNull ItemMeta meta, @Nullable Component displayName) {
        ITEM.setDisplayName(meta, displayName);
    }

    /**
     * Gets the lore that is set.
     * <p>
     * Plugins should check if hasLore() returns <code>true</code> before
     * calling this method.
     *
     * @param meta item meta to get from
     * @return a list of lore that is set
     */
    public static List<Component> getLore(@NotNull ItemMeta meta) {
        return ITEM.getLore(meta);
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param meta item meta to get from
     * @param lore the lore that will be set
     */
    public static void setLore(@NotNull ItemMeta meta, @Nullable List<Component> lore) {
        ITEM.setLore(meta, lore);
    }

    /**
     * Registers an Objective on this Scoreboard
     *
     * @param name        Name of the Objective
     * @param criteria    Criteria for the Objective
     * @param displayName Name displayed to players for the Objective.
     * @return The registered Objective
     * @throws IllegalArgumentException if name is null
     * @throws IllegalArgumentException if name is longer than 32767
     *                                  characters.
     * @throws IllegalArgumentException if criteria is null
     * @throws IllegalArgumentException if displayName is null
     * @throws IllegalArgumentException if displayName is longer than 128
     *                                  characters.
     * @throws IllegalArgumentException if an objective by that name already
     *                                  exists
     */
    public static Objective registerNewObjective(@NotNull Scoreboard scoreboard, @NotNull String name, @NotNull String criteria, @NotNull Component displayName) {
        return SCOREBOARD.registerNewObjective(scoreboard, name, criteria, displayName);
    }

    /**
     * Sets the name displayed to players for this objective.
     *
     * @param displayName Display name to set
     * @throws IllegalArgumentException if displayName is null
     * @throws IllegalArgumentException if displayName is longer than 128
     *                                  characters.
     */
    public static void setDisplayName(Objective objective, Component displayName) {
        SCOREBOARD.setDisplayName(objective, displayName);
    }

    /**
     * Gets the name displayed to players for this objective
     *
     * @return this objective's display name
     * @throws IllegalStateException if this objective has been unregistered
     */
    public static Component getDisplayName(Objective objective) {
        return SCOREBOARD.getDisplayName(objective);
    }

    /**
     * Checks whether is Adventure natively supported on this platform
     *
     * @return If adventure is natively supported
     */
    public static boolean isNativelySupported() {
        return IS_NATIVELY_SUPPORTED;
    }

    private static final boolean IS_NATIVELY_SUPPORTED = checkIfSupported();
    private static final AdventureItem ITEM = AdventureItem.ITEM.get();
    private static final AdventureScoreboard SCOREBOARD = AdventureScoreboard.SCOREBOARD.get();

    private static boolean checkIfSupported() {
        try {
            Method method = GameVersion.ocbClass("inventory.ItemMeta").getDeclaredMethod("displayName");
            return method.getReturnType().getSimpleName().equals("Component");
        } catch (NoSuchMethodException ignored) {
            return false;
        }
    }
}
