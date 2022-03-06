# ColoredGlow Lib
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Emafire003/ColoredGlowLib/build?style=flat-square)
![Downloads CurseForge](https://cf.way2muchnoise.eu/full_coloredglowlib_downloads.svg?badge_style=flat)
![Modrinth](https://img.shields.io/modrinth/dt/coloredglowlib?color=green&label=Modrinth%20downloads&style=flat-square)
![Mc Versions](https://cf.way2muchnoise.eu/versions/Minecraft%20versions_coloredglowlib_all.svg?badge_style=flat)

This library enables you to make entities glow different colors other than plain white or based on their team color.

![coloredglowlib](https://user-images.githubusercontent.com/29462910/154980788-09722978-0594-48b2-a873-3d6b0434295b.png)


## Setup
### For normal users:
Just drag and drop this mod into your mod folder and enjoy!

#### Gamerules & commands
You can set weather or not to override the default team colors (the ones minecraft assigns) with the gamerule:

`/gamerule overrideTeamColors true/false`

It is recommended to leave this on false, since you may have some other mod/datapack/modpack that sets a team color to an entity, and it can get confusing.

You can specify a color to use for an entity using the following command:

`/setglowcolor <entity> <color`

The `entity` parameter is the entity you want to target, so @p, Emafire003, the uuid of the entity in front of you etc. It is not recommended to use @e, @a and 
stuff that targets a lot of entities since I will implement a command to set a color for each **type** of entity that will perform better. If you want you can use it anyway, for ~100ish entities it should run just fine, maybe even a lot more.

The `color` parameter is a hexadecimal color code (like #ff85ab, #750711, #abc, #123, #a7e ecc, search "color picker" online to get them) without the `#` (at least for now, in the future you will be able to include or not without problems)


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
If you want you can also `include` this (Jar-in-Jar dependency). To avoid confusion, tell the users of your mod that your mod includes this library already, in case of problems it would make debugging easier.
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
    include "maven.modrinth:coloredglowlib:<version>"
}
```

You can find the correct version in the [versions page](https://modrinth.com/mod/coloredglowlib/versions) on [Modrinth](https://modrinth.com/mod/coloredglowlib).


#### How to set the color in the code
To change the color of the glowing effect of entities use:
`ColoredGlowLib.setColor(Color color)`
(This will change the color of the effect itself, so for every entity. Scrolling down you will find how to do this specific EntityTypes and Entities)

You can use a `new Color(r,g,b)` object (Not AWT, the mod's Color object) or set a rgb color with `setColor(r,g,b)` or set a colorvalue with `.setColorValue()` which is an int that corresponds to an RGB value. You can get it using `Color.translateToColorValue(r,g,b)` or `RRRRRRGGGGGGBBBBBB`.

**WARNING!** The `Color` class is the mod's color class, `me.emafire003.dev.coloredglowlib.util.Color` and **NOT** `java.awt` 
or similar. This is because in some environments the awt package does not work. This applies to the rest 
of this guide too.

#### How to set a specific color for each EntityType
To use a custom color for each different type of entity (called EntityType) you can use the method 

`ColoredGlowLib.setColorToEntityType(EntityType type, Color color)`.

The `type` parameter is an EntityType, such as `EntityType.PIG`, so it works with modded entities to (like `ModEntitiesType.MYCUSTOMENTITY`).

To remove the custom color, call `removeColorFromEntity(EntityType type)`.

To disable this feature you will need to call the `ColoredGlowLib.setPerEntityTypeColor(boolean b)` and set the parameter to `false`. (It is enabled by default)


#### How to set a specific color for each Entity
To use a custom color for each different type of entity (called EntityType) you can use the method `ColoredGlowLib.setColorToEntity(Entity entity, Color color)`.

The `entity` parameter is the entity you want to target, such as a pig you are making shoot rockets from its rear-end. This works for every type of entity, even modded ones. (Internally, only their UUID is used).

To remove the custom color, call `removeColorFromEntity(Entity entity)`.

To disable this feature you will need to call the `ColoredGlowLib.setPerEntityColor(boolean b)` and set the parameter to `false`.

#### Color hierarchy
The mod first checks for any **easter-egg like names** and similar things, and it applies that without checking other things, then it checks to see if the **entity** has a color and does the same, then for its **EntityType** and finally if nothing else has been found it applies the "default" color common to all entities.
The one you set with `ColoredGlowLib.setColor(Color color)`.

**`easter-egg like names > entity > EntityType > default custom color`**

#### How to set a rainbow glowing
It is possible to set entities glowing rainbow using the method:
`ColoredGlowLib.setRainbowColor(boolean enabled);`

This will enable the effect for every entity. If you want, there are also entity/entitytype-specific methods.
`ColoredGlowLib.setRainbowColorToEntity(Entity entity, boolean enabled);`

`ColoredGlowLib.setRainbowColorToEntityType(EntityType type, boolean enabled)`

To disable the rainbow color, just use `false` as the parameter instead of `true`.



#### How to override minecraft default team colors via code
Just use `setOverrideTeamColors(boolean b);` Set it to `true` if you want to override the default minecraft team colors (if the entity is in a team) `false` if you want them to take priority over the mod's. (recommended since you could have other mods/datapacks/plugins that set entities inside teams with a specific color)

There is an example of this in my [FoxGlow](https://github.com/Emafire003/FoxGlow) mod.

![coloredglowlib2](https://user-images.githubusercontent.com/29462910/154981142-5f871d46-2f33-46f4-94a4-7885189b01a3.png)

#### Known issues
Currently, the setted color needs to be setted back manually at every game restart.

## License

This mod is available under the MIT License.

## Support me
If you would like to offer me a coffee, here you go.

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/S6S88307C)

For modpack devs: You are permitted to use this mod without directly asking, but please credit me somewhere, it would help! (Also, I'm kind of a curious person so maybe send me a message when you include it into your modpack, and I'd like to check it out)
