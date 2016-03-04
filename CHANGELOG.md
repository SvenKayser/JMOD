Changelog

v1.5.1
- Added Script Interface for RotaryCraft GrinderRecipes
- Added Script Interface for RotaryCraft BlastFurnaceRecips
- Removed conditioned Descriptors as a concept in favor of in-script conditions
- Massive cleanup to integrate operations into descriptors away from spaghetti code

v1.5.0 fixes
- Fixed OreDictionary Removal
- Reordered execution to account for mods registering oredict entries late in the init process
- Allowed for multiple buffs to be applied to one food
- Made sure every StagedObject implements it's own validation check
- Fixed armor repair using the wrong armor index numbers, potentially crashing in a division by zero
- Fixed error in subScripts not being caught
- Fixed wildcard removal of itemstacks from OreDict missing the script implementation
- OreDict Removal is more robust to weird item registration customs
- Added EnderIO Silicon being made with the blast furnace

v1.5.0
- Implemented addition and removal of chest loot
- Massively improved handling of script errors
- Improved type handling in the config scripts by a lot
- The config can now load sub scripts (which can load subscripts (which can load subscripts (which can load .....
- Fixing the game crashing laughing when milking cows empty handed.
- Fixed custom chisels trying to access the renderer on the server side
- Wooden Water buckets now are proper Fluid Containers to Forge
- Implemented an interface to alter drops when harvesting blocks under several conditions
- Added interface to create carving variations for chisel
- Some important performance tweak (more pending)
- A literal c**p ton of work on pretty much everything
- Almost certainly added Herobrine


v1.4.1.6
- **Major Change** switched from JSON config to "Nashorn" based JavaScript configuration
- Standard configs are no longer shipped with the mod
- Standard textures are no longer shipped with the mod
- Many now obsolete classes have left the project
- Added "Chisel" as a configurable tool type, obsoletes si.flintchisel
- The item registry now applies the complete tool data descriptor to ISITool tools instead of just the material.
- Chisels don't break anymore if the tool break prevention is active
- Chisels now can be repaired
- milking cows with the wooden bucket now respects hunger overhauls milkingdelay
- several minor fixes
- bunch of improvements under the hood
 

v1.4.1.5
- Fixed OreDict comparison not creating an appropriate match under some conditions
- Fixed OreDict comparison crashing when null ItemStacks were applied
- Updated textures with those from the SI resource pack
- Added support for HungerOverhauls hoe damage modifier to prevent it from breaking hoes
- Added HSLA as a tool and armor Material to the game and updated RoC tool (and now armor...) patcher
- Added HSLA tool and armor repair
- Removed Leggins
- Added Leggings
- Rebalanced some of the armor materials to better reflect their rareness and point in the progression
- Fixed EnderUtilities tools crashing when trying to repair
- Fixed EnderUtilities tool material typo
- Buffed EnderUtilities tools to better reflect their ultimate tool status
- Added support for EnderUtilities tool repairs
- EnderUtilities ender alloys have joined the oreDictionary

v1.4.1.4
- Added FMLCorePlugin si.core.plugin
- Added ASM patch to allow Anvil operations that require 0 levels
- Added Custom Anvil Handler, buffing the anvil to give incentive to use it
- Added Inventory Repairs from raw Items for tools (explicitly not armor)
- Added Repair Material to toolMaterial and armorMaterial config
- Added darkSteel to toolMaterial and armorMaterial patchlist
- Added generic armor patcher
- Nerfed darkSteel to require 1200 deg C in the Blast furnace (might require further nerf)
- ItemStack Substitutes now accept mod conditions and can now be chained (infinitely!)
- RecipePurges now accept mod conditions
- Added option to prevent tools from breaking
- some internal fixes, tweaks and improvements


v1.4.1.3
- Several minor fixes and log improvements
- Fixed Ender Quarry Progression

v1.4.1.2
- Added support for custom dyes
- Added Weeeeeeeee[...]eeeeee! Flowers integration to config

v1.4.1.1
- Fixed several server-only related bugs and crashes

v1.4.1.0
- Changed versioning to simply apply a 4th incremental number to the SI version number
- Fixed Creative tab functions executing on servers (hopefully)
- Added support for armor items and armor materials
- Removed crucible purging vanilla recipes
- Ore to ingot recipes in the crucible now duplicate ores
- Added basic support to remove recipes
- Added support to hide items from NEI
- Added support for generic tooltips
- Added Wodden buckets for water and milk (fluid container support pending)
- Added EnderIO alloying support
- Added support for generic food items
- Added support to apply buffs (potion effects) to these food items
- Updated si.core's default config to match SI 1.4.1 astral acorn
- Translated most of SI's MineTweaker Scripts to si.core's default configuration utilizing the new game manipulation tools
- Added modpack dependency checker (plus annoying notification)
- Added my favorite breakfast 
- Potentially added Herobrine
- bunch of minor fixes & stuff


v1.4.0-a2
- Added tooltip to blocks to reflect if a block is safe from (most) explosions
- Bumped (most) ores blast resistance to be TNT safe
- Bumped some chisel'd materials (smoothstone, factory, iron, granite) to be TNT safe
- Bumped RoC's decoblocks (Bedrockalloy, Steel, f.e.) to be TNT safe

v1.4.0-a1
- Fixed CdInAg block to work with ReC v10
- Shovel and Axes now also display their harvestLevel

v1.3.1-a1.6a
- Pushed reteo's tool materials into base config
- Added !COG to the default conditions for the ore generators

v1.3.1-a1.6
- Fixed base config: Flint tools are no longer mutually exclusive to RoC or M4
- added error handling to oregen
- Fixed Oregen stupidly trying to run 0 generators.
- presetting settings now so new version don't crash reading old versions configs (at least not if they are somewhat compatible)
- added "buildtypetest" target for gradle to run with default config instead of si.config.json

v1.3.1-a1.5
- Removed some more log spam
- Added handling to update enderutilies tools
- Added harvestlevel tooltip support for the enderutilities pickaxe

v1.3.1-a1.4
- Hidden the technical versions of the crucible from NEI
- Fixed the damn Creative tab
- Updated mcmod.info
- Now with configurable vanilla-style Ore (or whatever you want) generation!
- Added a bunch of settings to shut off different parts of the mod
- Added a Super-Setting that disables everything the mod does for use as lib only

v1.3.1-a1.3
- Implemented Tool Building from config
- Added config and textures for steel, copper, bronze and flint tools
- Added config, textures and oredictionary for copper, tin and silver ores (no worldgen yet)
- removed old recipe setter as it wasn't used anymore anyways
- switched to JAVA1.8 JDK (since the pack uses Java8 anyways)
- code cleanup and removal of unused artifacts
- fixed creative tab enblem
- fixed creative tab not matching when reading from config
- some config fixes by reteo
- bumped ae2 skystone to harvest level 7

v1.3.1-a1.2
- Fixed Tool Patcher breaking at Reika's tools instead of continuing with the next interation
- Casting Recipes with 1 ingredient now disable their vanilla furnace counterpart
- Fixed some configuration
- Fixed some lang stuff
- The Ingot Mold now has a in-world mechanic to recraft it to sand
- Skinnyfied the Crucible to one class
- Fixed Pickaxe tooltipping working with Pickaxes implementing a custom getHarvestLevel
- Fixed Reflector's log spam

v1.3.1-a1.1
- Fixed Alloy Recipe generation with oredictnames returning no item
- Introduced RoC Steel Tool Patcher (to make them real steel) until Reika comes up with a solution (configurable)
- Fixed double Recipes

v1.3.1-a1
- Added "Backyard Metal Casting" (tm) with config for alloying
- Generic metal blocks and ingots from config (with mod conditions)
- Recipes from config (with mod conditions, shapesless, shaped, oredict sensitive)
- Blockproperty overrides
- Generic Item creation from config (with mod conditions)
- Generic Block creation from config (with mod condtions)
- RoC Blast Furnace can cast alloys from metal casting config
- RoC Blast Furnace can make steel (integrated si.lowgradesteel'S functionality)
- Add and patch tool materials
- Tools are patched as well if materials change
- Tooltips for Pickaxes and Blocks indicating harvestlevel
- CdInAg Blocks catch neutrons at 60% rate
- A whole lot of other stuff not worth mentioning here
