package com.jeffpeng.jmod.worldgeneration;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.util.actions.AddOreGeneration;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class ConfigurableOreGenerator extends OwnedObject implements IWorldGenerator {

	WorldGenerator worldGen;
	AddOreGeneration action;
	int spread;

	public ConfigurableOreGenerator(AddOreGeneration action) {
		super(action.getOwner());
		this.action = action;
		this.config = owner.getConfig();
		
		
		String[] genBlockNameSplit = action.blocktogenerate.split(":");
		String genModId = genBlockNameSplit[0];
		String genBlockName = genBlockNameSplit[1];
		int meta = 0;
		if(genBlockNameSplit.length > 2) meta = Integer.parseInt(genBlockNameSplit[2]);

		String[] repBlockNameSplit = action.blocktoreplace.split(":");
		String repModId = repBlockNameSplit[0];
		String repBlockName = repBlockNameSplit[1];

		Block blockToGenerate = GameRegistry.findBlock(genModId, genBlockName);
		Block blockToReplace = GameRegistry.findBlock(repModId, repBlockName);
		this.worldGen = new WorldGenMinable(blockToGenerate,meta,action.veinsize,blockToReplace);
		
		
		
	}
	

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
//		Long nano = System.nanoTime();
		if (world.provider.dimensionId == action.dimension) new Thread(new Runnable(){
			public void run(){
				WorldGenerator iWorldGen = worldGen;
				int ispread = action.spread;
				int iendy = action.endy;
				int istarty = action.starty;
				Long internalSeed = random.nextLong() ^ action.seedoffset;
		        Random internalRandom = new Random(internalSeed);
		        int mulX = chunkX * ispread;
		        int mulZ = chunkZ * ispread;
				for (int r = 0; r < action.chancesperchunk; r++) {
					int spawnX = mulX + internalRandom.nextInt(ispread);
					int spawnZ = mulZ + internalRandom.nextInt(ispread);
					int spawnY = internalRandom.nextInt(iendy - istarty) + istarty;
					iWorldGen.generate(world, internalRandom, spawnX, spawnY, spawnZ);
				}
//				log.info("gen");
			}
		}).start();
//		nano = System.nanoTime() - nano;
//		log.info(action.blocktogenerate + " " + nano);
	}
}
