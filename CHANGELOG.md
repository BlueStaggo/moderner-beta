**WARNING: OLD WORLDS FROM 2.X ARE INCOMPATIBLE!**
However, they can be simply upgraded by replacing the "WorldGenSettings" compound in the level.dat file with a different "WorldGenSettings" compound from a newly generated world. Unmodified presets are now referred to by IDs so breaking changes should no longer have as much impact.

A major number change means a big update. For this update, significant parts of the codebase have been refactored to ease development for not just people working on this mod but also for people working *with* Moderner Beta. Some changes have been made to make the mod more modular and to be more customizable, including entirely reworking the fractal layer system. Old worlds from 2.x releases are incompatible.

## Additions
- Modern caves are back! Access them with the new "Generate Noise Caves" / "useNoiseCaves" setting.
- Added "Snow ain't Snowier" preset for 1.17 biome generation with some modern biomes.
- Added large biome variants for the "Release Hybrid" and "Snow ain't Snowier" presets.
- Added networking code to send biome provider settings along with the world seed to allow for Beta biomes to have their true colours on multiplayer.
- Added a biome previewer to preview the biomes that will be generated for a world.
- Added a toggle for farlands. Now farlands can be made to generate in release worlds and stop generating in beta worlds.
- (1.21+) Added Trial Chambers to Moderner Beta biomes.

## Changes
- The mod now supports Minecraft 1.21.6.
- **BREAKING CHANGE**: Every single property has been moved.
  - This is due to a more modular approach to configuration being implemented. This allows for addons to use the Moderner Beta API to add their own configuration options.
  - This also affects how settings appear in graphical configuration menus. They should still all be accessible, just in different positions.
  - All pre-3.0.0 presets are guaranteed to not work under the new system so they need to be recreated. To find out what values go where, look at the built-in presets which cover every single settings component.
- **BREAKING CHANGE**: Reworked the fractal layer system.
  - All options for the fractal biome provider have been removed in place of a single option giving users control of every single layer in the pipeline through JSON.
  - The new system allows for much greater accuracy for the 1.12 and 1.17 presets as it can easily satisfy all sorts of edge cases without introducing too many individual options.
  - All fractal-based biome provider settings are incompatible for this release so they need to be adapted to the new format. This may be tedious for some users, but documentation may release some time soon.
  - Currently, the new layer system has no graphical configuration screen.
- **BREAKING CHANGE**: Moderner Beta now uses the vanilla registry implementation instead of its own custom implementation.
- Settings can now be referenced by their preset id.
  - This eliminates the chance of breaking changes affecting worlds that use built-in presets. Worlds using custom presets are still affected.
  - However, preset ids can be combined with other settings components through JSON to provide better compatibility for custom presets. For example, to generate noise caves in a Beta 1.7.3 world, the chunk provider settings would look like this:
    ```json
    {
      "moderner_beta:preset": "moderner_beta:beta",
      "moderner_beta:cave_generation": {
        "useNoiseCaves": true
      }
    }
    ```
    *Note that other values in `moderner_beta:cave_generation` default to beta values.*
- Improved accuracy of the 0.0.14a_08 preset.
- Improved accuracy of Beta 1.8, 1.0.0 and 1.1 climate, providing the signature messy look as well as ice plains generating without snow on its edges.
- Biomes with custom grass/foliage colors now display their colors in worlds with custom climate while blending in the climatic base color.
- Removed dependency on Architectury API.
- Made ocean biome injectors not be used if the biome provider already generates oceans.
- Moved deepslate and tuff blobs from the reduced height data pack into a separate data pack.
- Major release worlds can now be generated without surface rules.
- Carvers in Beta 1.8+ now have the same seeds.
- The Drought preset no longer has gravel ocean beds.
- The /locate command no longer identifies the location of injected biome including ocean biomes in beta worlds as well as cave biomes. Biome injectors heavily slowed down the biome location process.
- (1.21+) Replaced use of `@Redirect` mixins with `@WrapOperation` mixins.
- (1.21 only) Mod is now built using NeoForge 21.1.173 to fix crashes when entering the mod config screen on NeoForge 21.0.110-beta or newer.

## Removals
- Removed fixed seed options, as it has been made redundant by the addition of the networking code for biome provider settings and world seed.
- Removed biome tags relating to fractal biome generation (this does not include height config tags).

## Fixes
- Fixed Beta water colours not working on Forge/NeoForge.
- Fixed Mangrove Leaves not having Beta leaf colouring.
- Fixed Bush, Pink Petal and Wildflower blocks not having Beta grass colouring.
- Fixed Tall Grass and Large Fern blocks not having consistent colouring between the two halves.
- Fixed buried treasure not generating in worlds without beaches.
- Main noise scale now affects Indev worlds.
- Fixed terracotta stripes generating in underground badlands and other surface rule related bugs.
- Snowfall now occurs in the right places in worlds.
