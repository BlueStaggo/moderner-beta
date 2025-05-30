**WARNING: OLD WORLDS FROM 2.X ARE INCOMPATIBLE!**
However, they can be simply upgraded by replacing the "WorldGenSettings" compound in the level.dat file with a different "WorldGenSettings" compound from a newly generated world.

A major number change means a big update. For this update, significant parts of the codebase have been refactored to ease development for not just people working on this mod but also for people working *with* Moderner Beta. Some changes have been made to make the mod more modular and to be more customizable, including entirely reworking the fractal layer system. Old worlds from 2.x releases are incompatible.

## Additions
- Added "Snow ain't Snowier" preset for 1.17 biome generation with some modern biomes.
- Added large biome variants for the "Release Hybrid" and "Snow ain't Snowier" presets.

## Changes
- Reworked the fractal layer system:
  - All options for the fractal biome provider have been removed in place of a single option giving users control of every single layer in the pipeline through JSON.
  - The new system allows for much greater accuracy for the 1.12 and 1.17 presets as it can easily satisfy all sorts of edge cases without introducing too many options for the provider.
  - All fractal-based biome provider settings are incompatible for this release so they need to be adapted to the new format. This may be tedious for some users, but documentation may release some time soon.
  - Currently, the new layer system has no graphical configuration screen.
- Improved accuracy of the 0.0.14a_08 preset.
- Removed dependency on Architectury API.

## Fixes
- Fixed Beta water colours not working on NeoForge.
- Fixed Mangrove Leaves not having Beta leaf colouring.
- Fixed Bush, Pink Petal and Wildflower blocks not having Beta grass colouring.
- Fixed Tall Grass and Large Fern blocks not having consistent colouring between the two halves.
- Main noise scale now affects Indev worlds
- Terracotta stripes no longer generate in underground badlands.
  - This was done by disabling "addStoneDepth" for surface rules in Moderner Beta worlds; this may have unintended side effects with modded surface rules.
