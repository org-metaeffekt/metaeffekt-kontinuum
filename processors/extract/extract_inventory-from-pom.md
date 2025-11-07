# Extract - Inventory from POM

This process creates an inventory from the dependencies listed in the pom.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter              | Required | Description                                              |
|------------------------|----------|----------------------------------------------------------|
| input.pom.file         | yes      | The POM file used as input.                              |
| output.inventory.file  | yes      | The output file for the inventory being created.         |

### Parameters
| Parameter                    | Required | Description                                                               |
|------------------------------|----------|---------------------------------------------------------------------------|
| param.include.scope.provided | no       | Boolean controlling if the scope is set to 'provided' (default is false). |
| param.include.scope.system   | no       | Boolean controlling if the scope is set to 'system' (default is false).   |
| param.include.scope.test     | no       | Boolean controlling if the scope is set to 'test' (default is false).     |
| param.include.optional       | no       | Boolean controlling if the scope is set to 'optional' (default is false). |
| param.include.plugins        | no       | Boolean controlling if plugins are included (default is false).           |

### Environment
None


