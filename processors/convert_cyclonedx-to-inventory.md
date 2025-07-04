# (Convert) CycloneDx to Inventory

Used to convert a cyclonedx document into an inventory.

| Property                 | Required | Explanation                                                       |
|--------------------------|----------|-------------------------------------------------------------------|
| input.bom                | yes      | The input inventory file path from which to generate the bom.     |
| output.inventory         | yes      | The output bom file path with the correct format extension.       |
| includeMetadataComponent | no       | If the cyclonedx component under metadata should be included.     |
| deriveAttributesFromPurl | no       | If missing attributes should be derived from the PURL if present. |
| includeAssets            | no       | If assets should be included in the conversion or omitted.        |
| includeLicenses          | no       | If licenses should be included in the conversion or omitted.      |