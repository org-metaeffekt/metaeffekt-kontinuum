# Report - Create Annex Archive

This process is for generating the archive .zip containing all Annex relevant content. The .zip contains the PDF document
as well as the aggregated license and component directories of the inventory.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                  | Required | Description                                           |
|----------------------------|----------|-------------------------------------------------------|
| input.document.en.pdf.file | no       | The English input PDF document for the Annex archive. |
| input.document.de.pdf.file | no       | The German input PDF document for the Annex archive.  |
| output.annex.archive.file  | yes      | The output Annex archive file.                        |

### Parameters
| Parameter                      | Required | Description                                                                              |
|--------------------------------|----------|------------------------------------------------------------------------------------------|
| input.inventory.components.dir | no       | The directory containing the components of the inventory for which the Annex is created. |
| input.inventory.licenses.dir   | no       | The directory containing the licenses of the inventory for which the Annex is created.   |
| input.inventory.sources.dir    | no       | The directory containing the sources of the inventory for which the Annex is created.    |

### Environment
None
