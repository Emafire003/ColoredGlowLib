# ColoredGlow Lib
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Emafire003/ColoredGlowLib/build?style=flat-square)
![Downloads CurseForge](https://cf.way2muchnoise.eu/full_coloreglowlib_downloads.svg?badge_style=flat)
![Modrinth](https://img.shields.io/modrinth/dt/coloredglowlib?color=green&label=Modrinth%20downloads&style=flat-square)
![Mc Versions](https://cf.way2muchnoise.eu/versions/Minecraft%20versions_coloredglowlib_all.svg?badge_style=flat)

This library enables you to make enities glow different colors other thank white or based on their team color.

![coloredglowlib](?)

## Setup
### For normal users:
Just drag and drop this mod into your mod folder and enjoy! It will require another mod to be functional (for now, i'll probably add a command to do this later)

### For developers:
Include this library into your `build.gradle` as a dependency
`  repositories {
        maven { url "https://jitpack.io" }
   }
   dependencies {
         modImplementation 'com.github.emafire003:coloredglowlib:1.0.0'
   }`
If you want you can also `include` this (Jar-in-Jar dependency). To avoid confusion, tell the users of your mod that your mod includes this library already, in case of problems it would make debugging easier.
`  repositories {
        maven { url "https://jitpack.io" }
   }
   dependencies {
         modImplementation 'com.github.emafire003:coloredglowlib:1.0.0'
         include 'com.github.emafire003:coloredglowlib:1.0.0'
   }`

Then, to change the color of the glowing effect of entities use:
`ColoredGlowLib.setColor()`
You can use a `new Color(r,g,b)` object (Not AWT, the mod's Color object) or set a an rgb color with `setColor(r,g,b)` or set a colorvalue with `.setColorValue()` which is an int that corresponds to an RGB value. You can get it useing `Color.translateToColorValue(r,g,b)` or `RRRRRRGGGGGGBBBBBB`.

There is an example of this in my [FoxGlow](https://github.com/Emafire003/FoxGlow) mod.

![fox1](https://user-images.githubusercontent.com/29462910/152815217-8ca8abcf-2dfe-4c20-8235-84013a047c1e.png)

## License

This mod is available under the MIT License.

## Support me
If you would like to offer me a coffee, here you go.

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/S6S88307C)

For modpack devs: You are permitted to use this mod without directly asking, but please credit me somewhere, it would help! (Also, i'm kind of a curios person so maybe send me a message when you include it into your modpack and i'd like to check it out)
