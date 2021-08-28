# Auto Spigot Reload

A minecraft plugin that allows you to automatically reload the server when a specific file is updated

Made for spigot 1.8.8 and later\
Requires Java 8

This plugin was made to simplify the plugin development process, and **should not be used on non-testing server**

## Commands

> Note : all commands are only available to OP players

### `asr list`
Shows a list of the watched files

### `asr add <file path>`
Adds a file to the watch list\
The file can't be directory
> Example :
> ```
> asr add ./plugins/awesomePlugin.jar
> ```

### `asr remove <file path>`
Removes a file from the watch list
> Example :
> ```
> asr remove ./plugins/awesomePlugin.jar
> ```

### `asr clear`
Clears the watched file list

### `asr on`
Enables auto reload

### `asr off`
Disables auto reload
