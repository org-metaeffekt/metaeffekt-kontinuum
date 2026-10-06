# Util - Convert Assessments

This process converts assessments, based on an older version of the assessment format to the newest assessment format.
## Properties

The different properties are sorted into three different groups which are explained in the top level [README](../../README.md)
of this repository.

### Input / Output
| Parameter             | Required | Description                                                        |
|-----------------------|----------|--------------------------------------------------------------------|
| input.assessment.dir  | yes      | The directory containing assessment files which will be converted. |
| output.assessment.dir | yes      | The directory containing the converted assessment files.           |

### Parameters
| Parameter            | Required | Description                                                                           |
|----------------------|----------|---------------------------------------------------------------------------------------|
| param.output.mode    | yes      | The output mode controlling converter behaviour. Can be MERGE, DIRECTORY or IN_PLACE. |
| param.output.format  | yes      | The format in which the assessments will be saved. Can be JSON, YAML or PRESERVE.     |

For more information about the parameters and behavior listed above, please see: [Assessment Format Upgrade Tool](https://github.com/org-metaeffekt/metaeffekt-documentation/blob/main/metaeffekt-vulnerability-management/other-topics/assessment/vulnerability-assessment-upgrade-plugin.md)

### Environment
None