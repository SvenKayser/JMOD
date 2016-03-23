package com.jeffpeng.jmod.actions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.registry.PlayerData;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class AddStartingPlatform extends BasicAction {
	
	private int baseY;
	private int playerY;

	public AddStartingPlatform(JMODRepresentation owner, int baseY, int playerY) {
		super(owner);
		this.baseY = baseY;
		this.playerY = playerY;

	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		log.info("login reg handler");
		FMLCommonHandler.instance().bus().register(this);
		return true;
	}
	
	@SubscribeEvent
	public void on(PlayerLoggedInEvent event){
		PlayerData playerdata;
		EntityPlayer ep;
		World world = event.player.worldObj;
		double px;
		double py;
		double pz;
		
		if(!world.isRemote){
//			playerdata = PlayerData.getInstanceFor(event.player, owner);
//			log.info("player login");
//			if(!playerdata.getBoolean("knownPlayer")){
//				playerdata.set("knownPlayer", true);
//				ep = event.player;
//				px = ep.posX;
//				pz = ep.posZ;
//			} 
		}
		
		
		log.info("player login");
		
		
	}

}
