package com.jeffpeng.jmod.scripting;

import java.util.ArrayList;
import java.util.List;

import com.jeffpeng.jmod.JMODLoader;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.actions.*;
import com.jeffpeng.jmod.descriptors.*;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.scripting.mods.*;
import com.jeffpeng.jmod.validator.Validator;

public class ScriptObject extends OwnedObject {
	
	private JScript jscriptInstance;
	
	public ScriptObject(JScript jscriptinstance){
		super(jscriptinstance.getMod());
		jscriptInstance = jscriptinstance;
	}
	
	public Settings Settings = new Settings(owner);
	public Global Global = new Global(owner);
	public RotaryCraft RotaryCraft = new RotaryCraft(owner);
	public Chisel Chisel = new Chisel(owner);
	public Applecore Applecore = new Applecore(owner);
	public Sync Sync = new Sync(owner);
	public ExNihilo ExNihilo = new ExNihilo(owner);
	public GardenStuff GardenStuff = new GardenStuff(owner);
	
	public void loadjs(String script){
		jscriptInstance.evalScript(script);
	}
	
	public  void log(String msg){
		log.info(msg);
	}
	
	public JMODRepresentation JModScript() {
		return owner;
	}
	
	public void addShapelessRecipe(String result, Object ingredients){
		new AddShapelessRecipe(owner,result, Lib.convertArray(ingredients,String[].class) );
	}
	
	public  AddShapedRecipe addShapedStandardRecipe(String result, String type, String mat){
		return new AddShapedRecipe(owner,result, standardShape(type,mat));
	}
	
	public  AddShapedRecipe addShapedRecipe(String result, Object pattern){
		return new AddShapedRecipe(owner,result, Lib.convertPattern(pattern));
	}
	
	public  void addSmeltingRecipe(String result, String ingredient){
		new AddSmeltingRecipe(owner,result,ingredient);
	}
	
	public  AddItem addItem(String name, String refClass,int stackSize,String creativeTab){
		return new AddItem(owner, name,refClass,stackSize,creativeTab);
	}
	
	public  AddBlock addBlock(String name, String refClass, Float hardness, Float blastresistance,
			String tool, int harvestlevel, String material, String tab){
		return new AddBlock(owner,name,refClass,hardness,blastresistance,tool,harvestlevel,material,tab);
	}
	
	public void testType(Object o){
		throw new RuntimeException(o.getClass().getName());
	}
	
	
	
	public void addMetalIngot(String name){
		config.metalingots.add(name);
	}
	
	public void addMetalBlock(String name){
		config.metalblocks.add(name);
	}
	
	public  AddToolMaterial addToolMaterial(String name,int harvestLevel, int durability, float efficiency, float damage, int enchantability, String repairmaterial){
		return new AddToolMaterial(owner,name,harvestLevel,durability,efficiency,damage,enchantability,repairmaterial);
	}
	
	public  AddArmorMaterial addArmorMaterial(String name,int reductionbase, int helmetfactor, int chestfactor,int leggingsfactor,int bootsfactor,int enchantability,String repairmaterial){
		return new AddArmorMaterial(owner,name,reductionbase,helmetfactor,chestfactor,leggingsfactor,bootsfactor,enchantability,repairmaterial);
	}
	
	public void removeRecipes(String target){
		new RemoveRecipe(owner,target);
	}
	
	public void removeSmeltingRecipes(String result){
		new RemoveSmeltingRecipe(owner,result);
	}
	
	public void hideFromNEI(String target){
		if(owner.testForMod("NotEnoughItems")) new HideNEIItem(owner,target);
	}
	
	public  TooltipDescriptor addToolTip(String[] target, String[] lines){
		TooltipDescriptor newTooltip = new TooltipDescriptor(target,lines);
		config.tooltips.add(newTooltip);
		return newTooltip;
	}
	
	public  ItemStackSubstituteDescriptor itemStackSubstitute(String source, String target){
		ItemStackSubstituteDescriptor newISD = new ItemStackSubstituteDescriptor(source,target);
		config.itemstacksubstitutes.add(newISD);
		return newISD;
	}
	
	public  AlloyDescriptor addAlloy(String result, String one, String two, int amount){
		AlloyDescriptor newAD = new AlloyDescriptor(result,one,two,amount);
		config.alloymap.add(newAD);
		return newAD;
	}
	
	public  AlloyDescriptor addAlloy(String result, String one, int amount){
		AlloyDescriptor newAD = new AlloyDescriptor(result,one,amount);
		config.alloymap.add(newAD);
		return newAD;
	}
	
	public void addOreDict(String is, String entry){
		new AddOreDictionaryEntry(owner,is,entry);
	}
	
	public  void removeOreDict(String is, String entry){
		new RemoveOreDictionaryEntry(owner,is, entry);
	}
	
	public void removeOreDict(String is){
		new RemoveOreDictionaryEntry(owner,is, null);
	}
	
	public  SetBlockProperties setBlockProperties(String item){
		return new SetBlockProperties(owner, item);
	}
	
	public  void dependency(String modid, String name){
		config.moddependencies.put(modid, name);
	}
	
	public  ColorDescriptor defineColor(String color, int red, int green, int blue){
		ColorDescriptor newColor = new ColorDescriptor(red,green,blue);
		config.colors.put(color, newColor);
		return newColor;
	}
	
	public void addChestLoot(String is,int min,int max,int weight){
		new AddChestLoot(owner,is,min,max,weight, null);
	}
	
	public void addChestLoot(String is,int min,int max,int weight,String[] targets){
		new AddChestLoot(owner,is,min,max,weight, targets);
	}
	
	public void removeChestLoot(String is){
		new RemoveChestLoot(owner,is,null);
	}
	
	public void removeChestLoot(String is, String[] targets){
		new RemoveChestLoot(owner,is,targets);
	}
	
	public AddBlockDrop addBlockDrop(String block, String itemstack, Integer chance, boolean stopeventchain, boolean playeronly){
		AddBlockDrop newBDD = new AddBlockDrop(owner,block, itemstack,chance, stopeventchain,playeronly);
		config.blockDrops.add(newBDD);
		return newBDD;
	}
	
	
	public  FoodDataDescriptor FoodData(int hunger, float saturation,boolean wolffood,boolean alwaysEdible){
		return new FoodDataDescriptor(owner,hunger,saturation,wolffood,alwaysEdible);
	}
	public  ToolDataDescriptor ToolData(String toolmat){
		return new ToolDataDescriptor(toolmat);
	}
	
	public  ArmorDataDescriptor ArmorData(String mat, String type){
		return new ArmorDataDescriptor(mat,type);
	}
	
	public void addCreativeTab(String tabId, String tabName, String itemString){
		new AddCreativeTab(owner, tabId, tabName, itemString);
		
	}
	
	public AddOreGeneration addOreGeneration(){
		return new AddOreGeneration(owner);
	}
	
	public AddStartingPlatform addStartingPlatform(int baseY, int playerY){
		return new AddStartingPlatform(owner,baseY,playerY);
	}
	
	public AddFluid addFluid(String fluidname){
		return new AddFluid(owner,fluidname);
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
			
		if(shape.equals("shears")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{null, null, null});
			pattern.add(new String[]{null, mat,  null});
			pattern.add(new String[]{mat,  null, null});
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
		
		if(shape.equals("smallblock")){
			pattern = new ArrayList<>();
			pattern.add(new String[]{mat,mat,null});
			pattern.add(new String[]{mat,mat,null});
			pattern.add(new String[]{null,null,null});
		}
		
		return pattern;
	}
	
	
	
	public  boolean isModLoaded(String modid){
		return Validator.isValidator || JMODLoader.isModLoaded(modid);
	}
	
	public String getModId(){
		return owner.getModId();
	}
	
	public int celcius(float c){	return Math.round(c + 273.15F);				}
	public int fahrenheit(float f){	return Math.round((f + 459.67F) * (5/9));	}
	
	
}
