package com.jeffpeng.jmod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;

/* Based on mcp.mobius.waila.utils.ModIdentification by ProfMobius released under the Apache License 2.0
 * 
 */

public class ModId {

    public static HashMap<String, String> modSource_Name = new HashMap<String, String>();
    public static HashMap<String, String> modSource_ID = new HashMap<String, String>();


    public static void init() {
        for (ModContainer mod : Loader.instance().getModList()) {
            modSource_Name.put(mod.getSource().getName(), mod.getName());
            modSource_ID.put(mod.getSource().getName(), mod.getModId());
        }

        //TODO : Update this to match new version (1.7.2)
        modSource_Name.put("1.6.2.jar", "Minecraft");
        modSource_Name.put("1.6.3.jar", "Minecraft");
        modSource_Name.put("1.6.4.jar", "Minecraft");
        modSource_Name.put("1.7.2.jar", "Minecraft");
        modSource_Name.put("1.8.0.jar", "Minecraft");
        modSource_Name.put("Forge", "Minecraft");
        modSource_ID.put("1.6.2.jar", "Minecraft");
        modSource_ID.put("1.6.3.jar", "Minecraft");
        modSource_ID.put("1.6.4.jar", "Minecraft");
        modSource_ID.put("1.7.2.jar", "Minecraft");
        modSource_ID.put("1.8.0.jar", "Minecraft");
        modSource_ID.put("1.8.8.jar", "Minecraft");
        modSource_ID.put("Forge", "Minecraft");

    }

    public static String nameFromObject(Object obj) {
        String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

        try {
            objPath = URLDecoder.decode(objPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String modName = "<Unknown>";
        for (String s : modSource_Name.keySet())
            if (objPath.contains(s)) {
                modName = modSource_Name.get(s);
                break;
            }

        if (modName.equals("Minecraft Coder Pack"))
            modName = "Minecraft";

        return modName;
    }

    public static String nameFromStack(ItemStack stack) {
        try {
        	GameData.getItemRegistry().getNameForObject(stack.getItem());
            ResourceLocation resource = new ResourceLocation(GameData.getItemRegistry().getNameForObject(stack.getItem())); 
            ModContainer mod = ModId.findModContainer(resource.getResourceDomain());
            String modName = mod == null ? "Minecraft" : mod.getName();
            return modName;
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static ModContainer findModContainer(String modID) {
        for (ModContainer mc : Loader.instance().getModList())
            if (modID.toLowerCase(Locale.US).equals(mc.getModId().toLowerCase(Locale.US)))
                return mc;
        return null;
    }
}