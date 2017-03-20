package com.jeffpeng.jmod.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class JAH {
	public static String getDescriptor(Class<?> clazz){
		return "L"+clazz.getName().replace(".", "/")+";";
	}
	
	public static String getDescriptor(String clazz){
		return "L"+clazz.replace(".", "/")+";";
	}
	
	public static TypeInsnNode NEW(Class<?> clazz){
		return new TypeInsnNode(Opcodes.NEW,clazz.getName().replace(".", "/"));
	}
	
	public static TypeInsnNode NEW(String clazz){
		return new TypeInsnNode(Opcodes.NEW,clazz.replace(".", "/"));
	}
	
	public static InsnNode DUP(){
		return new InsnNode(Opcodes.DUP); 
	}
	
	public static VarInsnNode ALOAD(int a){	
		return new VarInsnNode(Opcodes.ALOAD,a);	
	}
	
	public static FieldInsnNode GETSTATIC(String base,String field, String fieldtype){
		return new FieldInsnNode(Opcodes.GETSTATIC,base.replace(".", "/"),field,getDescriptor(fieldtype));
	}
	
	public static MethodInsnNode INVOKESPECIAL(String base, String method, String... arguments){
		String argdesc = "(";
		for(int x = 0; x < arguments.length;x++){
			argdesc += getDescriptor(arguments[x]);
		}
		argdesc += ")V";
		return new MethodInsnNode(Opcodes.INVOKESPECIAL,base.replace(".", "/"),method,argdesc,false);
	}
	
	
	
	//new MethodInsnNode(Opcodes.INVOKESPECIAL,"com/jeffpeng/jmod/API/forgeevents/JMODManageCreativeTabListEvent","<init>","(Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V",false)
	//new FieldInsnNode(Opcodes.GETSTATIC,"com/jeffpeng/jmod/JMOD","BUS","Lcpw/mods/fml/common/eventhandler/EventBus;")

}
