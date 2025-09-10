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
| Parameter                      | Required        | Description                                             |
|--------------------------------|-----------------|---------------------------------------------------------|
| input.asset.descriptor.dir     | yes             | The directory where the asset descriptors are located.  |
| input.asset.descriptor.path    | yes             | The path of the asset descriptor used for the document. | 
| input.inventory.file           | yes             | The path of the input inventory for the document.       |
| input.reference.inventory.file | yes             | The path of the reference inventory for the document.   |
| input.reference.license.dir    | yes             | The path to the reference license directory.            |
| input.reference.component.dir  | yes             | The path to the reference component directory.          |
| input.security.policy.dir      | no (VR/CR only) | The directory containing the security policy files.     | 
| output.document.file           | yes             | The file where to store the resulting output document.  | 

### Parameters
| Parameter                              | Required        | Description                                                                                                                                                                                     |
|----------------------------------------|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| param.template.dir                     | yes             | The directory containing the document template.                                                                                                                                                 |
| param.document.type                    | yes             | The type of the document, choose from `VR` (vulnerability-report), `CR` (cert-report), `SDA` (software distribution annex), `LD` (license distribution) or `ILD`(initial license distribution). | 
| param.document.language                | yes             | The language of the document, currently `de` (German) and `en` (English) are supported.                                                                                                         | 
| param.asset.id                         | yes             | The Id of the asset.                                                                                                                                                                            | 
| param.asset.name                       | yes             | The name of the asset.                                                                                                                                                                          | 
| param.asset.version                    | yes             | The version of the asset.                                                                                                                                                                       | 
| param.product.name                     | yes             | The name of the product.                                                                                                                                                                        | 
| param.product.version                  | yes             | The version of the product.                                                                                                                                                                     | 
| param.product.watermark                | yes             | The watermark of the product.                                                                                                                                                                   |
| param.overview.advisors                | no (VR/CR only) | The selection of advisors for which the overview is created, provide as a comma-separated list (e.g. "CERT-FR, ...").                                                                           |
| param.property.selector.organization   | yes             | Templates may introduce a set of custom properties. These properties are loaded via a dedicated set of property files qualified by this selector.                                               |
| param.property.selector.classification | no              | Templates may introduce a set of custom properties. These properties are loaded via a dedicated set of property files qualified by this selector.                                               |
| param.property.selector.control        | no              | Templates may introduce a set of custom properties. These properties are loaded via a dedicated set of property files qualified by this selector.                                               |

### Environment
| Parameter                    | Required           | Description                                           |
|------------------------------|--------------------|-------------------------------------------------------|
| env.kontinuum.processors.dir | yes                | The directory of the kontinuum processors.            | 
| env.workbench.processors.dir | yes                | The directory of the workbench processors.            | 
| env.vulnerability.mirror.dir | yes (VR/CR only)   | The input database containing the vulnerability data. |

# FIXMEs

* do we require the asset descriptor separated into two parameters?
* asset.* should be optional; what about product.*; could be also optional falling back on asset
* the input inventory path should be redundant; maybe required as placeholder
* the reference inventory path should be redundant; maybe required as placeholder
