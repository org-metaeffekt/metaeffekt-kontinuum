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

### Environment

None
