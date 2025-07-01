package hr.mdodik.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Counters (
    @JsonProperty("_id") val id: String,
    @JsonProperty("seq") val seq: Int,
)