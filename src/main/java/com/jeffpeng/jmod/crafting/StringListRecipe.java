package com.jeffpeng.jmod.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class StringListRecipe extends OwnedObject implements IRecipe {
	
	public static List<StringListRecipe> recipeList = new ArrayList<>();
	
	private boolean mirroredYAxis = true;
	private boolean mirroredXAxis = true;
	private boolean valid;
	//private String resultString;
	private ItemStack result;
	private String resultname;
	private int width = 0;
	private int height = 0;
	private int size = 0;
	private List<String[]> lines = new ArrayList<>(); 
	private List<String[]> originalShape;
	private Map<String,Boolean> containsIngredientCache = new HashMap<>(); 
	
	public boolean isValid(){return this.valid;}
	
	private void processResult(String result){
		resultname = result;
		Object is = lib.stringToItemStack(result);
		if(is instanceof ItemStack){
			this.result = (ItemStack)is;
			valid = true;
		} else if(is instanceof String){
			ItemStack firstEntry = lib.getFirstOreDictMatch((String) is);
			if(firstEntry != null){
				this.result = firstEntry;
				valid = true;
			} else valid = false;
		}
		
		if(valid == false) log.warn("Cannot resolve " + result); else log.info ("Registered StringListRecipe for " + result);
		
	}
	
	private boolean lineEmpty(String[] line){
		boolean empty = true;
		int c = 0;
		while(empty && c < line.length)	empty = (line[c++] == null);
		return empty;
	}
	
	private boolean rowEmpty(List<String[]> rawShape,int col){
		boolean empty = true;
		for(String[] line : rawShape){
			if(line.length >= col)	empty &= (line[col] == null);
		}
		return empty;
	}
	
	private void cropAndTransport(List<String[]> rawShape){
		
		int startx = 0;
		int endx = 2;
		
		while(lineEmpty(rawShape.get(0))) rawShape.remove(0);
		while(lineEmpty(rawShape.get(rawShape.size()-1))) rawShape.remove(rawShape.size()-1);
		this.height = rawShape.size();
		if(this.getHeight()<1) {this.valid=false;return;}
		
		while(rowEmpty(rawShape,startx)) startx++;
		while(rowEmpty(rawShape,endx)) endx--;
		this.width = endx-startx+1;
		if(this.getWidth()<1) {this.valid=false;return;}
		
		this.size = this.getWidth() * this.getHeight();
		
		
		
		for(String[] line : rawShape){
			String[] newLine = new String[this.getWidth()];
			for(int c = 0; c < this.getWidth();c++) newLine[c] = line[c+startx];
			lines.add(newLine);
		}
	}
	
	private boolean sanityCheck(){
		for(String[] line : lines){
			for(String element : line){
				if(element != null){
					Object is = lib.stringToItemStack(element);
					if(is instanceof String){
						if( (!OreDictionary.doesOreNameExist((String)is)) || OreDictionary.getOres((String)is, false).size() < 1){
								log.warn("StringListRecipe for " + resultname + " failed sanity check because " + (String)is + " is neither a valid ItemStack, nor is it an OreDictionary entry that contains something.");
								return false;
						}
					}
				}
			}
		}
		
		
		return true;
	}
	
	
	public void setMirrorHorizontally(boolean set){
		this.mirroredYAxis = set;
	}
	
	public void setMirrorVertically(boolean set){
		this.mirroredXAxis = set;
	}
	
	
	
	public StringListRecipe(JMODRepresentation jmod, String result, List<String[]> rawShape){
		super(jmod);
		this.originalShape = new ArrayList<String[]>(rawShape);
		//this.resultString = result;
		processResult(result);
		if(valid){
			cropAndTransport(rawShape);
		}
		
		valid = sanityCheck();

		if(valid)
		{
			StringListRecipe.recipeList.add(this);
		} 
	}
	
	private boolean matchAgainstOffset(int w, int h,InventoryCrafting inv,boolean mirrorx, boolean mirrory){
		int invheight = inv.getSizeInventory()/inv.inventoryWidth;
		for(int x = 0; x < inv.inventoryWidth; x++){
			for(int y = 0; y < invheight; y++){
				int tx = (!mirrorx) ? x-w : inv.inventoryWidth -1 -w - x;
				int ty = (!mirrory) ? y-h : invheight -1 -h - y;
				if(ty < 0 || tx < 0 || tx >= this.width || ty >= this.height){
					if(inv.getStackInRowAndColumn(x, y) != null) return false;
				} else {
					if(!lib.matchItemStacksOreDict(inv.getStackInRowAndColumn(x, y), lines.get(ty)[tx])) return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		
		boolean empty = true;
		int x = 0;
		int size = inv.getSizeInventory();
		while(empty && x < size) empty &= inv.getStackInSlot(x++) == null;
		if(empty) return false;
		if(!valid) return false;
		if(inv.getSizeInventory() < this.size) return false;
		if(inv.inventoryWidth < this.getWidth()) return false;
		for (int w = 0; w <= inv.inventoryWidth - this.getWidth(); w++)
        {
            for (int h = 0; h <= (inv.getSizeInventory()/inv.inventoryWidth) - this.getHeight(); h++)
            {
            	if(matchAgainstOffset(w,h,inv,false,false)) return true;
            	if(this.mirroredXAxis){
            		if(matchAgainstOffset(w,h,inv,true,false)) return true;
            		if(this.mirroredYAxis) if(matchAgainstOffset(w,h,inv,true,true)) return true;
            	}
            	if(this.mirroredYAxis) if(matchAgainstOffset(w,h,inv,false,true)) return true;
            }
        }
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		if(!valid) return null; else return this.result.copy();
	}

	@Override
	public int getRecipeSize() {
		// TODO Auto-generated method stub
		return this.size;
	}

	@Override
	public ItemStack getRecipeOutput() {
		if(!valid) return null; else return this.result;
	}
	
	public ItemStack getRecipeOutputProxy(){
		return getRecipeOutput();
	}
	
	public boolean fits(int x, int y){
		return this.getWidth() <= x && this.getHeight() <= y;
	}
	
	public List<String[]> getOriginalShape(){
		return originalShape;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Object[] getIngredientArray(){
		Object[] array = new Object[this.size];
		for(int y = 0; y < this.height;y++){
			for(int x = 0; x < this.width;x++){
				Object ingredient = lib.stringToItemStack(lines.get(y)[x]);
				if(ingredient == null || ingredient instanceof ItemStack)	array[y*this.width+x] = ingredient;
				if(ingredient instanceof String) array[y*this.width+x] = OreDictionary.getOres((String) ingredient);
			}
		}
		return array; 
	}
	
	public boolean containsIngredient(ItemStack is){
		if(containsIngredientCache.containsKey(is.toString())) return containsIngredientCache.get(is.toString());
		for(String[] line : lines){
			for(String element : line){
				if(element != null && lib.matchItemStacksOreDict(is, element)){
					containsIngredientCache.put(is.toString(), true);
					return true;
				}
			}
		}
		containsIngredientCache.put(is.toString(), false);
		return false;
	}
}
