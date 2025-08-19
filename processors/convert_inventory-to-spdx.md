# Convert inventory to spdx

This process converts an inventory, independent of which stage it was produced in, into a spdx document. All available
parameters are listed in the table below. Parameters marked as "not required" already have default values associated with
them if necessary. For example "documentFormat" is no required to be manually set, as it already has the default value "JSON".


| Parameter                    | Required | Description                                                                               |
|------------------------------|----------|-------------------------------------------------------------------------------------------|
| input.inventory.file         | yes      | The input inventory file                                                                  |
| output.bom.file              | yes      | The output inventory file                                                                 |
| document.name                | yes      | The name of the SPDX-Document                                                             |
| document.description         | yes      | A description contained in the SPDX-Document                                              |
| document.id.prefix           | no       | A custom prefix for every SPDX-element ID                                                 |
| document.organization        | no       | The organization which produced the SPDX-Document                                         |
| document.organization.url    | no       | The URL of the organization which produced the SPDX-Document                              |
| document.person              | no       | The Person which produced the SPDX-Document                                               |
| document.comment             | no       | Any additional information about the SPDX-Document                                        |
| document.output.format       | no       | Output format of the SPDX-Document (JSON or XML)                                          |
| document.version             | no       | The version of this exact SPDX-Document                                                   |
| map.relationships            | no       | Whether relationships between artifacts should be tracked                                 |
| use.license.expressions      | no       | Whether license expressions instead of single licenses should be used                     |
| include.license.texts        | no       | Whether license texts should be contained in the document                                 |
| include.assets               | no       | Whether assets should be listed in the document or omitted                                |
| include.technical.properties | no       | Whether additional properties required for lossless cyclic conversion should be included. |
| derive.attributes.from.purl  | no       | Whether missing attributes should be derived from PURL if possible                        |



