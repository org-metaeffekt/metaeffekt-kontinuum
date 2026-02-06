# Report - Create Annex Archive

This process is for generating the archive .zip containing all Annex relevant content. The .zip contains the PDF Document
As well as the aggregated license and component directories of the inventory.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Property                   | Required | Explanation                                   |
|----------------------------|----------|-----------------------------------------------|
| input.document.pdf.file    | yes      | The input PDF document for the Annex archive. |
| output.annex.archive.file  | yes      | The output Annex archive file.                |

### Parameters
| Property                       | Required | Explanation                                                                              |
|--------------------------------|----------|------------------------------------------------------------------------------------------|
| input.inventory.components.dir | yes      | The directory containing the components of the inventory for which the Annex is created. |
| input.inventory.licenses.dir   | yes      | The directory containing the licenses of the inventory for which the Annex is created.   |

### Environment
None
