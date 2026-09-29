# Extract - Generate Linux Arch Inventory

This process analysis extracted data from a Linux appliance and generates an inventory with artifacts.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output

| Parameter             | Required | Description                                     |
|-----------------------|----------|-------------------------------------------------|
| input.extract.dir     | yes      | The directory containing extracted information. |
| output.inventory.file | no       | The output inventory file.                      |

### Parameters

| Parameter                   | Required | Description                                                                      |
|-----------------------------|----------|----------------------------------------------------------------------------------|
| input.exclude.patterns.file | no       | The yaml config containing patterns for files to be excluded from the inventory. |

### Environment

None


