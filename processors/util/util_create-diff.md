# Util - Create Diff

This process creates two output files containing the differences between two provided inventory versions. Which 
inventory version is declared as "base" and which as "compare" is negligible since the comparison is done in both 
ways and saved separately. The parameters "product.version" and "product.version.compare" are only used for naming the 
two output files.

| Parameter                    | Required | Description                                                       |
|------------------------------|----------|-------------------------------------------------------------------|
| input.inventory.file         | yes      | The file of the base inventory for comparison.                    |
| input.inventory.compare.file | yes      | The file of the inventory to be compared with the base inventory. |
| inventory.version            | yes      | The version of the base inventory.                                |
| inventory.compare.version    | yes      | The version of the compare inventory.                             |
| output.inventory.dir         | yes      | The output directory for saving the results of the diff.          |
