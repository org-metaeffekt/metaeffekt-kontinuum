# Util - Transform inventories

This process performs a transformation on a specified inventory using a Kotlin script. 
These transformations can range from inventory merging to field replacements.

| Parameter            | Required | Description                                                                        |
|----------------------|----------|------------------------------------------------------------------------------------|
| input.inventory.dir  | yes      | The directory containing the input inventories to perform the transformation with. |
| output.inventory.dir | yes      | The directory to which the output inventories will be saved after transformation.  |
| kotlin.script.file   | yes      | The file of the Kotlin script to be used for transforming inventories.             |
