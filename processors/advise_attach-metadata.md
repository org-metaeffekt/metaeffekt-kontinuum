# Advise - Attach Metadata

This process attaches specified metadata to a given input inventory. This process can be triggered before dashboard / 
report creation to ensure that necessary metadata is available.

| Parameter              | Required | Description                                                         |
|------------------------|----------|---------------------------------------------------------------------|
| input.inventory.file   | yes      | The file  of the input inventory to attach metadata to.             |
| output.inventory.file  | yes      | The file  of the output inventory containing the attached metadata. |
| metadata.asset.id      | yes      | The asset ID which will be attached to the inventory.               |
| metadata.asset.name    | no       | The asset name which will be attached to the inventory.             |
| metadata.asset.version | no       | The asset version which will be attached to the inventory.          |
| metadata.asset.path    | no       | The asset path which will be attached to the inventory.             |
| metadata.asset.type    | no       | The asset type which will be attached to the inventory.             |
