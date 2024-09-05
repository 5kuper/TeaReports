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
    var id: Int = 0
}
