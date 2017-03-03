### The Javascript MOD Loader (JMOD)

JMOD let's you define simple mods without the hassle of writing setting up a Java development environment and compiling your mods each time you want to try a change. While you don't need to learn Java do write mods with JMOD, you need some basic understanding of JavaScript, however the emphasis definitely is on "basic".

JMOD excels at building "glue" mods for mod packs, which was its original purpose. It provides you with a multitude of tools to modify existing content in the game, like tool materials, block hardness, recipes or random chest loot. It also allows you to implement new items and blocks in an instant, most of the time with just one single line of code. All the content you add will belong to your own mod, rather than to JMOD or some other mod (like CustomItems, for example). So if you want to go ahead and add a few new tools to the game, and some new metals, along with ingots and ores, including world generation - JMOD is for you.

However, if you want build more complicated mods, such that implement machines, custom overlays or altogether complex things that require to alter the game at a deeper level, JMOD is not what you want. While most of this probably is possible with JMOD, it would require to surpass JMODs easy-access-layer and mess with the underlying Java API of MinecraftForge and eventually MineCraft itself, and at this point JMOD's ease-of-use advantages are completely nullified, while the performance disadvantage of JavaScript over Java would still apply. If that is what you desire coding mods in Java is the best way to go. That being said, JMOD will at a later point allow the use of "plugins" which are basically compiled Java classes you can acquire and use with your mods.

If you desire a feature for JMOD please let me know and I will see if it is possible to implement.

A fully fledged reference will be available as a wiki shortly. 


