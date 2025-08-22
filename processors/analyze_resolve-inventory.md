### (Resolved) Inventory Resolved

Used to resolve all artifacts contained in an inventory and gather additional information on those
artifacts.

| Property                      | Required | Explanation                                                   |
|-------------------------------|----------|---------------------------------------------------------------|
| input.inventory.file          | yes      | The input file path of the inventory which is to be resolved. |
| output.inventory.file         | yes      | The output file path of the resolved inventory.               |
| artifact.resolver.config.file | yes      | Yaml file containing config options for the resolver.         |
| artifact.resolver.proxy.file  | yes      | Yaml file containing proxy information for the resolver.      |
| download.base.dir             | no       | Download directory of the maven repo index.                   |
