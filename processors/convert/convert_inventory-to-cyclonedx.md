# (Convert) Inventory to CycloneDx

Used to convert an inventory into a CycloneDX bom in either xml or json format.

| Parameter                        | Required | Description                                                                                                             |
|----------------------------------|----------|-------------------------------------------------------------------------------------------------------------------------|
| input.inventory.file             | yes      | The input inventory file used to generate the CycloneDX BOM.                                                            |
| output.bom.file                  | yes      | The output file where the generated CycloneDX BOM will be written.                                                      |
| document.name                    | yes      | The name of the generated document (e.g., project or product name).                                                     |
| document.organization            | yes      | The name of the organization responsible for the document.                                                              |
| document.organization.url        | yes      | The URL of the organization responsible for the document.                                                               |
| document.output.format           | no       | The output format of the document (e.g. XML, JSON).                                                                     |
| document.version                 | no       | The version (iteration) of the generated document.                                                                      |
| document.description             | no       | A description of the document.                                                                                          |
| document.person                  | no       | The name of the person responsible for the document.                                                                    |
| document.comment                 | no       | A free-text comment to include in the document metadata.                                                                |
| custom.license.mappings          | no       | Path to custom license mappings. These map found licenses to specific or custom licenses during the conversion process. |
| map.relationships                | no       | Whether to map relationships between components in the BOM.                                                             |
| use.license.expressions          | no       | Whether to use SPDX license expressions instead of plain license names.                                                 |
| include.license.texts            | no       | Whether to include full license texts in the BOM.                                                                       |
| include.assets                   | no       | Whether to include assets in the BOM.                                                                                   |
| include.technical.properties     | no       | Whether to include technical properties in the BOM, required to enable near-lossless roundtrip conversion.              |
| derive.attributes.from.purl      | no       | Whether to derive missing attributes from the purl where possible.                                                      |