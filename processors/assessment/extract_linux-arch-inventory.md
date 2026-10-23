# Extract - Generate Linux Arch Inventory

This process analysis extracted data from a Linux appliance and generates an inventory with artifacts.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output

| Parameter             | Required | Description                                           |
|-----------------------|----------|-------------------------------------------------------|
| input.archive.file    | no       | The archive with extraction results.                  |
| output.inventory.file | yes      | The output inventory file.                            |

### Parameters

| Parameter                      | Required | Description                                                                                                                            |
|--------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------|
| param.analysis.dir             | yes      | The directory where the extracted results are analyzed.                                                                                |
| param.activate.file.processing | no       | Whether files should be processed and added to the inventory or not. By default true.                                                  |
| param.exclude.pattern.file     | no       | The yaml config containing patterns for files to be excluded from the inventory. Required when param.activate.file.processing is true. |

### Environment

None
