# Convert - Inventory to CycloneDx

Used to convert an inventory into a CycloneDx BOM in either XML or JSON format.

| Property                   | Required | Explanation                                                                  |
|----------------------------|----------|------------------------------------------------------------------------------|
| input.inventory.file       | yes      | The input inventory file path from which to generate the BOM.                |
| output.bom.file            | yes      | The output BOM file path with the correct format extension.                  |
| document.name              | yes      | The document name listed in the BOM.                                         |
| organization               | yes      | The organization which created the BOM.                                      |
| organization.url           | yes      | The url of the organization which created the BOM.                           |
| description                | no       | The document description listed in the BOM.                                  |
| person                     | no       | The person which created the BOM.                                            |
| comment                    | no       | A comment regarding the creation of the BOM.                                 |
| output.format              | no       | Which output format the BOM should be in. (Default JSON)                     |
| document.version           | no       | The current version of this BOM.                                             |
| map.relationships          | no       | If relationships between inventory artifacts should be tracked.              |
| use.license.expressions    | no       | If license expressions or single licenses should be used.                    |
| include.license.texts      | no       | If license texts should be included.                                         |
| includeAssets              | no       | If only artifacts should be included or assets as well.                      |
| includeTechnicalProperties | no       | Only required to mitigate data-loss for multiple import/export cycles.       |
| deriveAttributesFromPurl   | no       | If missing attributes should be derived from the PURL if present.            |
| customLicenseMappings      | no       | A custom license mapping file containing license identifier : license pairs. |