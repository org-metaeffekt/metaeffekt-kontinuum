# Mirror - Download Data Sources

This process downloads the vulnerability mirror from different data sources.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
None. This processor does not operate on workspace level.


### Parameters
| Parameter           | Required | Description                                                                                                |
|---------------------|----------|------------------------------------------------------------------------------------------------------------|
| param.proxy.scheme  | no       | The proxy scheme.                                                                                          |
| param.proxy.host    | no       | The proxy host.                                                                                            |
| param.proxy.port    | no       | The proxy port.                                                                                            |
| param.proxy.user    | no       | The proxy user.                                                                                            |
| param.proxy.pass    | no       | The proxy pass.                                                                                            |
| param.fail.on.error | no       | Fails the process in case a error is detected during download. Defaults to `true`.                         |
| param.fail.on.issue | no       | Fails the process in case of an integrity issue or incomplete state with the download. Defaults to `true`. |


### Environment
| Parameter      | Required | Description                                        |
|----------------|----------|----------------------------------------------------|
| env.mirror.dir | yes      | The directory to which the mirror data downloaded. |
| env.nvd.apikey | yes      | An API key to access the NVD database.             |


