# Report - Create Document

This process creates a document for a selected document type. 
Types to choose from are vulnerability report (VR), cert report (CR), software distribution annex (SDA), license documentation (LD) and initial license documentation (ILD).
The document is generated for a specified set of inventories using an asset descriptor. 
The asset descriptor along with the specified inventories is consumed from user-specified sources.
Updating the asset descriptor and providing the inventories is the responsibility of the user.
The generated document (.pdf) along with aggregated sources (annex .zip) will be saved to the output directory.
The different document types require different parameters, the following table lists them and their usage/description:

## Parameters applying to all document types

| Parameter                | Required | Description                                                                                                                                                                                     |
|--------------------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| document.type            | yes      | The type of the document, choose from `VR` (vulnerability-report), `CR` (cert-report), `SDA` (software distribution annex), `LD` (license distribution) or `ILD`(initial license distribution). | 
| document.language        | yes      | The language of the document, currently `de` (German) and `en` (English) are supported.                                                                                                         | 
| asset.id                 | yes      | The Id of the asset.                                                                                                                                                                            | 
| asset.name               | yes      | The name of the asset.                                                                                                                                                                          | 
| asset.version            | yes      | The version of the asset.                                                                                                                                                                       | 
| product.name             | yes      | The name of the product.                                                                                                                                                                        | 
| product.version          | yes      | The version of the product.                                                                                                                                                                     | 
| product.watermark        | yes      | The watermark of the product.                                                                                                                                                                   |
| output.dir               | yes      | The directory where outputs are copied to. This will contain the .pdf file of the document.                                                                                                     | 
| asset.descriptor.basedir | yes      | The directory where the asset descriptors are located.                                                                                                                                          | 
| asset.descriptor.file    | yes      | The file of the asset descriptor used for the document.                                                                                                                                         | 
| inventory.path           | yes      | The path of the input inventory for the document.                                                                                                                                               |
| reference.inventory.path | yes      | The path of the reference inventory for the document.                                                                                                                                           |
| reference.license.path   | yes      | The path to the reference license directory.                                                                                                                                                    |
| reference.component.path | yes      | The path to the reference component directory.                                                                                                                                                  |
| workbench.base.path      | yes      | The path to the workbench. This is used to form relative paths to the inventories if they are not absolute.                                                                                     | 

## Parameters specific to vulnerability report and cert report

| Parameter                | Required | Description                                                                                                           |
|--------------------------|----------|-----------------------------------------------------------------------------------------------------------------------|
| input.database           | yes      | The input database containing the vulnerability data.                                                                 | 
| overview.advisors        | yes      | The selection of advisors for which the overview is created, provide as a comma-separated list (e.g. "CERT-FR, ..."). | 
| policy.basedir           | yes      | The directory containing the security policy files.                                                                   | 
