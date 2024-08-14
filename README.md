- This code contain little bit info about how to conver AndroidView to compose but nothing much it is just testing ground for offline map and their libraries.

- Also because it is test project, it does not contain any structure. But for some reason you decide to copy some of AndroidView in compose and their proxy 
implementation it is best to fill update block with map view releated params in comp function. Somehow if you decided to copy what I do it is best to offload
local map file creation form composable (create util class and provide it to constructor of viewmodel then add these layers using mapProxy)
