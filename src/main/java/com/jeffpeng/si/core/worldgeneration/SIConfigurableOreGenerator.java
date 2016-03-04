package com.jeffpeng.si.core.worldgeneration;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.util.descriptors.OreGenerationDescriptor;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class SIConfigurableOreGenerator implements IWorldGenerator {

	private subGen[] subGenerators;

	private class subGen {
		WorldGenerator worldGen;
		public String targetname;
		int spread;
		int minY;
		int maxY;
		int dim;
		int repeat;

		public subGen(String targetname, WorldGenMinable worldGen, int minY, int maxY, int repeat, int spread, int dim) {
			this.worldGen = worldGen;
			this.spread = spread;
			this.minY = minY;
			this.maxY = maxY;
			this.dim = dim;
			this.repeat = repeat;
		}

		public void generate(Random random, int chunkX, int chunkZ, World world) {
			if (world.provider.dimensionId == this.dim) {
				for (int r = 0; r < this.repeat; r++) {
					int spawnX = chunkX * this.spread + random.nextInt(this.spread);
					int spawnZ = chunkZ * this.spread + random.nextInt(this.spread);
					int spawnY = random.nextInt(this.maxY - this.minY) + this.minY;
					worldGen.generate(world, random, spawnX, spawnY, spawnZ);
				}
			}
		}

	}

	public SIConfigurableOreGenerator() {

		int generators = 0;
		subGenerators = new subGen[SI.CONFIG.oregeneration.size()];
		for (OreGenerationDescriptor orgdesc : SI.CONFIG.oregeneration) {
			String[] genBlockNameSplit = orgdesc.blocktogenerate.split(":");
			String genModId = genBlockNameSplit[0];
			String genBlockName = genBlockNameSplit[1];

			String[] repBlockNameSplit = orgdesc.blocktoreplace.split(":");
			String repModId = repBlockNameSplit[0];
			String repBlockName = repBlockNameSplit[1];

			Block blockToGenerate = GameRegistry.findBlock(genModId, genBlockName);
			Block blockToReplace = GameRegistry.findBlock(repModId, repBlockName);

			subGenerators[generators++] = new subGen(orgdesc.blocktogenerate, new WorldGenMinable(blockToGenerate, orgdesc.veinsize, blockToReplace), orgdesc.starty, orgdesc.endy,
					orgdesc.chancesperchunk, orgdesc.spread, orgdesc.dimension);
			FMLLog.info("[si.core] [Worldgen] Registered generation for " + orgdesc.blocktogenerate + " in dim" + orgdesc.dimension + " as generator " + generators);
		}
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (int c = 0; c < subGenerators.length; c++) {
			try{
				this.subGenerators[c].generate(random, chunkX, chunkZ, world);
			} catch(Exception e) {
				if(this.subGenerators[c] == null){
					FMLLog.warning("[si.core] [Worldgen] Something went wrong generating terrain, and the generator nr. " + c + "magically does not exist");
				} else {
					FMLLog.warning("[si.core] [Worldgen] Something went wrong generating terrain for " + this.subGenerators[c].targetname);
				}
				
				e.printStackTrace();
			}
		}
	}

}
