# Util - Enrich inventory with Reference

This process enriches a specified input inventory with a reference inventory to curate it with further information for later enrichment.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                     | Required | Description                                                                          |
|-------------------------------|----------|--------------------------------------------------------------------------------------|
| input.inventory.file          | yes      | The file path of the inventory which will be enriched with the reference inventory.  |

### Parameters
| Parameter                       | Required | Description                                                                            |
|---------------------------------|----------|----------------------------------------------------------------------------------------|
| param.reference.inventory.dir   | yes      | The directory of the reference inventory with which the input inventory is enriched.   |


### Environment
None