package com.jeffpeng.jmod.registry;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerData {
	
	private static Map<String,PlayerData> playerdatacache = new HashMap<>();
	private Map<String,String> values = new HashMap<>();
	@SuppressWarnings("unused")
	private static SimpleNetworkWrapper snw = NetworkRegistry.INSTANCE.newSimpleChannel("JMODPlayerData");
	
	public static void purge(){
		playerdatacache.clear();
	}

	public static PlayerData getInstanceFor(EntityPlayer player, JMODRepresentation jmod){
		String key = player.getUniqueID()+"@"+jmod.getModId();
		
		if(playerdatacache.containsKey(key)){
			return playerdatacache.get(key); 
		} else {
			PlayerData newdata = new PlayerData(player,jmod);
			playerdatacache.put(key, newdata);
			return newdata;
		}
	}
	
	public PlayerData(EntityPlayer player, JMODRepresentation jmod){
		
	}
	
	public void set(String key,Boolean value){		setImpl(key,value.toString());	}
	public void set(String key,Integer value){		setImpl(key,value.toString());	}
	public void set(String key,Float value){		setImpl(key,value.toString());	}
	public void set(String key,String value){		setImpl(key,value);	}
	
	private void setImpl(String key, String value){
		if(JMOD.isServer()){
			values.put(key, value);
		} else {
			
		}
	}
	
	public int getInteger(String key){		return Integer.parseInt(getImpl(key));	}
	public boolean getBoolean(String key){	return getImpl(key).equals("true");	}
	public float getFloat(String key){		return Float.parseFloat(getImpl(key));	}
	public String getString(String key){	return getImpl(key);	}
	
	private String getImpl(String key){
		String value = values.get(key);
		if(value == null){
			
		}
		
		return value;
		
	}
	
	public static class ValueToClient implements IMessage {
		
		public ValueToClient(JMODRepresentation jmod, String value){
			
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void toBytes(ByteBuf buf) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public static class ValueResponse implements IMessageHandler {

		@Override
		public IMessage onMessage(IMessage message, MessageContext ctx) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static class MessageBody{
		public String from;
		public String to;
		public String mod;
		public String valuekey;
		public String value;
		public String type;
		
		public MessageBody(String from, String to, String mod, String valuekey, String value, String type){
			this.from = from;
			this.to = to;
			this.mod = mod;
			this.valuekey = valuekey;
			this.value = value;
			this.type = type;
		}
	}
	
	

}
