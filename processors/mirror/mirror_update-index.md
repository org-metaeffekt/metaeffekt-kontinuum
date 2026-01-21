# Mirror - Update Index

This processor creates or updates the indices of the mirror. It uses the previously downloaded data files of the
external data sources. As a result the specified mirror directory is extended by the index files.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
None. This processor does not operate on workspace level.


### Parameters
| Parameter           | Required | Description                                                                                             |
|---------------------|----------|---------------------------------------------------------------------------------------------------------|
| param.proxy.scheme  | no       | The proxy scheme.                                                                                       |
| param.proxy.host    | no       | The proxy host.                                                                                         |
| param.proxy.port    | no       | The proxy port.                                                                                         |
| param.proxy.user    | no       | The proxy user.                                                                                         |
| param.proxy.pass    | no       | The proxy pass.                                                                                         |
| param.fail.on.error | no       | Fails the process in case a error is detected during indexing. Defaults to `true`.                      |
| param.fail.on.issue | no       | Fails the process in case of an integrity issue or incomplete state with the index. Defaults to `true`. |


### Environment
| Parameter      | Required | Description                                              |
|----------------|----------|----------------------------------------------------------|
| env.mirror.dir | yes      | The directory in which index will be created or updated. |
