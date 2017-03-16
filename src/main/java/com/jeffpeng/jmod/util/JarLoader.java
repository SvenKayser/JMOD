package com.jeffpeng.jmod.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
@SuppressWarnings({ "rawtypes"})
public class JarLoader {
	
	private static Method addURL;
	private static URLClassLoader sysloader;
	
	private static final Class[] parameters = new Class[] {URL.class};
	
	static {
		sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		try {
			addURL = URLClassLoader.class.getDeclaredMethod("addURL", parameters);
			addURL.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

    public static void addFile(String filename) throws IOException
    {
        addFile(new File(filename));
    }

    public static void addFile(File file) throws IOException
    {
        addURL(file.toURI().toURL());
    }

	public static void addURL(URL jar) throws IOException
    {
        try {
        	addURL.invoke(sysloader, new Object[] {jar});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }

    }
}
