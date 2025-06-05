### (Resolved) Inventory Resolved

Used to resolve all artifacts contained in an inventory and gather additional information on those
artifacts.

| Property               | Required | Explanation                                                   |
|------------------------|----------|---------------------------------------------------------------|
| inputInventoryFile     | yes      | The input file path of the inventory which is to be resolved. |
| outputInventoryFile    | yes      | The output file path of the resolved inventory.               |
| downloadBaseDir        | no       | Download directory of the maven repo index.                   |
| artifactResolverConfig | yes      | Yaml file containing config options for the resolver.         |
| artifactResolverProxy  | yes      | Yaml file containing proxy information for the resolver.      |