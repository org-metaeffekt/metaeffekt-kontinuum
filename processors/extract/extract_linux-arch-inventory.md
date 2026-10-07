# Extract - Generate Linux Arch Inventory

This process analysis extracted data from a Linux appliance and generates an inventory with artifacts.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output

| Parameter             | Required | Description                                     |
|-----------------------|----------|-------------------------------------------------|
| input.extract.dir     | yes      | The directory containing extracted information. |
| output.inventory.file | yes      | The output inventory file.                      |

### Parameters

| Parameter                             | Required | Description                                                                             |
|---------------------------------------|----------|-----------------------------------------------------------------------------------------|
| param.file.exclude.patterns           | yes      | The yaml config containing patterns for files to be excluded from the inventory.        |
| param.file.level.processing.activated | no       | Whether files should be processed and added to the inventory or not. By default active. |

### Environment

None


