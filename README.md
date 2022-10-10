# ColoredGlow Lib
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Emafire003/ColoredGlowLib/build?style=flat-square)
![Downloads CurseForge](https://cf.way2muchnoise.eu/full_coloredglowlib_downloads.svg?badge_style=flat)
![Modrinth](https://img.shields.io/modrinth/dt/coloredglowlib?color=green&label=Modrinth%20downloads&style=flat-square)
![Mc Versions](https://cf.way2muchnoise.eu/versions/Minecraft%20versions_coloredglowlib_all.svg?badge_style=flat)

This library enables you to make entities glow different colors other than plain white or based on their team color.

![coloredglowlib-with-player](https://user-images.githubusercontent.com/29462910/157507551-dfc4ee7e-66fb-4dae-9578-e17ca64e3b44.png)

## Setup
### For normal users:
Just drag and drop this mod into your mod folder and enjoy!

#### Commands & Configuration
Every command begins with `/cgl` , short for **C**olored**G**low**L**ib. You can also use `/coloredglowlib`. 

You can specify a color to use for an entity/entitytype using the following command:

`/cgl setglowcolor <entity/type> <color>`

The `entity` parameter is the entity you want to target, so @p, Emafire003, the uuid of the entity in front of you etc or it's type such as `minecraft:sheep`. It is not recommended to use @e, @a and stuff that targets a lot of entities. If you want you can use it anyway, for ~1000ish entities it should run just fine, maybe even a lot more. At around 4k tho it starts to complain so don't push it.

The `color` parameter is a hexadecimal color code (like #ff85ab, #750711, #abc, #123, #a7e ecc, search "color picker" online to get them) without the `#` since minecraft interprets it as a beginning of a tag (which is not what it should be doing in this case). In alternative you can provide the word `rainbow` that will make the entity glow, you guessed it, rainbow.

You can interact with the config directly in game. For example if you want to override the default team colors (the ones minecraft assigns) with the command:

`/cgl config set overrideTeamColors <true/false>`

It is recommended to leave this on false, since you may have some other mod/datapack/modpack that sets a team color to an entity, and it can get confusing.

You can also get the current value of the setting, like so: 

`/cgl config get overrideTeamColors`

More of this in the wiki!

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


#### You can find more information on the [wiki](https://github.com/Emafire003/ColoredGlowLib/wiki)! 

You can find examples of this lib being used in my [FoxGlow](https://github.com/Emafire003/FoxGlow) and [Glowful World](https://github.com/Emafire003/GlowfulWorld) mod.

![luma-shaders-coloredglowlib](https://user-images.githubusercontent.com/29462910/157507676-576d3fb5-e24b-41f7-a7f0-6956d7ae4e29.png)

## License

This mod is available under the GNU LGPL3 License.

## Support me
If you would like to offer me a coffee, here you go.

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/S6S88307C)

For modpack devs: You are permitted to use this mod without directly asking, but please credit me somewhere, it would help! (Also, I'm kind of a curious person so maybe send me a message when you include it into your modpack, and I'd like to check it out)
