# Extract - Generate Windows Arch Inventory

This process analysis extracted data from a Windows appliance and generates an inventory with artifacts.
Optionally, a kotlin script can be applied to the generated inventory. The scripted inventory is written to a
separate output directory, the original inventory remains unchanged.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output

| Parameter                        | Required | Description                                                                                                                      |
|----------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------|
| input.archive.file               | no       | The archive with extraction results.                                                                                             |
| output.inventory.file            | yes      | The output inventory file.                                                                                                       |
| output.scripted.inventory.dir | no       | The directory to which the inventory scripted by the kotlin script is written. Required when param.kotlin.script.file is set. |

### Parameters

| Parameter                      | Required | Description                                                                                                                            |
|--------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------|
| param.analysis.dir             | yes      | The directory where the extracted results are analyzed.                                                                                |
| param.activate.file.processing | no       | Whether files should be processed and added to the inventory or not. By default true.                                                  |
| param.exclude.pattern.file     | no       | The yaml config containing patterns for files to be excluded from the inventory. Required when param.activate.file.processing is true. |
| param.kotlin.script.file       | no       | The kotlin script applied to the extracted inventory. If not set, no script is performed on the inventory.                                      |

### Environment

None
