package com.jeffpeng.si.core.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.jeffpeng.si.core.interfaces.ISIItem;
import com.jeffpeng.si.core.interfaces.ISIItemColor;

public class SICoreDye extends SICoreItem implements ISIItem, ISIItemColor {
	
	private int color;
	private int colorindex;
	
	@Override
	public void setColorIndex(int color) {
		this.colorindex = color;
		
	}

	@Override
	public int getColorIndex() {
		return colorindex;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setColor(int red, int green, int blue){
		this.color = red*65536 + green*256 + blue;
	}
	

	public int getColor() {
		return color;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack is, EntityPlayer ep, EntityLivingBase elb)
    {
        if (elb instanceof EntitySheep)
        {
            EntitySheep entitysheep = (EntitySheep)elb;
            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != colorindex)
            {
                entitysheep.setFleeceColor(colorindex);
                --is.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

	



}
