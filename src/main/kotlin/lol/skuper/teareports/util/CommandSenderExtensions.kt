package lol.skuper.teareports.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

suspend fun CommandSender.playerOnly(commandHandler: suspend () -> Boolean): Boolean {
    if (this is Player) {
        return commandHandler()
    } else {
        sendMessage(Component.text("The command can only be used by a player.", NamedTextColor.DARK_RED))
        return true
    }
}