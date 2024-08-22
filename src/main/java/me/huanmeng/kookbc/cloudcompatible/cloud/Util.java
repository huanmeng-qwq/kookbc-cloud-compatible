/*
 *     KookBC -- The Kook Bot Client & JKook API standard implementation for Java.
 *     Copyright (C) 2022 - 2023 KookBC contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.huanmeng.kookbc.cloudcompatible.cloud;

import snw.kookbc.impl.KBCClient;
import snw.kookbc.impl.command.cloud.CloudCommandInfo;
import snw.kookbc.impl.command.cloud.CloudCommandManagerImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Util {

    private Util() {
    }

    public static List<String> listCloudCommandsHelp(KBCClient client, boolean force) {
        List<CloudCommandInfo> commandsInfo = ((CloudCommandManagerImpl) client.getCore().getCommandManager()).getCommandsInfo();

        List<String> result = new LinkedList<>();
        for (CloudCommandInfo command : commandsInfo) {
            if (!force && (command.owningPlugin() == null || !command.owningPlugin().isEnabled() || command.hidden())) {
                continue;
            }
            insertCommandHelpContent(result, command.syntax(), Arrays.asList(command.prefixes()), command.description());
        }
        return result;
    }

    private static void insertCommandHelpContent(List<String> result, String rootName, Collection<String> prefixes, String description) {
        result.add(
                limit(
                        String.format("(%s)%s: %s",
                                String.join(" ",
                                        prefixes),
                                rootName,
                                (isBlank(description)) ? "此命令没有简介。" : description
                        ),
                        4997
                )
        );
    }

    public static CloudCommandInfo findSpecificCloudCommand(KBCClient client, String name) {
        List<CloudCommandInfo> commandsInfo = ((CloudCommandManagerImpl) client.getCore().getCommandManager()).getCommandsInfo();
        if (!isBlank(name) && !name.equalsIgnoreCase("all")) {
            return commandsInfo.stream()
                    .filter(info ->
                            info.rootName().equalsIgnoreCase(name) ||
                                    info.syntax().equalsIgnoreCase(name) ||
                                    Arrays.stream(info.aliases())
                                            .anyMatch(
                                                    alias -> alias.equalsIgnoreCase(name)
                                            )
                    ).findFirst().orElse(null);
        } else {
            return null;
        }
    }

    public static String limit(String original, int maxLength) {
        if (maxLength < 0 || original.length() <= maxLength)
            return original;
        return String.format("%s...", original.substring(0, maxLength));
    }


    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
