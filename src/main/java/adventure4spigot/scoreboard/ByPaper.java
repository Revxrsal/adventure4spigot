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
package adventure4spigot.scoreboard;

import adventure4spigot.reflect.MethodCaller;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

final class ByPaper implements AdventureScoreboard {

    private static final boolean IS_SUPPORTED;
    private static MethodCaller regNewObj;
    private static MethodCaller getDisplayName, setDisplayName;

    static {
        boolean isSupported = true;
        try {
            regNewObj = MethodCaller.wrap(Scoreboard.class.getDeclaredMethod("registerNewObjective", String.class, String.class, Component.class));
            getDisplayName = MethodCaller.wrap(Objective.class.getDeclaredMethod("displayName"));
            setDisplayName = MethodCaller.wrap(Objective.class.getDeclaredMethod("displayName", Component.class));
        } catch (NoSuchMethodException e) {
            isSupported = false;
        }
        IS_SUPPORTED = isSupported;
    }

    @Override public Objective registerNewObjective(@NotNull Scoreboard scoreboard, @NotNull String name, @NotNull String criteria, @NotNull Component displayName) {
        return (Objective) regNewObj.call(scoreboard, name, criteria, displayName);
    }

    @Override public void setDisplayName(Objective objective, Component displayName) {
        setDisplayName.call(objective, displayName);
    }

    @Override public Component getDisplayName(Objective objective) {
        return (Component) getDisplayName.call(objective);
    }

    public static boolean isSupported() {
        return IS_SUPPORTED;
    }
}
