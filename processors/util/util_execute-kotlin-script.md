# Util - Execute Kotlin Script

This process executes a specified kotlin script file. Instead of calling the `kotlin` CLI
directly, the script is executed via the `ae-kotlin-scripting-maven-plugin` (akin to
`util_transform-inventories`). The script is evaluated as an `InventoryFilterScript` and
receives its arguments as a named parameter map, accessed in the script via the implicit
`params` receiver.

## Properties

The different properties are sorted into three different groups.

### Input / Output
| Parameter                | Required | Description                                                                              |
|--------------------------|----------|------------------------------------------------------------------------------------------|
| input.kotlin.script.file | yes      | The kotlin script file to be executed.                                                   |
| input.properties.file    | no       | The properties file to be read by the script. Used by the fetch and read.properties scripts. |
| input.workspace.dir      | no       | The workspace directory used by the fetch script to place downloaded assets.            |
| output.env.file          | no       | The target env file written by the read.properties script.                                |

### Parameters
| Parameter              | Required | Description                                                    |
|------------------------|----------|----------------------------------------------------------------|
| param.curl.arguments   | no       | Additional arguments passed to curl by the fetch script.       |

### Environment

None
