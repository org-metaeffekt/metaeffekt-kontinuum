### Resolved - Inventory Resolved

Used to resolve all artifacts contained in an inventory and gather additional information on those
artifacts.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Property                            | Required | Explanation                                                   |
|-------------------------------------|----------|---------------------------------------------------------------|
| input.inventory.file                | yes      | The input file path of the inventory which is to be resolved. |
| output.inventory.file               | yes      | The output file path of the resolved inventory.               |
| input.artifact.resolver.config.file | yes      | Yaml file containing config options for the resolver.         |
| input.artifact.resolver.proxy.file  | yes      | Yaml file containing proxy information for the resolver.      |

### Parameters
None

### Environment
| Property                            | Required | Explanation                                                   |
|-------------------------------------|----------|---------------------------------------------------------------|
| env.maven.index.dir                 | no       | Download directory of the maven repo index.                   |   
