# Convert - Inventory to CycloneDx

Used to convert an inventory into a cyclonedx bom in either xml or json format.

| Property                   | Required | Explanation                                                                  |
|----------------------------|----------|------------------------------------------------------------------------------|
| input.inventory.file       | yes      | The input inventory file path from which to generate the bom.                |
| output.bom.file            | yes      | The output bom file path with the correct format extension.                  |
| document.name              | yes      | The document name listed in the bom.                                         |
| organization               | yes      | The organization which created the bom.                                      |
| organization.url           | yes      | The url of the organization which created the bom.                           |
| description                | no       | The document description listed in the bom.                                  |
| person                     | no       | The person which created the bom.                                            |
| comment                    | no       | A comment regarding the creation of the bom.                                 |
| output.format              | no       | Which output format the bom should be in. (Default JSON)                     |
| document.version           | no       | The current version of this bom.                                             |
| map.relationships          | no       | If relationships between inventory artifacts should be tracked.              |
| use.license.expressions    | no       | If license expressions or single licenses should be used.                    |
| include.license.texts      | no       | If license texts should be included.                                         |
| includeAssets              | no       | If only artifacts should be included or assets as well.                      |
| includeTechnicalProperties | no       | Only required to mitigate data-loss for multiple import/export cycles.       |
| deriveAttributesFromPurl   | no       | If missing attributes should be derived from the PURL if present.            |
| customLicenseMappings      | no       | A custom license mapping file containing license identifier : license pairs. |