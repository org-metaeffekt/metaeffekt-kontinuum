# Extract - Scan Directory

This process scans a directory containing a number of base artifacts and extracts an inventory from it. The artifacts contained
in the input directory are usually a result of the "fetch" process which fetches artifacts from the local file system, maven repositories, remove URLs
or docker repositories.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                     | Required | Description                                                                                                   |
|-------------------------------|----------|---------------------------------------------------------------------------------------------------------------|
| input.extract.dir             | yes      | The directory containing extracted information to scan. This can be container extracts, pom dependencies etc. |
| output.scan.dir               | yes      | The output directory for the scanned files.                                                                   |
| output.inventory.file         | yes      | The output inventory containing the scanned information.                                                      |

### Parameters
| Parameter                       | Required | Description                                                                            |
|---------------------------------|----------|----------------------------------------------------------------------------------------|
| param.reference.inventory.dir   | yes      | The directory of the reference inventory with which the input inventory is enriched.   |

### Environment
None


