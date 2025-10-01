# Mirror - Update Index

This processor created or updates the indices of the mirror. It uses the previously downloaded data files of the
external data sources. As an result the specified mirror directory is extended by the index files.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
None. This processor does not operate on workspace level.


### Parameters
| Parameter          | Required | Description                            |
|--------------------|----------|----------------------------------------|
| param.proxy.scheme | no       | The proxy scheme.                      |
| param.proxy.host   | no       | The proxy host.                        |
| param.proxy.port   | no       | The proxy port.                        |
| param.proxy.user   | no       | The proxy user.                        |
| param.proxy.pass   | no       | The proxy pass.                        |


### Environment
| Parameter      | Required | Description                                              |
|----------------|----------|----------------------------------------------------------|
| env.mirror.dir | yes      | The directory in which index will be created or updated. |
