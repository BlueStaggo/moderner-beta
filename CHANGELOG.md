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
- Ocean Shrines now generate in presets without Deep Ocean biomes
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
