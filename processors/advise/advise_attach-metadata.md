# Advise - Attach Metadata

This process attaches specified metadata to a given input inventory. This process can be triggered before dashboard / 
report creation to ensure that necessary metadata is available.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md) 
of this repository.

### Input / Output
| Parameter              | Required | Description                                                                           |
|------------------------|----------|---------------------------------------------------------------------------------------|
| input.inventory.file   | yes      | The inventory file to which the metadata will be attached.                            |
| output.inventory.file  | yes      | The inventory with the new metadata attached. Can be the same as the input inventory. |

### Parameters
| Parameter                      | Required | Description                                                                           |
|--------------------------------|----------|---------------------------------------------------------------------------------------|
| param.metadata.asset.id        | yes      | The asset ID which will be attached to the inventory.                                 |
| param.metadata.asset.name      | no       | The asset name which will be attached to the inventory.                               |
| param.metadata.asset.version   | no       | The asset version which will be attached to the inventory.                            |
| param.metadata.asset.path      | no       | The asset path which will be attached to the inventory.                               |
| param.metadata.asset.type      | no       | The asset type which will be attached to the inventory.                               |

### Environment
None
