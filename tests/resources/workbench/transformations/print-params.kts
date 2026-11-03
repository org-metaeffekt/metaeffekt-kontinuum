import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

val scriptParams: Map<String, String> =
    jacksonObjectMapper().readValue(params["param.script.params"] ?: "{}")

println("Script params: $scriptParams")