package com.jeffpeng.jmod;

public class Defines {
	
	public class UseOredict {
		public static final short DENY = 0;
		public static final short ALLOW = 1;
		public static final short PREFER = 2;
		public static final short EXCLUSIVE = 3;
	}
	
	public static class Sides{
		public static final int NONE   = 0;			// 000000
		public static final int BOTTOM = 1 << 0; 	// 000001 
		public static final int TOP    = 1 << 1;	// 000010
		public static final int NORTH  = 1 << 2;	// 000100
		public static final int SOUTH  = 1 << 3;	// 001000
		public static final int WEST   = 1 << 4;	// 010000
		public static final int EAST   = 1 << 5;	// 100000
		public static final int SIDES  = 60; 		// 111100		
		public static final int ALL    = 63; 		// 111111
	}
	
	public static class Listmode{
		public static final boolean ALL_EXCEPT 	= true;
		public static final boolean NONE_EXCEPT = false;
	}
}
