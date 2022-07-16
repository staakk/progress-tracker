package io.github.staakk.progresstracker.data.training

import io.github.staakk.progresstracker.data.Id
import java.time.LocalDateTime

data class Training(
    val id: Id = Id.Empty,
    val date: LocalDateTime,
    val rounds: List<Round> = emptyList()
)