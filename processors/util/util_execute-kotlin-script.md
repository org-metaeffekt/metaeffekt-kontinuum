# Util - Execute Kotlin Script

This process executes a specified kotlin script file. Instead of calling the `kotlin` CLI
directly, the script is executed via the `ae-kotlin-scripting-maven-plugin` (akin to
`util_transform-inventories`). The script is evaluated as an `InventoryFilterScript` and
receives its arguments as a named parameter map, accessed in the script via the implicit
`params` receiver.

## Properties

The different properties are sorted into three different groups.

### Input / Output
| Parameter                | Required | Description                                                   |
|--------------------------|----------|---------------------------------------------------------------|
| input.kotlin.script.file | yes      | The kotlin script file to be executed.                        |
| input.inventory.file     | yes      | The input inventory file to be processed by the script.       |
| output.inventory.file    | yes      | The target inventory file written by the script.              |

### Parameters
| Parameter      | Required | Description                                  |
|----------------|----------|----------------------------------------------|
| param.asset.id | no       | The identifier of the asset being processed. |
| param.script.params | no      | Additional script parameters as a JSON object. Defaults to an empty object. |

### Environment

None

## Reading Script Parameters

All parameters listed above are available in the script via the implicit `params` receiver. Additional script
parameters passed with `param.script.params` arrive as a single JSON string and are parsed by the script itself:

```kotlin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

val scriptParams: Map<String, String> =
    jacksonObjectMapper().readValue(params["param.script.params"] ?: "{}")

val filter = scriptParams["filter"]
```

Example invocation:

```bash
mvn -f processors/util/util_execute-kotlin-script.xml process-resources \
  -Dinput.kotlin.script.file=scripts/my-script.kts \
  -Dinput.inventory.file=inventory.xlsx \
  -Doutput.inventory.file=output.xlsx \
  -Dparam.script.params='{"filter":"os"}'
```