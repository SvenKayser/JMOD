# The Javascript MOD Loader (JMOD) for Minecraft 1.7.10 / MinecraftForge 10.13.4.1558

JMOD let's you define simple mods without the hassle of writing setting up a Java development environment and compiling your mods each time you want to try a change. While you don't need to learn Java do write mods with JMOD, you need some basic understanding of JavaScript, however the emphasis definitely is on "basic".

JMOD excels at building "glue" mods for mod packs, which was its original purpose. It provides you with a multitude of tools to modify existing content in the game, like tool materials, block hardness, recipes or random chest loot. It also allows you to implement new items and blocks in an instant, most of the time with just one single line of code. All the content you add will belong to your own mod, rather than to JMOD or some other mod (like CustomItems, for example). So if you want to go ahead and add a few new tools to the game, and some new metals, along with ingots and ores, including world generation - JMOD is for you.

JMOD also allows the use of plugins, which are easy to build native Java implementations of functionalities that would otherwise be used in mods. Bound with the right scripting methods you can make use of rather complex things implemented in native Java while calling them from JavaScript. All of JMODs bindings to other mods are implemented this way. The "Vanilla Ingot Works" Plugin however is an example of a standalone implementation of new content.

If you desire a feature for JMOD please let me know and I will see if it is possible to implement.

Reteo invested a lot of time [documenting JMOD](https://github.com/SvenKayser/JMOD/blob/indev/Documentation/JMOD.pdf). Please understand that do tue the quite frequent code fluctuations and no frozen API this documentation might not always reflect the latest version. This is not reteos fault - but mine ;-)

If you interested in maintaining a plugin or adding a plugin to support your mod please let me know.

## UberCredits

- The amazing reteo for not only taking care of the documentation, but also for being the driving creative force behind many things JMOD can and will do
- nmarshall23 for his valuable contributions and a couple of nice mod integrations (now known as "plugins")

### Credits
- Reika for sharing his mod sources. I learned a lot from them
- The MinecraftForge guys for their amazing ModLoader/Modding API that's the groundwork for countless mods, so it is for JMOD
- The entire [Survival Industry](https://github.com/reteo/Survival-Industry) crew for a couple of man-years of testing SurvivalIndustry - and with it JMOD and it's precursors
- Generally everybody sharing source and giving advice related to modding

## FAQ

#### I found a bug. What now?
Please open a new [issue](https://github.com/SvenKayser/JMOD/issues) and describe the bug as verbose as possible. Don't forget to mention the version you are using. There are multiple supported versions of JMOD as mentioned in LTS.md. Bugs will be fixed for versions still supported if possible.

#### I want to contribute. How can I do that?
Fork the repo and submit pull requests if you deem them worth it. If the code is good and useful, I'll accept it. I'd also gladly give away maintainership for some of the plugins.

#### Can I use JMOD on my private/public server/modpack?
TL;DR: YES.

It's MIT licensed for a reason - so please don't bother to ask permission. I just would not like you to exploit the might of JMOD to create pay-to-win or pay-for-content servers. But I can not and will not forbid you to do that - I'd just dislike it. What I would like, however, is a note of what you are using it for and opensourcing your JMODs if you deem them useful.

#### Can I run just JMOD without [MinecraftForge](https://github.com/MinecraftForge) if I don't need any other mods?
Nope, you cannot.

JMOD still relies on a good portion of forge and especially The Forge Mod Loader. It's lightweight enough not to worry.

#### Does JMOD rely on a certain architecture? What are the requirements?
TL;DR: Something that beeps with Java 8

JMOD runs on every system, and as far as I know at least the server on every architecture (having it tested with x86-64, ARMv7l, and SPARC UA2007) that runs Java 8. JMOD is pretty lightweight, so requirements shouldn't be higher than what you'd usually expect from modded Minecraft. Making extensive use of repeatedly executed Javascript code, however, can be hard on the CPU, so it depends on the JMODs you use.

#### Why there is no JMOD for Minecraft 1.8/1.9/1.10/1.11 ?
TL;DR: Because [RotaryCraff](https://github.com/ReikaKalseki/RotaryCraft) is still 1.7.10 - so is [Survival Industry](https://github.com/reteo/Survival-Industry)

The reason is rather simple from my point of view: JMODs main purpose was (and probably still is) to support and enable the ModPack [Survival Industry](https://github.com/reteo/Survival-Industry). Survival Industry relies heavily on ReikaKalseki's [RotaryCraff](https://github.com/ReikaKalseki/RotaryCraft), which still is and maybe will remain exclusively for 1.7.10. However, as most of the community has moved on to 1.10+, I plan to complete a few key features (namely the EventAPI and configurable TileEntities/BlockEntities) and then move JMOD to the latest Minecraft version as well, keeping the 1.7.10 version under maintenance, but feature frozen. So: Eventually it will happen.

#### Why do I need Java 8 for this? My Java 6 JRE is so much better!
TL;DR: Because JavaScript

Sadly Nashorn, the JavaScript engine JMOD is built upon, is a Java 8 feature. While there are cumbersome and buggy ways to get Nashorn into Java 7, there is no way whatsoever to run in in Java 6. That's why I see little reason to maintain compatibility with anything below 8. Also: especially nmarshall23 likes to use fancy stuff that's new to Java 8.

#### This is more like MineTweakerPro. Why cannot I write "real" mods with this?
For once: An important part to address this issue is still under development: The EventAPI, which will allow writing real event driven JavaScript code much like you are used from your browser or node.js projects. But, honestly: It doesn't make much sense to reflect the entire Minecraft API into JavaScript scope. At certain points you eventually will hit a wall, and if it's just a performance wall, at which you will go with either a JMOD Plugin or a real mod altogether. If you just want to add a few blocks, tools made of pork, and spawn funny flowers in the overworld: JMOD can do that already, and not too long ago that was considered enough for a mod. Don't expect JMOD to allow you to rewrite BuildCraft in JavaScript. It won't happen.

#### Can I revert and change scripts while running the game... like I can with MineTweaker?
TL;DR: No.

I actually planned to implement this for a long time, but eventually gave up on the endeavour, and here is why: First of all JMOD does all sorts of things that are not reversible without breaking the game - like adding blocks and item. There is just no way to code this without severly breaking the way minecraft works. This would leave me with a specific subsets of actions I can revert - and some I cannot. Also with JMOD becoming sensitive to game events soon there is no longer a deterministic script execution guaranteed.

#### I personally hate JavaScript - can't you write this for Lua?
I personally hate Lua, so no.
