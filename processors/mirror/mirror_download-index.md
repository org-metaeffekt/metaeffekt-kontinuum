# Mirror - Download Index

This process downloads the vulnerability mirror index to a specified target directory to be used for later enrichment of inventories.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
None.

### Parameters
| Parameter                 | Required | Description                                         |
|---------------------------|----------|-----------------------------------------------------|
| param.mirror.archive.name | yes      | The name of the vulnerability mirror archive name.  |
| param.mirror.archive.url  | yes      | The source url of the vulnerability mirror archive. |

### Environment
| Parameter                     | Required | Description                                               |
|-------------------------------|----------|-----------------------------------------------------------|
| env.vulnerability.mirror.dir  | yes      | The target directory of the vulnerability mirror archive. |