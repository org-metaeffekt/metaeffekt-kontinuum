# Util - Transform Inventories

This process performs a transformation on a specified inventory using a Kotlin Script. These transformations can range from inventory merging to field replacements.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                   | Required | Description                                                                        |
|-----------------------------|----------|------------------------------------------------------------------------------------|
| input.inventory.dir         | yes      | The directory containing the input inventories to perform the transformation with. |
| output.inventory.dir        | yes      | The directory to which the output inventories will be saved after transformation.  |

### Parameters
| Parameter                | Required | Description                                                                                               |
|--------------------------|----------|-----------------------------------------------------------------------------------------------------------|
| param.kotlin.script.file | yes      | The file of the Kotlin Script to be used for transforming inventories.                                    |
| param.filter.preset      | no       | A preset listed in the called kotlin script which can be used to configure how the inventory is filtered. |
| param.asset.name         | no       | The inventory asset name, only relevant when working with a metaeffekt-workbench or workspace.            |

### Environment
None
