# Util - Merge Inventories

This process merges multiple input inventories into one output inventory. The input inventories are specified using a 
directory. Additionally, a regex can be provided to further specify the inventories used for merging within the given 
directory. This process can be triggered at any point in the pipeline and is not bound to a phase.

| Parameter           | Required | Description                                                       |
|---------------------|----------|-------------------------------------------------------------------|
| input.inventory.dir | yes      | The directory containing the inventories to be merged.            |
| output.inventory    | yes      | The file path of the output inventory containing the merged data. |


## Further Parameters

| Parameter          | Required | Description                                                                          |
|--------------------|----------|--------------------------------------------------------------------------------------|
| inventory includes | no       | Regex used to specify which file names are considered for merging, default is *.xls. |
