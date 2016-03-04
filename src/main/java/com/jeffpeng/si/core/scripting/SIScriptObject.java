package com.jeffpeng.si.core.scripting;

import java.util.ArrayList;
import java.util.List;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.scripting.mods.Chisel;
import com.jeffpeng.si.core.scripting.mods.RotaryCraft;
import com.jeffpeng.si.core.util.descriptors.*;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;

public class SIScriptObject {
	
	public RotaryCraft RotaryCraft = new RotaryCraft();
	public Chisel Chisel = new Chisel();
	
	public void load(String script){
		SIScript.instance().evalScript(script);
	}
	
	public  void log(String msg){
		FMLLog.info(msg);
	}
	
	
	public  ShapelessRecipeDescriptor addShapelessRecipe(String result, Object ingredients){
		
		ShapelessRecipeDescriptor newRecipe = new ShapelessRecipeDescriptor(result, SILib.convertArray(ingredients,String[].class) );
		SI.CONFIG.shapelessrecipes.add(newRecipe);
		return newRecipe;
	}
	
	public  ShapedRecipeDescriptor addShapedStandardRecipe(String result, String type, String mat){
		return new ShapedRecipeDescriptor(result, standardShape(type,mat));
	}
	
	public  ShapedRecipeDescriptor addShapedRecipe(String result, Object pattern){
		ShapedRecipeDescriptor newRecipe = new ShapedRecipeDescriptor(result, SILib.convertPattern(pattern));
		SI.CONFIG.shapedrecipes.add(newRecipe);
		return newRecipe;
	}
	
	public  void addSmeltingRecipe(String result, String ingredient){
		SI.CONFIG.smeltingrecipes.put(result,ingredient);
	}
	
	public  ItemDescriptor addItem(String name, String refClass,int stackSize,String creativeTab){
		return new ItemDescriptor(name,refClass,stackSize,creativeTab);
	}
	
	public  BlockDescriptor addBlock(String name, String refClass, Float hardness, Float blastresistance,
			String tool, int harvestlevel, String material, String tab){
		return new BlockDescriptor(name,refClass,hardness,blastresistance,tool,harvestlevel,material,tab);
	}
	
	public void testType(Object o){
		throw new RuntimeException(o.getClass().getName());
	}
	
	
	
	public void addMetalIngot(String name){
		SI.CONFIG.metalingots.add(name);
	}
	
	public void addMetalBlock(String name){
		SI.CONFIG.metalblocks.add(name);
	}
	
	public  ToolMaterialDescriptor addToolMaterial(String name,int harvestLevel, int durability, float efficiency, float damage, int enchantability, String repairmaterial){
		return new ToolMaterialDescriptor(name,harvestLevel,durability,efficiency,damage,enchantability,repairmaterial);
	}
	
	public  ArmorMaterialDescriptor addArmorMaterial(String name,int reductionbase, int helmetfactor, int chestfactor,int leggingsfactor,int bootsfactor,int enchantability,String repairmaterial){
		return new ArmorMaterialDescriptor(name,reductionbase,helmetfactor,chestfactor,leggingsfactor,bootsfactor,enchantability,repairmaterial);
	}
	
	public void purgeRecipe(String target){
		SI.CONFIG.purgerecipes.add(target);
	}
	
	public  ShapelessRecipeDescriptor purgeShapelessRecipe(String result, String[] ingredients){
		ShapelessRecipeDescriptor newRecipe = new ShapelessRecipeDescriptor(result,ingredients);
		SI.CONFIG.purgeshapelessrecipes.add(newRecipe);
		return newRecipe;
	}
	
	public void purgeSmeltingRecipe(String result){
		SI.CONFIG.purgesmeltingrecipes.add(result);
	}
	
	
	
	public void hideFromNEI(String target){
		if(Loader.isModLoaded("NotEnoughItems")) new NEIHidingDescriptor(target);
	}
	
	public  TooltipDescriptor addToolTip(String[] target, String[] lines){
		TooltipDescriptor newTooltip = new TooltipDescriptor(target,lines);
		SI.CONFIG.tooltips.add(newTooltip);
		return newTooltip;
	}
	
	public  ItemStackSubstituteDescriptor itemStackSubstitute(String source, String target){
		ItemStackSubstituteDescriptor newISD = new ItemStackSubstituteDescriptor(source,target);
		SI.CONFIG.itemstacksubstitutes.add(newISD);
		return newISD;
	}
	
	public  AlloyDescriptor addAlloy(String result, String one, String two, int amount){
		AlloyDescriptor newAD = new AlloyDescriptor(result,one,two,amount);
		SI.CONFIG.alloymap.add(newAD);
		return newAD;
	}
	
	public  AlloyDescriptor addAlloy(String result, String one, int amount){
		AlloyDescriptor newAD = new AlloyDescriptor(result,one,amount);
		SI.CONFIG.alloymap.add(newAD);
		return newAD;
	}
	
	public  OreDictionaryDescriptor addOreDict(String is, String entry){
		OreDictionaryDescriptor newODD = new OreDictionaryDescriptor(is,entry);
		SI.CONFIG.oredict.add(newODD);
		return newODD;
	}
	
	public  void removeOreDict(String is, String entry){
		SI.CONFIG.removeOredict.add(new OreDictionaryDescriptor(is, entry));
	}
	
	public void removeOreDict(String is){
		SI.CONFIG.removeOredict.add(new OreDictionaryDescriptor(is, null));
	}
	
	public  BlockPropertyDescriptor setBlockProperties(String item){
		BlockPropertyDescriptor newBPD = new BlockPropertyDescriptor();
		SI.CONFIG.blockproperties.put(item, newBPD);
		return newBPD;
	}
	
	public  void dependency(String modid, String name){
		SI.CONFIG.moddependencies.put(modid, name);
	}
	
	public  ColorDescriptor defineColor(String color, int red, int green, int blue){
		ColorDescriptor newColor = new ColorDescriptor(red,green,blue);
		SI.CONFIG.colors.put(color, newColor);
		return newColor;
	}
	
	public  void setHarvestLevelColor(int hlevel, String color){
		SI.CONFIG.harvestlevelcolors.put(hlevel, color);
	}
	
	public  void addChestLoot(String is,int min,int max,int weight){
		SI.CONFIG.addChestLoot.put(new ChestLootDescriptor(is,min,max,weight), null);
	}
	
	public void addChestLoot(String is,int min,int max,int weight,String[] targets){
		SI.CONFIG.addChestLoot.put(new ChestLootDescriptor(is,min,max,weight), targets);
	}
	
	public void removeChestLoot(String is){
		SI.CONFIG.removeChestLoot.put(is, null);
	}
	
	public void removeChestLoot(String is, String[] targets){
		SI.CONFIG.removeChestLoot.put(is, targets);
	}
	
	public BlockDropDescriptor addBlockDrop(String block, String itemstack, Integer chance, boolean stopeventchain, boolean playeronly){
		BlockDropDescriptor newBDD = new BlockDropDescriptor(block, itemstack,chance, stopeventchain,playeronly);
		SI.CONFIG.blockDrops.add(newBDD);
		return newBDD;
	}
	
	
	public  FoodDataDescriptor FoodData(int hunger, float saturation,boolean wolffood,boolean alwaysEdible){
		return new FoodDataDescriptor(hunger,saturation,wolffood,alwaysEdible);
	}
	public  ToolDataDescriptor ToolData(String toolmat){
		return new ToolDataDescriptor(toolmat);
	}
	
	public  ArmorDataDescriptor ArmorData(String mat, String type){
		return new ArmorDataDescriptor(mat,type);
	}
	
	public  List<String[]> standardShape(String shape, String mat){
		List<String[]> pattern = null;
		if(shape.equals("pickaxe")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{mat,mat,mat});
			pattern.add(new String[]{null,"stickWood",null});
			pattern.add(new String[]{null,"stickWood",null});
		} else
			
		if(shape.equals("axe")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{null,mat,mat});
			pattern.add(new String[]{null,"stickWood",mat});
			pattern.add(new String[]{null,"stickWood",null});
		} else
			
		if(shape.equals("shovel")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{null,mat,null});
			pattern.add(new String[]{null,"stickWood",null});
			pattern.add(new String[]{null,"stickWood",null});
		} else
			
		if(shape.equals("sword")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{null,mat,null});
			pattern.add(new String[]{null,mat,null});
			pattern.add(new String[]{null,"stickWood",null});
		} else
		
		if(shape.equals("hoe")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{null,mat,mat});
			pattern.add(new String[]{null,"stickWood",null});
			pattern.add(new String[]{null,"stickWood",null});
		} else
			
		if(shape.equals("helmet")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{mat,mat,mat});
			pattern.add(new String[]{mat,null,mat});
			pattern.add(new String[]{null,null,null});
		} else
			
		if(shape.equals("chest") || shape.equals("chestplate")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{mat,null,mat});
			pattern.add(new String[]{mat,mat,mat});
			pattern.add(new String[]{mat,mat,mat});
		} else
			
		if(shape.equals("leggings")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{mat,mat,mat});
			pattern.add(new String[]{mat,null,mat});
			pattern.add(new String[]{mat,null,mat});
		} else
				
		if(shape.equals("boots")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{null,null,null});
			pattern.add(new String[]{mat,null,mat});
			pattern.add(new String[]{mat,null,mat});
		} else
			
		if(shape.equals("block")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{mat,mat,mat});
			pattern.add(new String[]{mat,mat,mat});
			pattern.add(new String[]{mat,mat,mat});
		} 
		return pattern;
	}
	
	
	
	public  boolean isModLoaded(String modid){
		return Loader.isModLoaded(modid);
	}
}
