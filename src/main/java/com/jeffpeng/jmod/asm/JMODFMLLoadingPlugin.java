package com.jeffpeng.jmod.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;


@MCVersion(value="1.7.10")
@TransformerExclusions(value={"com.jeffpeng.jmod.asm"})
public class JMODFMLLoadingPlugin implements IFMLLoadingPlugin {
	
	public JMODFMLLoadingPlugin(){
		JMODObfuscationHelper.init();
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { JMODClassTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return JMODModContainer.class.getName();
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
