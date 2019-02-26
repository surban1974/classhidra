package it.classhidra.core.tool.util;

import java.lang.reflect.Field;
import java.util.Vector;

public abstract class ClassScope{
	

	
    public static Class<?> [] getLoadedClasses (final ClassLoader loader)
    {
        if (loader == null) throw new IllegalArgumentException ("null input: loader");
        if (CLASSES_VECTOR_FIELD == null)
            throw new RuntimeException ("ClassScope::getLoadedClasses() cannot" +
            " be used in this JRE", CVF_FAILURE);
        
        try
        {
            final Vector<?> classes = (Vector<?>) CLASSES_VECTOR_FIELD.get (loader);
            if (classes == null) return EMPTY_CLASS_ARRAY;
            
            final Class<?> [] result;
            
            // Note: Vector is synchronized in Java 2, which helps us make
            // the following into a safe critical section:
            
            synchronized (classes)
            {
                result = new Class [classes.size ()];
                classes.toArray (result);
            }
            
            return result;
        }
        // This should not happen if <clinit> was successful:
        catch (IllegalAccessException e)
        {
//            e.printStackTrace (System.out);
            
            return EMPTY_CLASS_ARRAY;
        }
    }
    
    
    private static final Field CLASSES_VECTOR_FIELD; // Set in <clinit> [can be null]
    
    private static final Class<?> [] EMPTY_CLASS_ARRAY = new Class [0];
    private static final Throwable CVF_FAILURE; // Set in <clinit>
    
    static
    {
        Throwable failure = null;
        
        Field tempf = null;
        try
        {
            // This can fail if this is not a Sun-compatible JVM
            // or if the security is too tight:
            
            tempf = ClassLoader.class.getDeclaredField ("classes");
            if (tempf.getType () != Vector.class)
                throw new RuntimeException ("not of type java.util.Vector: " +
                tempf.getType ().getName ());
            tempf.setAccessible (true);
        }
        catch (Throwable t)
        {
            failure = t;
        }
        CLASSES_VECTOR_FIELD = tempf;
        CVF_FAILURE = failure;
    }
} // End of class