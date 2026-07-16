# 5.0.0-alpha.2

**WARNING!: This is an alpha version, which may include bugs and issues, and can be prone to breaking with future releases.**

## Additions
- Added the `moderner_beta:y_above` biome injection predicate

## Changes
- Attempted to make Beta big Oak Tree generation more accurate.
- Made Classic and Indev lava flooding more accurate.
- Improved performance in some cases, when using surface rules.

## Fixes
- Fixed crashes with Lithostiched
- Fixed regression with Infdev 415 terrain generation, which was introduced in 4.0.0
- Fixed beaches generating not high enough, when surface rules are enabled.

# 5.0.0-alpha.1

**WARNING!: This release introduces breaking changes. Additionally, this is an alpha version,
which may include bugs and issues, and can be prone to breaking with future releases.**

## Additions
- Added support for Minecraft 26.2
- Added component for structure modifiers
  - This allows to modify how structures generate, or allow for custom structures to generate on a per-preset basis.
- Added data-driven biome injectors
  - This new system allows for more advanced biome generation
  - In future versions this system will be expanded further
- (1.20.1) Added support for Forge biome tags.

## Changes
- Ocean Shrines now generate in most presets without Deep Ocean biomes
  - Previously they only generated in Moderner Beta Ocean biomes.
- Strongholds now generate properly in LCE presets
- Moved erosion generation to surface rules
  - NOTE: Currently erosion has issues with water when sea level is not 63.
- Enabled surface rules by default in presets with cave biomes
- Surface rules now use same surface depth as normal surface generation
- Surface rules now use precise preliminary height, rather than quantised height
- Surface rules now use precise biome for certain biome providers
  - This is mostly noticeable with the Beta biome provider.
- Refactored biome generators to use biome references directly
  - NOTE: This does not apply to the fractal biome generators, as those use an extended ID system that requires a rework.
- Refactored noise generation to use a common interface
- `/locate` can now locate injected biomes from some biome injectors.
- General optimisations for world generation.

## Fixes
- Fixed cave biome and ocean biome injection occurring after surface generation.
  - This notably fixes frozen ocean biome generation, along with sulfur cave generation.
- Fixed certain Moderner Beta biomes not generating properly with surface rules enabled.

## Removals
- Removed support for Minecraft 1.21.6-1.21.8, 1.21.9-1.21.10, and 1.21.11
- Removed GUI settings for biome injectors
  - It is currently not known if these will be added back due to the complexity of biome injectors.
- Removed climate mappings for Ocean and Deep Ocean biomes
  - These are now to be handled by the biome injector.
- Removed Ocean and Deep Ocean layer types
  - These should be handled by the biome injector
  - Currently, no exact replacement exists, and will only be added in a future release.
- Removed the `moderner_beta:indev_stronghold` structure
  - It is now done using the structure modifier component.

# 4.1.8

## Fixes
- Fixed custom surface rules not being added by mods in Moderner Beta in most cases
- Fixed up invoker mixin to not cause warning

# 4.1.7

## Fixes
- Fixed where pressing ESC in the graphical settings screen causing to return to the title screen.
- (1.20.1) Fixed crash with TerraBlender

# 4.1.6

## Additions
- Added keyboard input support to preset selection GUI

## Fixes
- Fixed issues with TerraBlender and surface rules in Moderner Beta

# 4.1.5

## Fixes
- Fixed biome previewer not working in some cases
- Fixed Beta Fractal biome provider crashing game
- (26.1) Fixed grass block breaking particles being tinted
- (26.1) Fixed grass sides and foliage being invisible in some cases

# 4.1.4

## Fixes
- Fixed Fractal biome provider being seeded wrongly on world first load.
- (26.1) Fixed text boxes being broken with Moderner Beta.

# 4.1.3

## Fixes
- Fixed crash with some mods like Citadel and Alex's Mobs
- Fixed crash when attempting to use Voronoi cave biome provider default settings
- Fixed crash when using odd vertical sample size with 3D Noise chunk provider
- Fixed 3D Noise chunk provider not making use of climate values from currently set biome provider.
  - NOTE: This is only noticeable with certain configurations, however could possibly happen more often with 3rd-party addons.

# 4.1.2

## Changes
- Made mushroom generation more accurate in presets with all custom biomes
- Renamed the Noise Caves option to Modern Caves to reduce confusion.

## Fixes
- Fixed crash with Serene Seasons on Forge 1.20.1
- Fixed biome injection being broken when going up and down vertically.

# 4.1.1

## Additions
- Added support for Minecraft 26.1

## Changes
- Fixed inaccuracies with biome generation on Major Release presets
- Made the Bedrock carver seeding accurate.

## Fixes
- Fixed regression with Beta beach generation caused in 4.0.0

# 4.1.0

## Additions
- Added World Border options, with options for terrain falloff, size and position.
- Added Uniform Bedrock Generation option for generating a flat Bedrock floor.
- Added a layer output for fractal biome providers just for adjusting biome height
- Added layer outputs for injecting ocean and deep ocean biomes
- The heights that oceans, deep oceans and cave biomes generate at can now be adjusted

## Changes
- Legacy Console presets have been moved to the main categories.
- Legacy Console presets are now fully accurate, comparing with their respective versions.
- Finite presets now make use of the new World Border options.

## Fixes
- Fixed Data Pack exporting not working correctly on 1.20.1
- Removed nonfunctional options from the GUI for finite Chunk Providers.

# 4.0.4

## Fixes

- Fixed Bedrock not generating in the Infdev 227 chunk provider when surface rules are enabled
- Fixed some components having their values be broken after editing settings in the GUI in some cases

# 4.0.3

## Changes
- Improved compatibility with Serene Seasons

## Fixes
- Fixed incompatibilities with Optifine

# 4.0.2

## Fixes

- Fixed Bedrock generation not respecting the settings in Surface Properties when surface rules are enabled.
- Fixed Bedrock generation not working when surface rules and Deepslate generation are enabled.
- Fixed bug where some list based GUIs do not adjust properly upon the game window being resized

# 4.0.1

## Fixes

- (1.21.11) Fixed Reduced Height datapack having wrong overworld dimension properties set, resulting in no skylight.
- (1.21.11) Fixed sky colour not changing with weather and time.
- Fixed divide by zero error with some grass colours sampled from colour maps.

# 4.0.0

**WARNING: This version introduces breaking changes, which might break your world(s) and/or Data Packs.
You may have to update them due to the changes in this version. Modders will also have to update their mods!**

## Additions
- Added a Quick Import/Export screen. Useful for when having to share the current settings to others, or to set it
  within `server.properties`
- Added Data Pack exporting, for easily exporting the current settings as a datapack that adds it as a preset.
- Added support for the `generator-settings` field within `server.properties`. It accepts either the values from the
  Quick Import/Export screen, or a shorter format which just takes the preset (example: `{"preset": "moderner_beta:beta_vanilla"}`)
- Added Moderner Beta biomes to tags used by Serene Seasons.
- Added a component to change noise generation parameters, including total terrain height and starting height.
- Added a component to control perlin noise generator properties, including the ability to set the point noise
  generation breaks down, allowing to move the Farlands nearer to world origin.

## Changes
- Ported the mod to 1.21.11.
- Migrated the mod to Mojmap, in preparation to migrate to a less brittle build system and for Minecraft 26.1
- Generalised the following chunk providers into a single 3D noise chunk provider:
  - Infdev 415
  - Infdev 420
  - Infdev 611
  - Alpha
  - Beta
  - Skylands
  - Pocket Edition
  - Early Release
  - Major Release
  - Early Bedrock
- Generalised the Indev and Classic chunk providers into one.
- Presets and Preset Categories can now have defined names and descriptions, along with formatting.
- Deepslate is no longer baked into the surface rules of Moderner Beta worlds.
- The default preset is now swapped for the one set in the settings, rather when getting the preset
- Now only the differences between the preset and changes are saved into the world settings, reducing impact of breaking
  updates
- Indev Woods and Hell now have custom skylight tinting on 1.21.11
- Added a search box to the biome selection screen, borrowed from 1.21.11
- Surface Configs are now data driven.
- Major Release LCE presets now use Early Release style cave generation.
- Biome colour configuration now can take hex colour codes in 1.21.11
- Replaced the sea level offset component with one that directly changes sea level, instead of offsetting it

## Fixes
- Fixed the Voronoi Cave Biome provider having wrong components be used in the GUI
- Removed Trial Chamber generation in Indev presets
- Fixed incompatibility with Valkyrien Skies
- Fixed Serene Seasons not having seasonal grass tinting with Beta and Beta Fractal biome providers
- Fixed surface generation going down to underground structures like Ancient Cities in the Infdev 227 chunk provider,
  when surface rules are disabled.

# 3.2.1

## Changes
- The 1.21.9 version now supports 1.21.10
- Beta cave carvers now use surface rules when they are enabled.

# 3.2.0

## Additions
- Added AMPLIFIED presets, which reproduce AMPLIFIED world type terrain generation.
- Added back Ocean Shrines from pre-1.19 versions of the original Modern Beta mod.
- Added option to toggle ocean biome injection in the GUI.
- Added option to only apply forced-height modifiers when depth is above zero.
- Voronoi biome provider now has options to adjust climate noise scales from the GUI.

## Changes
- The mod now supports 1.21.9.
- 1.20.1 version now makes use of MixinExtras for compatibility.
- LCE presets now have fully accurate biome generation

## Fixes
- Fixed Bedrock biome generation with seeds that are negative integers
- Fixed some chunk providers not reporting proper sea level to the biome injector

# 3.1.4

## Changes
- The 1.21.6 version now supports 1.21.8.

## Fixes
- (1.21.1 and older) Fixed presets containing entries for the Pale Garden biome.
- Fixed reduced height datapack not working with the Early Bedrock noise provider
- Fixed Voronoi biome provider GUI configuration having Ocean and Deep Ocean biomes be set to None.

# 3.1.3

## Changes
- The 1.21.6 version now supports 1.21.7.

## Fixes
- Fixed Beta Vanilla preset not generating ocean biomes.
- Fixed a crash when biome climate values are outside the normal range.

# 3.1.2

## Fixes
- Fixed issues with the Blueprint mod.
- Fixed biome color overrides not appearing for climatic biome providers with Sodium and other rendering mods.

# 3.1.1

## Fixes
- Fixed Classic 0.30 and 0.0.14a_08 presets not working and causing a crash.

# 3.1.0

## Additions
- Added experimental Bedrock 1.2 and Bedrock 1.17 presets based on the 1.12.2 and 1.17.1 presets respectively but with Bedrock Edition biome and terrain generation. Farlands and carvers are currently inaccurate.
- Added legacy console presets for various world sizes based on the 1.12.2 preset.
- Added the Early Bedrock chunk provider to allow for the use of Mersenne Twister RNG and the 128 block height limit for forced height worlds.
- Added a Perlin Zoom layer.
- Added In Range and In Grid biome predicates.

## Fixes
- Fixed plains borders between swamps and deserts, snowy taigas and snowy plains in 1.7+ presets being missing.
- Fixed cave generation seed method not appearing in the graphical configuration screen

# 3.0.0

**WARNING: OLD WORLDS FROM 2.X ARE INCOMPATIBLE!**
However, they can be simply upgraded by replacing the "WorldGenSettings" compound in the level.dat file with a different "WorldGenSettings" compound from a newly generated world. Unmodified presets are now referred to by IDs so breaking changes should no longer have as much impact.

1. Create a new world with the latest version of Moderner Beta with the same seed and preset as the old world.
2. Open the level.dat file of the new world in an NBT editor and copy the "WorldGenSettings" tag.
3. In the same editor, open the level.dat file of the old world, paste over the existing "WorldGenSettings" tag and save.

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
  - However, preset ids can be combined with other settings components through JSON to provide better compatibility for custom presets. For example, to create a beta world with noise caves:
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
- Removed dependency on Cloth Config, thus changing how the configuration screen looks to be more consistent with the world customization screen.
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

# 2.1.8

## Fixes
- Fixed sea level not being set properly for chunk provider when entering chunk provider settings
- Fixed crash when opening chunk provider settings.

# 2.1.7

## Changes
- Moved client-side initialisation code to be handled entirely by client-side code
  - This fixes a crash on NeoForge

# 2.1.6

NOTE: This release is only for 1.21.5

## Changes
- Preliminary port to 1.21.5
- Added new biome features to biomes used by this mod.
  - This includes fallen trees, bushes, desert grass and firefly bushes
- Leaf litter now generates in forest biomes
  - NOTE: It does not currently generate under large oak trees.

# 2.1.5

NOTE: This release is only for 1.21.4

## Changes
- Fixed incompatibility with Architectury API 15.0.2 and NeoForge 21.4.84-beta.

# 2.1.4

## Changes
- Fixed custom fractal edge variants not being used

# 2.1.3

## Changes
- Made major release generation more accurate.
- Improved biome selection GUIs
  - It is now possible to select "The Void" as a biome
  - Made it so you cannot have none biome in cases where a biome is required

# 2.1.2

## Additions
- Updated the mod to 1.21.4
- Added the new Pale Garden to Release Hybrid and Beta Vanilla presets.

## Fixes
- Fixed a crash involving certain fractal biome generator configurations.

# 2.1.1

## Fixes
- Fixed an off-by-one error with underground lava generation

# 2.1.0

## Additions
- Added chunk provider setting `forceBetaRavines`.
  - Set to `true` by default.
  - Generates pre-1.18 ravines which were twice as common and generated closer to the bottom of the world.
- Added biome provider setting `addStonyShores`.
  - Set to `false` by default, set to `true` in major release presets.
  - Replaces beaches with stony shores with biomes that are part of the `moderner_beta:fractal_has_stony_shores` tag.

## Fixes
- The reduced height datapack prevents "deep" caves from generating

# 2.0.2

## Changes
- Increased scale of landmasses in the Release Hybrid preset.

## Fixes
- Raised lava level in beta caves by one block.

# 2.0.1

## Fixes
- Grass and leaves should no longer appear transparent under certain conditions.

# 2.0.0

The highly anticipated graphical customization menu has made a return! Every single option can be configured from graphical menus with JSON editing still available as an option.

## Additions
- Added graphical configuration menus for chunk, biome and cave biome providers.
  - Settings can still be edited as JSON by clicking any of the pencil icons to the right of the "Settings" buttons.
- Presets are now sorted into categories, allowing for easier browsing.
- Added large biomes presets for release versions.

## Changes
- BREAKING CHANGE: Reworked fractal sub-variants
  - `fractalSubVariants` used to be `Map<BiomeInfo, List<BiomeInfo>>` and controlled by `fractalSubVariantScale`,
    but now it is of the type `Map<Integer, Map<BiomeInfo, List<BiomeInfo>>>` with integers surrounded in quotes.
    Here's an example of the change:
    ```json5
    [
      // Before
      {
        "fractalSubVariants": {
          "minecraft:jungle": [
            "minecraft:plains",
            "minecraft:sparse_jungle",
            "minecraft:jungle"
          ]
        },
        "fractalSubVariantScale": 1,
        "fractalSubVariantSeed": 3000
      },
      // After
      {
        "fractalSubVariants": {
          "1": { // 1 formerly "fractalSubVariantScale"
            "minecraft:jungle": [
              "minecraft:plains",
              "minecraft:sparse_jungle",
              "minecraft:jungle"
            ]
          },
        },
        "fractalSubVariantSeed": 2999 // Subtract scale from seed accordingly
      }
    ]
    ```
  - This allows for a wider variety of customization for fractal biome generation.
  - Old presets will be automatically updated to use the new sub-variant system.
- Changed up biome distribution in the Release Hybrid preset to be more like 1.6.4 with additional biome mutations.
- The default block for Badlands soil has been changed from White Terracotta to Orange Terracotta.

## Fixes
- Added Ice Spikes to 1.12.2+ presets.
- Infdev 20100325 caves now overwrite ores with air.
- Fixed some deep oceans having the incorrect height values.
- Mutated variants now save properly.

## Removals
- Removed Beta Hybrid preset.

# 1.4.0
## Changes
- Ported to Minecraft Forge using Architectury
  - Due to this the mod now depends on [Architectury API](https://modrinth.com/mod/architectury-api).
- Dropped "For Fabric" from logos and all assets.
  - The icon has been updated on Fabric to add the logo for the mod
  - The banner and icon now feature a new screenshot
- Dropped Fabric suffix from mod name.
- Dropped 6.5+er version prefix. 
- Mod ID has been changed to `moderner_beta` to distinguish from the original Modern Beta mod.
- Mentions of Modern Beta ingame have been replaced with Moderner Beta.

# 1.3.1
## Fixes
- Bedrock now generates correctly in Infdev 415+ world generators with surface rules enabled

# 1.3
Woo! New world generators! Classic 0.0.14a_08 and Infdev 20100325 are now available as well as a datapack to reduce world height.

## Additions
- Added Classic 0.0.14a_08 and Infdev 20100325 world generators.
  - Note: Caves in Infdev 20100325 are tied to the biome so they will not generate if a different biome provider is used and they will still generate if a different chunk provider is used.
- Added a built-in datapack that raises the minimum Y value from -64 to 0 and restores pre-1.18 ore generation.
  - This datapack can be accessed through the datapacks menu on world creation.
- Added plenty of options for Indev and Classic worlds:
  - indevNoiseScale: Scale for the main terrain noise
  - indevSelectorScale: Scale for selector noise
  - indevMinHeightDamp and indevMaxHeightDamp: Reduced steepness of the terrain
  - indevMinHeightBoost and indevMaxHeightBoost: Base height of the terrain
  - indevHeightUnderDamp: Reduced steepness of terrain below sea level
  - indevCaveRarity: Rarity and spread of caves
  - indevSandBeachThreshold and indevGravelBeachThreshold: How much beach there is
  - indevSandBeachUnderAir and indevGravelBeachUnderAir: Generate beaches that are exposed to air above them
  - indevSandBeachUnderFluid and indevGravelBeachUnderFluid: Generate beaches that are exposed to water or lava above them
  - indevWaterRarity: Rarity of water pools
  - indevLavaRarity: Rarity of lava pools
  - indevSpawnHouse: Generate a house at the spawn location

## Changes
- Hills variants of savannas now uses the savanna plateau biome.
  - That means that plateau generation outside of 1.12 and 1.17 is broken, but honestly I'm too lazy to fix that until the next release.
- Late Beta Taigas and Early Release Taigas no longer generate with ferns.
- Tree noise now uses 64 bit seeds instead of 32 bit seeds.
  - Generation of worlds that use tree noise (anything between Infdev 325 and Beta 1.7.3) has therefore been slightly altered with changes being most apparent in Alpha and Infdev.

# 1.2.3
Yet another slight oversight (I release updates too often, don't I?)

## Fixes
- Fractal climatic biomes now generate correctly with oceans disabled
- Some biomes now have proper names

# 1.2.2
Oopsies

## Fixes
- Windswept Gravelly Hills now have the correct height values

# 1.2.1
Just two minor details I just noticed

## Fixes
- Gravel now generates below sea level in 1.7+.
- Erosion noise in Beta 1.8 - 1.6.4 no longer suffers from floating point imprecision.

# 1.2
Feature update! New presets!

## Additions
- Added 1.17.1 preset with ocean biomes and bamboo jungles.
- Added various presets from Old Customized as well as beta variants.
- Added chunk generation option seaLevelOffset to adjust the sea level.

# 1.1.2
Just one slight oversight

## Fixes
- Some biomes now have the correct height values in the 1.6.4 preset

# 1.1.1
Fixing just a few more bugs

## Changes
- The Beta Vanilla preset now uses fixed caves

## Fixes
- Gravel beaches now generate correctly with surface rules
- The useFixedCaves chunk generator option now correctly saves

# 1.1
This update fixes some issues with the first Moderner Beta release.

## Additions
- Added Beta 1.1_02 and Beta 1.9 Prerelease 3 presets.
  - Beta 1.1_02 generates only oak trees.
  - Beta 1.9 Prerelease 3 does not generate short grass or flowers in ice plains.

## Fixes
- Snow biomes are now shape correctly in Beta 1.9 - Release 1.6.4.
- Features of biomes only available as hills, edges or mutations now generate correctly.
  - Therefore, every missing mutated biome from 1.12.2 has been added back into world generation.

# 1.0
This is the first release of Moderner Beta and it introduces terrain generation from early release versions of Minecraft.

## Additions
- Added Beta 1.8, 1.0.0, 1.1, 1.2.5, 1.6.4, 1.12.2, Beta Hybrid and Release Hybrid presets.
  - There are some inaccuracies, like 1.0.0-1.6.4 having differently shaped snowy biomes and 1.12.2 missing some mutated variants of biomes.
- Added Early Release (Beta 1.8 - 1.6.4) and Major Release (1.7 - 1.17) chunk generators.
- Added Fractal biome provider.
  - There are a total of 24 settings for this biome provider including adjusting the biome scale, replacing certain biomes, adding/removing certain features and adding in variants of biomes.
- Added chunk generation option releaseHeightOverrides to adjust the height of different biomes.
- Added chunk generation option useSurfaceRules to generate surfaces by vanilla means.
  - This fixes biomes such as badlands and old growth taigas generating with incorrect blocks.
  - Additional surface features such as beaches and erosion are preserved.
- Added chunk generation options useFixedCaves that fixes harsh cave borders and forceBetaCaves to force every biome to use beta cave carvers.
- Custom presets now have icons.
