package lol.skuper.teareports.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class PlayerCommandExecutor : CommandExecutor {
    override fun onCommand(
        sender: CommandSender, cmd: Command, label: String, args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("The command can only be used by a player.", NamedTextColor.DARK_RED))
            return true
        }

        return onPlayerCommand(sender, cmd, label, args)
    }

    abstract fun onPlayerCommand(
        sender: Player, cmd: Command, label: String, args: Array<out String>
    ): Boolean
}