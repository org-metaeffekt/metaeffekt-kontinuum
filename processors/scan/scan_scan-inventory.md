# Scanned - Inventory Scanner

Scans a resolved inventory for licenses and copyrights and writes the resulting information to an inventory.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Property                           | Required | Explanation                                                                                                                    |
|------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------|
| input.inventory.file               | yes      | The resolved input inventory file path.                                                                                        |
| output.inventory.file              | yes      | The scanned output inventory file path.                                                                                        |
| input.output.analysis.base.dir     | yes      | The directory in which the scanner caches information on artifacts. Increases speed drastically between multiple scanner runs. |

### Parameters
| Parameter                         | Required | Description                                                                          | default                                                           |
|-----------------------------------|----------|--------------------------------------------------------------------------------------|-------------------------------------------------------------------|
| param.properties.file             | yes      | Yaml file containing properties to configure the scanners behaviour.                 |
