# Convert - CycloneDX to Inventory

Used to convert a CycloneDX document into an inventory.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Property                    | Required | Explanation                                                                                   |
|-----------------------------|----------|-----------------------------------------------------------------------------------------------|
| input.bom.file              | yes      | The CycloneDX bom which to convert to an inventory.                                           |
| output.inventory.file       | yes      | The file path of the output inventory.                                                        |


### Parameters
| Property                                  | Required | Explanation                                                                                   |
|-------------------------------------------|----------|-----------------------------------------------------------------------------------------------|
| param.include.metadata.component.enabled  | no       | If set to true, includes components contained in the CycloneDX metadata field.                |
| param.derive.attributes.from.purl.enabled | no       | If set to true, derives attributes not present as CycloneDX fields from the purl if possible. |
| param.include.assets.enabled              | no       | If set to true, includes CycloneDX components recognized as assets.                           |
| param.include.licenses.enabled            | no       | If set to true, licenses listed in the CycloneDX document will be included in the inventory.  |

### Environment
None