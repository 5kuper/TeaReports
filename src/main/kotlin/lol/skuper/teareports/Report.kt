package lol.skuper.teareports

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val reportedPlayer: String,
    val reporterPlayer: String,
    val msg: String,
    val time: Instant
) {
    @Serializable
    data class Answer(val msg: String, val player: String, val time: Instant)

    var id = 0
    var answer: Answer? = null
}
