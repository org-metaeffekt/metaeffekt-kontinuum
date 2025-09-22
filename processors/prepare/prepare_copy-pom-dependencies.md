# Prepare - Copy Pom Dependencies

This process copies dependencies found in a pom.xml file into a directory for further processing. 

## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter                        | Required | Description                                                                                                   |
|----------------------------------|----------|---------------------------------------------------------------------------------------------------------------|
| output.dependencies.dir          | yes      | The output directory in which to copy the extracted dependencies.                                             |

### Parameters
| Parameter                         | Required | Description                                                                |
|-----------------------------------|----------|----------------------------------------------------------------------------|
| param.group.id                    | yes      | The group id of the pom containing the relevant dependencies.              |
| param.artifact.id                 | yes      | The artifact id of the pom containing the relevant dependencies.           |
| param.version                     | yes      | The version of the pom containing the relevant dependencies.               |
| param.exclude.transitive.enabled  | yes      | Set to false if transitive dependencies should be included in the extract. |

### Environment
None


