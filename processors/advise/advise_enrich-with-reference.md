# Advise - Enrich with Reference

This process enriches a specified input inventory with a reference directory to curate it with further information for later enrichment.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                     | Required | Description                                                                          |
|-------------------------------|----------|--------------------------------------------------------------------------------------|
| input.inventory.file          | yes      | The file path of the inventory which will be enriched with the reference inventory.  |
| input.reference.inventory.dir | yes      | The directory of the reference inventory with which the input inventory is enriched. |
| output.inventory.dir          | yes      | The target directory for the enriched output inventory.                              |     
| output.inventory.path         | yes      | The path of the output inventory relative to 'output.inventory.dir' .                |     

### Parameters
None

### Environment
None