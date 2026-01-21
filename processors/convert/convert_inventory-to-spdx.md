# Convert inventory to spdx

This process converts an inventory, independent of which stage it was produced in, into an SPDX document. All available
parameters are listed in the table below. Parameters marked as "not required" already have default values associated.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                                 | Required | Description                                                                                                             |
|-------------------------------------------|----------|-------------------------------------------------------------------------------------------------------------------------|
| input.inventory.file                      | yes      | The input inventory file used to generate the SPDX BOM.                                                                 |
| output.bom.file                           | yes      | The output file where the generated SPDX BOM will be written.                                                           | |

### Parameters
| Parameter                                 | Required | Description                                                                                                             |
|-------------------------------------------|----------|-------------------------------------------------------------------------------------------------------------------------|
| param.document.name                       | yes      | The name of the generated document (e.g., project or product name).                                                     |
| param.document.organization               | yes      | The name of the organization responsible for the document.                                                              |
| param.document.organization.url           | yes      | The URL of the organization responsible for the document.                                                               |
| param.document.output.format              | no       | The output format of the document (e.g. XML, JSON).                                                                     |
| param.document.version                    | no       | The version (iteration) of the generated document.                                                                      |
| param.document.description                | no       | A description of the document.                                                                                          |
| param.document.person                     | no       | The name of the person responsible for the document.                                                                    |
| param.document.comment                    | no       | A free-text comment to include in the document metadata.                                                                |
| param.document.id.prefix                  | no       | A prefix for every SPDX element ID.                                                                                     |
| param.custom.license.mappings.enabled     | no       | Path to custom license mappings. These map found licenses to specific or custom licenses during the conversion process. |
| param.map.relationships.enabled           | no       | Whether to map relationships between components in the BOM.                                                             |
| param.license.expressions.enabled         | no       | Whether to use SPDX license expressions instead of plain license names.                                                 |
| param.include.license.texts.enabled       | no       | Whether to include full license texts in the BOM.                                                                       |
| param.include.assets.enabled              | no       | Whether to include assets in the BOM.                                                                                   |
| param.technical.properties.enabled        | no       | Whether to include technical properties in the BOM, required to enable near-lossless roundtrip conversion.              |
| param.derive.attributes.from.purl.enabled | no       | Whether to derive missing attributes from the purl where possible.                                                      |
| param.derive.attributes.from.purl.enabled | no       | Whether to derive missing attributes from the purl where possible.                                                      |

### Environment
None


