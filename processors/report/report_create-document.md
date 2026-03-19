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
| Parameter            | Required | Description                                              | default                                       |
|----------------------|----------|----------------------------------------------------------|-----------------------------------------------|
| input.inventory.dir  | yes      | The directory of the input inventories for the document. |                                               |
| output.document.file | yes      | The file where to store the resulting output document.   |                                               | 


### Parameters
| Parameter                              | Required             | Description                                                                                                                                                                                                                    | default                                                           |
|----------------------------------------|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------|
| param.computed.inventory.dir           | no                   | The directory where to store the computed (filtered) inventory.                                                                                                                                                                | <param.template.dir>/target/tmp                                   | 
| param.template.dir                     | no                   | The directory containing the document template.                                                                                                                                                                                | <env.workbench.dir/templates/report-template                      |
| param.document.type                    | yes                  | The type of the document, choose from `VR` (vulnerability-report), `CR` (cert-report), `SDA` (software distribution annex), `CAD` (custom annex document), `LD` (license distribution) or `ILD`(initial license distribution). |                                                                   | 
| param.document.language                | no                   | The language of the document, currently `de` (German) and `en` (English) are supported.                                                                                                                                        | en                                                                | 
| param.asset.id                         | yes                  | The Id of the asset.                                                                                                                                                                                                           |                                                                   | 
| param.asset.name                       | yes                  | The name of the asset.                                                                                                                                                                                                         |                                                                   | 
| param.asset.version                    | yes                  | The version of the asset.                                                                                                                                                                                                      |                                                                   | 
| param.product.name                     | yes                  | The name of the product.                                                                                                                                                                                                       |                                                                   | 
| param.product.version                  | yes                  | The version of the product.                                                                                                                                                                                                    |                                                                   | 
| param.product.watermark                | yes                  | The watermark of the product.                                                                                                                                                                                                  |                                                                   |
| param.overview.advisors                | yes (VR/CR only)     | The selection of advisors for which the overview is created, provide as a comma-separated list (e.g. "CERT-FR, ...").                                                                                                          |                                                                   |
| param.property.selector.organization   | yes                  | Templates may introduce a set of custom properties. These properties are loaded via a dedicated set of property files qualified by this selector.                                                                              |                                                                   |
| param.property.selector.classification | no                   | Templates may introduce a set of custom properties. These properties are loaded via a dedicated set of property files qualified by this selector.                                                                              |                                                                   |
| param.property.selector.control        | no                   | Templates may introduce a set of custom properties. These properties are loaded via a dedicated set of property files qualified by this selector.                                                                              |                                                                   |
| param.security.policy.file             | yes (VR/VSR/CR only) | The file containing the security policy configurations for the workbench.                                                                                                                                                      | <env.workbench.dir>/policies/security-policy/security-policy.json |
| param.asset.descriptor.file            | yes                  | The file of the asset descriptor used for the document.                                                                                                                                                                        |                                                                   | 
| param.reference.inventory.dir          | yes (VR only)        | The directory of the reference inventory for the document.                                                                                                                                                                     |                                                                   |
| param.reference.license.dir            | no                   | The path to the reference license directory.                                                                                                                                                                                   | <param.reference.inventory.dir>/../licenses                       |
| param.reference.component.dir          | no                   | The path to the reference component directory.                                                                                                                                                                                 | <param.reference.inventory.dir>/../components                     |

### Environment
| Parameter                    | Required             | Description                                           | default                        |
|------------------------------|----------------------|-------------------------------------------------------|--------------------------------|
| env.kontinuum.dir            | yes                  | The directory of the kontinuum.                       |                                | 
| env.kontinuum.processors.dir | no                   | The directory of the kontinuum processors.            | <env.kontinuum.dir>/processors | 
| env.workbench.dir            | yes                  | The directory of the workbench.                       |                                | 
| env.vulnerability.mirror.dir | yes (VR/VSR/CR only) | The input database containing the vulnerability data. |                                |

### Notes

Even though a security policy file can be set for document creation, the security policy activeIds are to be set within
the asset descriptor because different document parts can refer to different configurations. In order to have control over
the security policy configurations for each step of document generation, these activeIds can be individually set for
each document part and each transformation as well.

# FIXMEs

* asset.* should be optional; what about product.*; could be also optional falling back on asset
* the input inventory path should be redundant; maybe required as placeholder
* the reference inventory path should be redundant; maybe required as placeholder
* in case the security.policy.file is only used by descriptors; we do not require to pass it in here. The
  file path can then be expressed relative to env.workbench.base.dir; this would enable more flexibility --> the 
  security policy file is required at different steps of creating VR/CR this means that the descriptors as well as the 
  template poms require it