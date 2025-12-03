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

## Fixes
- Fixed the Voronoi Cave Biome provider having wrong components be used in the GUI
- Removed Trial Chamber generation in Indev presets
- Fixed incompatibility with Valkyrien Skies
- Fixed Serene Seasons not having seasonal grass tinting with Beta and Beta Fractal biome providers