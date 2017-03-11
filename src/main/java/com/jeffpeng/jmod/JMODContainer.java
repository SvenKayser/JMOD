package com.jeffpeng.jmod;

import java.io.File;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jeffpeng.jmod.interfaces.IStagedObject;
import com.jeffpeng.jmod.primitives.JMODInfo;
import com.jeffpeng.jmod.primitives.ModCreationException;

import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

public class JMODContainer implements ModContainer {

	private String modId;
	private String name;
	private String version;
	private String desc = "";
	private File source;
	private JMODRepresentation modInstance;
	private JMODInfo modinfo;
	@SuppressWarnings("unused")
	private LoadController controller;
	private EventBus eventBus;
	private List<String> ownedPackages = new ArrayList<>();
	private Set<ArtifactVersion> requirements = new HashSet<>();
	private List<ArtifactVersion> dependencies = new ArrayList<>();
	private List<ArtifactVersion> dependants = new ArrayList<>();

	public JMODContainer(JMODRepresentation mod, File source) throws ModCreationException {
		mod.setContainer(this);
		this.modinfo = mod.getModInfo();
		try {
			this.name = modinfo.name;
			this.modId = modinfo.modid;
			this.desc = modinfo.description;
			this.version = modinfo.version;
			this.source = source;
			this.modInstance = mod;

		} catch (NullPointerException e) {
			throw new ModCreationException("The modinfo section from your configuration is missing. I will not initialize without it.");
		}

		if (this.name == null || this.modId == null || this.desc == null || this.version == null)
			throw new ModCreationException(
					"One of the fields 'name', 'modid', 'description' and/or 'version' from the modinfo section of the mod's configuration is missing. These fields are -mandatory- and I will not initialize the mod without them.");
	}

	@Override
	public String getModId() {
		return modId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

	@Override
	public File getSource() {
		return source;
	}

	@Override
	public ModMetadata getMetadata() {
		ModMetadata meta = new ModMetadata();
		meta.name = name;
		meta.modId = modId;
		meta.description = desc + "\n\nThis mod was loaded by " + JMOD.NAME;
		meta.version = version;
		meta.authorList = modinfo.authors;
		meta.url = modinfo.url;
		meta.logoFile = modinfo.logo;
		meta.credits = modinfo.credits;
		return meta;
	}

	@Override
	public void bindMetadata(MetadataCollection mc) {
	}

	@Override
	public void setEnabledState(boolean enabled) {
	}

	@Override
	public Set<ArtifactVersion> getRequirements() {
		return this.requirements;
	}

	@Override
	public List<ArtifactVersion> getDependencies() {
		return this.dependencies;
	}

	@Override
	public List<ArtifactVersion> getDependants() {
		return this.dependants;
	}

	@Override
	public String getSortingRules() {
		return null;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		this.eventBus = bus;
		this.controller = controller;
		eventBus.register(this);
		return true;

	}

	@Override
	public boolean matches(Object mod) {
		return mod == modInstance;
	}

	@Override
	public JMODRepresentation getMod() {
		return modInstance;
	}

	@Override
	public ArtifactVersion getProcessedVersion() {
		return null;
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public String getDisplayVersion() {
		return this.version;
	}

	@Override
	public VersionRange acceptableMinecraftVersionRange() {
		return null;
	}

	@Override
	public Certificate getSigningCertificate() {
		return null;
	}

	@Override
	public Map<String, String> getCustomModProperties() {
		return null;
	}

	@Override
	public Class<?> getCustomResourcePackClass() {
		if (modInstance.isZipMod())
			return FMLFileResourcePack.class;
		return FMLFolderResourcePack.class;
	}

	@Override
	public Map<String, String> getSharedModDescriptor() {
		return null;
	}

	@Override
	public Disableable canBeDisabled() {
		return Disableable.NEVER;
	}

	@Override
	public String getGuiClassName() {
		return null;
	}

	@Override
	public List<String> getOwnedPackages() {
		return ownedPackages;
	}

	@Subscribe
	public void handleModStateEvent(FMLEvent event) {
		Collections.sort(modInstance.stageables);
		
		JMOD.LOG.info("modstage " + modInstance.getModId() + " " + event.getClass());
		
		IStagedObject.broadcast(event, modInstance.stageables);
		if (event instanceof FMLPreInitializationEvent){
			modInstance.on((FMLPreInitializationEvent) event);
			modInstance.fire("fmlPreInit");
		} else if (event instanceof FMLInitializationEvent) {
			modInstance.on((FMLInitializationEvent) event);
			modInstance.fire("fmlInit");
		} else if (event instanceof FMLPostInitializationEvent) {
			modInstance.on((FMLPostInitializationEvent) event);
			modInstance.fire("fmlPostInit");
		} else if (event instanceof FMLLoadCompleteEvent) {
			modInstance.on((FMLLoadCompleteEvent) event);
			modInstance.fire("fmlLoacComplete");
		} else if (event instanceof FMLServerAboutToStartEvent) {
			modInstance.on((FMLServerAboutToStartEvent) event);
			modInstance.fire("fmlServerBeforeStart");
		} else if (event instanceof FMLModIdMappingEvent) {
			modInstance.on((FMLModIdMappingEvent) event);
			modInstance.fire("fmlModIdMapping");
		}
	}

	@Override
	public String toString() {
		return "JMOD:" + getModId() + "{" + getVersion() + "}";
	}

}
