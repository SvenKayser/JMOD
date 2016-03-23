Changelog

alpha1.0.4
- implemented Fluids + Fluid API
- implemented interface for sifting results (ExNihilo)
- implemented Centrifuge API (RotaryCraft)
- tons of changes under the hood

alpha1.0.3
- the JMOD jar is now executable and provides a simple .jmod validator
- Improved Nashorn's binding of the ScriptingObject
- Hooked scripts earlier into FML's loading process
- Improved loading performance again (to the point where JMOD is actually waiting on minecraft, instead of the other way around)
- The Loader now performs a rudimentary sanity check to prevent stupid jmods from doing stupid things
- Fixed AddSmeltingRecipe being invalid by default
- Fixed not executing RemoveSmeltingRecipes
- Fixed eval error line numbers not working (again...)

alpha1.0.2
- JMOD is now a core mod only
- Improved loading performance
- Added sanity check for StringListRecipes
- Fixed crash when encountering missing metal blocks/ingots
- Fixed creative tabs not ready on time
- Fixed Action priority
- Fixed OreGen not running concurrently


alpha1.0.1

- First Version, rewrite from the base of the si.core mod


