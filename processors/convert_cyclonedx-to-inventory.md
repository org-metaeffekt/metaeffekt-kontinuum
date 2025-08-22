# (Convert) CycloneDX to Inventory

Used to convert a CycloneDX document into an inventory.

| Property                    | Required | Explanation                                                                                   |
|-----------------------------|----------|-----------------------------------------------------------------------------------------------|
| input.bom.file              | yes      | The CycloneDX bom which to convert to an inventory.                                           |
| output.inventory.file       | yes      | The file path of the output inventory.                                                        |
| include.metadata.component  | no       | If set to true, includes components contained in the CycloneDX metadata field.                |
| derive.attributes.from.purl | no       | If set to true, derives attributes not present as CycloneDX fields from the purl if possible. |
| include.assets              | no       | If set to true, includes CycloneDX components recognized as assets.                           |
| include.licenses            | no       | If set to true, licenses listed in the CycloneDX document will be included in the inventory.  |