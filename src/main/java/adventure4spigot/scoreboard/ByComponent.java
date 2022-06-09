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
import adventure4spigot.reflect.MethodCaller.BoundMethodCaller;
import adventure4spigot.reflect.ReflectField;
import adventure4spigot.util.GameVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import static adventure4spigot.util.GameVersion.*;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.colorDownsamplingGson;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

// versions 1.13.2-1.17.1: we must manually call ScoreboardObjective#setDisplayName()
// versions 1.18.1: it's ScoreboardObjective#a()
final class ByComponent implements AdventureScoreboard {

    private static final GsonComponentSerializer SERIALIZER = supportsHexColors() ? gson() : colorDownsamplingGson();

    private static final Class<?> COMPONENT = nmsClass(
            "IChatBaseComponent",
            "network.chat.IChatBaseComponent"
    );

    private static final Class<?> CHAT_SERIALIZER = nmsClass(
            "IChatBaseComponent$ChatSerializer",
            "network.chat.IChatBaseComponent$ChatSerializer"
    );

    private static final Class<?> SCOREBOARD_OBJECTIVE = nmsClass(
            "ScoreboardObjective",
            "world.scores.ScoreboardObjective"
    );

    private static final BoundMethodCaller C_TO_S;
    private static final BoundMethodCaller S_TO_C;
    private static final MethodCaller setDisplayName, getDisplayName;
    private static final ReflectField<Object> objectiveField;

    static {
        try {
            C_TO_S = MethodCaller.wrap(CHAT_SERIALIZER.getDeclaredMethod("a", COMPONENT)).bindTo(null);
            S_TO_C = MethodCaller.wrap(CHAT_SERIALIZER.getDeclaredMethod("a", String.class)).bindTo(null);
            if (GameVersion.supports(18)) {
                getDisplayName = MethodCaller.wrap(SCOREBOARD_OBJECTIVE.getDeclaredMethod("d"));
                setDisplayName = MethodCaller.wrap(SCOREBOARD_OBJECTIVE.getDeclaredMethod("a", COMPONENT));
            } else {
                getDisplayName = MethodCaller.wrap(SCOREBOARD_OBJECTIVE.getDeclaredMethod("getDisplayName"));
                setDisplayName = MethodCaller.wrap(SCOREBOARD_OBJECTIVE.getDeclaredMethod("setDisplayName", COMPONENT));
            }
            objectiveField = ReflectField.getter(ocbClass("scoreboard.CraftObjective"), "objectiveField");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public Objective registerNewObjective(@NotNull Scoreboard scoreboard,
                                                    @NotNull String name,
                                                    @NotNull String criteria,
                                                    @NotNull Component displayName) {
        Objective o = scoreboard.registerNewObjective(name, criteria, ".");
        setDisplayName(o, displayName);
        return o;
    }

    @Override public void setDisplayName(Objective objective, Component displayName) {
        Object nmsObjective = objectiveField.get(objective);
        String asJson = SERIALIZER.serialize(displayName);
        Object chatComponent = S_TO_C.call(asJson);
        setDisplayName.call(nmsObjective, chatComponent);
    }

    @Override public Component getDisplayName(Objective objective) {
        Object nmsObjective = objectiveField.get(objective);
        Object chatComponent = getDisplayName.call(nmsObjective);
        String json = (String) C_TO_S.call(chatComponent);
        return SERIALIZER.deserialize(json);
    }
}
