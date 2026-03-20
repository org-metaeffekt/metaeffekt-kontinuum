# Util - Copy Inventories

Copies a list of inventories to a directory. This is a utility processor used to copy different inventories from individual 
locations to a common directory.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Property               | Required | Explanation                                                          |
|------------------------|----------|----------------------------------------------------------------------|
| input.base.dir         | no       | The directory in which the inventories are located.                  |
| output.inventories.dir | yes      | The output directory to which the files are copied.                  |

### Parameters
| Property               | Required | Explanation                                                          |
|------------------------|----------|----------------------------------------------------------------------|
| param.inventories.list | yes      | The list containing the inventories to copy to the output directory. |


### Environment
None
