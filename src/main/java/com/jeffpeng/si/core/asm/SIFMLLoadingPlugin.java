package com.jeffpeng.si.core.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;


@MCVersion(value="1.7.10")
@TransformerExclusions(value={"com.jeffpeng.si.core.asm"})
public class SIFMLLoadingPlugin implements IFMLLoadingPlugin {
	
	public SIFMLLoadingPlugin(){
		SIObfuscationHelper.init();
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { SIClassTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return SIModContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
 
		
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
