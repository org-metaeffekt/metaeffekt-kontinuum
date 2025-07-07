### (Scanned) Inventory Scanner

Scans a resolved inventory for licenses and copyrights and writes the resulting information to an inventory.

| Property         | Required | Explanation                                                                                                                    |
|------------------|----------|--------------------------------------------------------------------------------------------------------------------------------|
| input.inventory.file  | yes      | The resolved input inventory file path.                                                                                        |
| output.inventory | yes      | The scanned output inventory file path.                                                                                        |
| analysisBaseDir  | yes      | The directory in which the scanner caches information on artifacts. Increases speed drastically between multiple scanner runs. |
| propertiesFile   | yes      | Yaml file containing properties to configure the scanners behaviour.                                                           |
