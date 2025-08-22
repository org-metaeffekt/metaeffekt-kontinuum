# Report - Create Document

This process creates a document for a selected document type. 
Types to choose from are vulnerability report (VR), cert report (CR), software distribution annex (SDA), license documentation (LD) and initial license documentation (ILD).
The document is generated for a specified set of inventories using an asset descriptor. 
The asset descriptor along with the specified inventories is consumed from user-specified sources.
Updating the asset descriptor and providing the inventories is the responsibility of the user.
The generated document (.pdf) along with aggregated sources (annex .zip) will be saved to the output directory.
The different document types require different parameters, the following table lists them and their usage/description:

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                      | Required        | Description                                                                                         |
|--------------------------------|-----------------|-----------------------------------------------------------------------------------------------------|
| output.dir                     | yes             | The directory where outputs are copied to. This will contain the .pdf file of the document.         | 
| input.asset.descriptor.dir     | yes             | The directory where the asset descriptors are located.                                              |
| input.asset.descriptor.file    | yes             | The file of the asset descriptor used for the document.                                             | 
| input.inventory.file           | yes             | The path of the input inventory for the document.                                                   |
| input.reference.inventory.file | yes             | The path of the reference inventory for the document.                                               |
| input.reference.license.dir    | yes             | The path to the reference license directory.                                                        |
| input.reference.component.dir  | yes             | The path to the reference component directory.                                                      |
| input.security.policy.dir      | no (VR/CR only) | The directory containing the security policy files.                                                 | 

### Parameters
| Parameter                    | Required        | Description                                                                                                                                                                                     |
|------------------------------|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| param.document.type          | yes             | The type of the document, choose from `VR` (vulnerability-report), `CR` (cert-report), `SDA` (software distribution annex), `LD` (license distribution) or `ILD`(initial license distribution). | 
| param.document.language      | yes             | The language of the document, currently `de` (German) and `en` (English) are supported.                                                                                                         | 
| param.asset.id               | yes             | The Id of the asset.                                                                                                                                                                            | 
| param.asset.name             | yes             | The name of the asset.                                                                                                                                                                          | 
| param.asset.version          | yes             | The version of the asset.                                                                                                                                                                       | 
| param.product.name           | yes             | The name of the product.                                                                                                                                                                        | 
| param.product.version        | yes             | The version of the product.                                                                                                                                                                     | 
| param.product.watermark      | yes             | The watermark of the product.                                                                                                                                                                   |
| param.overview.advisors      | no (VR/CR only) | The selection of advisors for which the overview is created, provide as a comma-separated list (e.g. "CERT-FR, ...").                                                                           |

### Environment
| Parameter                    | Required           | Description                                                                                                  |
|------------------------------|--------------------|--------------------------------------------------------------------------------------------------------------|
| env.workbench.base.path      | yes                | The path to the workbench. This is used to form relative paths to the inventories if they are not absolute.  | 
| env.vulnerability.mirror.dir | yes (VR/CR only)   | The input database containing the vulnerability data.                                                        |
