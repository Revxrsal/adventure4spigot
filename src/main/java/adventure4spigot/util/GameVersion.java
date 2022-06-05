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

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

/**
 * A utility for accessing Minecraft versions
 */
public final class GameVersion {

    private GameVersion() {}

    /**
     * The server version, e.g "v1_11_R1"
     */
    public static final String VERSION = getVersion(Bukkit.getServer());

    /**
     * The server's protocol. For example, if 1.11.2 it will be {@code 11}
     */
    public static final int PROTOCOL = Integer.parseInt(VERSION.split("_")[1]);

    /**
     * The exact protocol version. For example, if 1.12.2, it will be {@code 12.2}
     */
    public static final double EXACT = Double.parseDouble(Bukkit.getBukkitVersion().split("-", 2)[0].split(".", 2)[1].substring(1));

    /**
     * Returns the server protocol version, e.g. v1_12_R1
     *
     * @param server Server instance
     * @return The server version
     */
    private static @NotNull String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    /**
     * Returns the NMS class from the specified name
     *
     * @param name Name of the class
     * @return The class instance
     */
    public static @NotNull Class<?> nmsClass(@NotNull String name) {
        try {
            if (PROTOCOL >= 17) {
                return Class.forName("net.minecraft.server." + name);
            }
            return Class.forName("net.minecraft.server." + VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the CraftBukkit class from the specified name
     *
     * @param name Name of the class
     * @return The class instance
     */
    public static @NotNull Class<?> ocbClass(@NotNull String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns whether the current protocol is newer than the specified one
     *
     * @param protocol Protocol to check for
     * @return ^
     */
    public static boolean supports(int protocol) {
        return PROTOCOL >= protocol;
    }

    public static boolean supportsHexColors() {
        return supports(16);
    }
}
