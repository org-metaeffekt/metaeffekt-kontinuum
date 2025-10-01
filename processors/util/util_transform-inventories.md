# Util - Transform inventories

This process performs a transformation on a specified inventory using a kotlin script. These transformations can range from inventory merging to field replacements.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                   | Required | Description                                                                        |
|-----------------------------|----------|------------------------------------------------------------------------------------|
| input.inventory.dir         | yes      | The directory containing the input inventories to perform the transformation with. |
| output.inventory.dir        | yes      | The directory to which the output inventories will be saved after transformation.  |

### Parameters
| Parameter                | Required | Description                                                                        |
|--------------------------|----------|------------------------------------------------------------------------------------|
| param.kotlin.script.file | yes      | The file of the kotlin script to be used for transforming inventories.             |

### Environment
None