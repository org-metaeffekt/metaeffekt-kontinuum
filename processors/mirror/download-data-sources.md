# Mirror - Download Data Sources

This process downloads the vulnerability mirror from different data sources.

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
None. This processor does not operate on workspace level.


### Parameters
| Parameter                                        | Required | Description                                                                                                                      |
|--------------------------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------|
| param.proxy.scheme                               | no       | The proxy scheme.                                                                                                                |
| param.proxy.host                                 | no       | The proxy host.                                                                                                                  |
| param.proxy.port                                 | no       | The proxy port.                                                                                                                  |
| param.proxy.user                                 | no       | The proxy user.                                                                                                                  |
| param.proxy.pass                                 | no       | The proxy pass.                                                                                                                  |
| param.fail.on.error                              | no       | Fails the process in case an error is detected during download. Defaults to `true`.                                              |
| param.fail.on.issue                              | no       | Fails the process in case of an integrity issue or incomplete state with the download. Defaults to `true`.                       |
| param.activate.vulnerabilities.custom.download   | no       | Clones the git repositories with the custom vulnerability data. Defaults to `false`.                                             |
| param.vulnerabilities.custom.download.git.url    | no       | The remote URL of the repository to clone, optional with credentials. Further repositories are declared in the processor itself. |
| param.vulnerabilities.custom.download.git.branch | no       | The branch or tag to check out. The default branch of the remote is used when left empty.                                        |


### Environment
| Parameter      | Required | Description                                            |
|----------------|----------|--------------------------------------------------------|
| env.mirror.dir | yes      | The directory to which the mirror data is downloaded.  |
| env.nvd.apikey | yes      | An API key to access the NVD database.                 |


