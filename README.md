# ColoredGlow Lib
![Modrinth](https://img.shields.io/modrinth/dt/coloredglowlib?color=green&label=Modrinth%20downloads&style=flat-square)

This library mod enables you to make entities glow different colors other than plain white or Minecraft vanilla's Team colors. You can also add custom color animation, a set of colors that will change overtime, like a rainbow!

For normal users, you can use the commands to set custom colros, and datapacks to add custom animation, while developers can use the API and the javadoc as guide. There is also a wiki.

TODO change this
![coloredglowlib-with-player](https://user-images.githubusercontent.com/29462910/157507551-dfc4ee7e-66fb-4dae-9578-e17ca64e3b44.png)

#### Commands
Every command begins with `/cgl` , short for **C**olored**G**low**L**ib. You can also use `/coloredglowlib`. 

You can specify a color to use for an entity/entitytype using the following command:

`/cgl setglowcolor <entity/type/default> <color>`

The `entity/type/defaylt` parameter is what you want to target, so @p, Emafire003, the uuid of the entity in front of you etc or it's type such as `minecraft:sheep`. `default` means that all entites that don't have a specific color will glow the default color.

The `color` parameter is a hexadecimal color code (like #ff85ab, #750711, #abc, #123, #a7e ecc, search "color picker" online to get them) without the `#` since minecraft interprets it as a beginning of a tag (which is not what it should be doing in this case). In alternative you can provide the word `rainbow` that will make the entity glow, you guessed it, rainbow. You can also use `random` to make an entity glow a different random color every half a second, or another string that represents a custom animation added via datapack!

You can interact with the config directly in game. For example if you want to override the default team colors (the ones minecraft assigns) with the command:

`/cgl config set overrideTeamColors <true/false>`

You can also get the current value of the setting, like so: 

`/cgl config get overrideTeamColors`

More of this in the wiki!

[![bisecthosting](https://www.bisecthosting.com/images/CF/ColoredGlowLib/BH_NU_PROMO.png)](https://www.bisecthosting.com/LightDev)

## Setup
### For normal users:
Just drag and drop this mod into your mod folder and enjoy! (Make sure you are downloading the correct file for the correct loader tho!)

### For developers:
Include this library into your `build.gradle` as a dependency
```gradle
repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
        content {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    modImplementation "maven.modrinth:coloredglowlib:<version>"
}
```

You can find the correct version in the [versions page](https://modrinth.com/mod/coloredglowlib/versions) on [Modrinth](https://modrinth.com/mod/coloredglowlib). 
If you want the forge version, add a +forge to the number version


#### You can find more information on the [wiki](https://github.com/Emafire003/ColoredGlowLib/wiki)! 

You can find examples of this lib being used in my [FoxGlow](https://github.com/Emafire003/FoxGlow) and [Glowful World](https://github.com/Emafire003/GlowfulWorld) mod.

![luma-shaders-coloredglowlib](https://user-images.githubusercontent.com/29462910/157507676-576d3fb5-e24b-41f7-a7f0-6956d7ae4e29.png)

## License

This mod is available under the GNU LGPL3 License.

## Support me
If you would like to offer me a coffee, here you go.

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/S6S88307C)

For modpack devs: You are permitted to use this mod without directly asking, but please credit me somewhere, it would help! (Also, I'm kind of a curious person so maybe send me a message when you include it into your modpack, and I'd like to check it out)
