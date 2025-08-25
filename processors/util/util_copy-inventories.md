# Util - Copy Inventories

Copies a list of inventories to a directory. This is a utility processor used to copy different inventories from specific
locations to a shared directory which can be parameterized to the template for creating a report from an inventory directory.
Use this processor when your directory structure contains multiple versions of the same inventory within one directory and
you want to combine them with other directories in a report.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Property               | Required | Explanation                                                          |
|------------------------|----------|----------------------------------------------------------------------|
| input.inventories.list | yes      | The list containing the inventories to copy to the output directory. |
| input.base.dir         | no       | The directory in which the inventories are located.                  |
| output.inventories.dir | yes      | The output directory to which the files are copied.                  |

### Parameters
None

### Environment
None