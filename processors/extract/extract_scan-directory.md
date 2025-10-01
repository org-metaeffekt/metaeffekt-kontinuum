# Extract - Scan Directory

This process converts scans a directory containing extracted / prepared artifacts into an inventory. The artifacts contained
in the input directory are usually a result of the "prepare" process which extracts information from container images, dependencies
listed in poms and so on.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                     | Required | Description                                                                                                   |
|-------------------------------|----------|---------------------------------------------------------------------------------------------------------------|
| input.reference.inventory.dir | yes      | The reference inventory which to include.                                                                     |
| input.extract.dir             | yes      | The directory containing extracted information to scan. This can be container extracts, pom dependencies etc. |
| output.scan.dir               | yes      | The output directory for the scanned files.                                                                   |
| output.inventory.file         | yes      | The output inventory containing the scanned information.                                                      |

### Parameters
None

### Environment
None


