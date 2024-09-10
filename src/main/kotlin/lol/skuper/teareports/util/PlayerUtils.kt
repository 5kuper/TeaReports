package lol.skuper.teareports.util

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
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

@Serializable
data class SoundInfo(val key: String, val pitch: Float = 1f)

fun Player.notify(msg: Component, sound: SoundInfo?) {
    if (isOnline) {
        sendMessage(msg)
        sound?.run {
            playSound(Sound.sound(Sound.Type { Key.key(key) }, Sound.Source.MASTER, 1f, pitch))
        }
    }
}