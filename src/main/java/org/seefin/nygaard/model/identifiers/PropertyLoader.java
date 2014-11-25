package org.seefin.nygaard.model.identifiers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper to load configuration property files, for identifier types
 * having a configurable scheme (such as MSISDN, IBAN)
 * 
 * @author phillipsr
 *
 */
class PropertyLoader
{
	public static final String NO_SCHEMAS_LOADED = "No schemes were loaded";
    public static final String SCHEMA_LOCATION_INVALID = "Schema location may not be null or empty";

    /**
     * Load the scheme definitions from a properties file located on
     * the classpath, with the path <code>ibanFormats</code>, if it exists
     *
     * @return the loaded schema map
     * @throws RuntimeException
     */
    static Properties
    getProperties ( String propertyFilePath)
    {
        if  ( propertyFilePath == null || propertyFilePath.isEmpty () == true)
        {
            throw new IllegalArgumentException ( SCHEMA_LOCATION_INVALID);
        }
        Properties schemes = new Properties();
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = tccl.getResourceAsStream(propertyFilePath);
        try
        {
            if ( inputStream == null || inputStream.available () <= 0)
            {
                throw new ExceptionInInitializerError (
                        "No properties available (location='"
                            + propertyFilePath + "')");
            }
        }
        catch ( IOException e)
        {
            ExceptionInInitializerError throwMe = new ExceptionInInitializerError (
                    "Cannot access properties (location='"
                            + propertyFilePath + "')");
            throwMe.initCause ( e);
            throw throwMe;
        }
        try
        {
            schemes.load(inputStream);
        }
        catch ( Exception e)
        {
            throw new RuntimeException ("Failed to load properties (location='"
                    + propertyFilePath + "')", e);
        }
        // for some reason, opening an empty ("") resource as a stream, returns the token "com"
        if ( schemes.size () == 0 || (schemes.size() == 1 && "".equals ( schemes.get ( "com"))))
        {
            throw new ExceptionInInitializerError ( NO_SCHEMAS_LOADED 
            			+ " (location='" + propertyFilePath + "')");
        }
        return schemes;
    }

	/**
	 * @param schemeResource
	 * @return
	 * @throws IOException 
	 */
	public static Properties
	getPropertiesFromFile ( File schemeFile)
		throws IOException
	{
        if  ( schemeFile == null)
        {
            throw new IllegalArgumentException ( SCHEMA_LOCATION_INVALID);
        }
        Properties schemes = new Properties();
        try
        {
            schemes.load(new FileInputStream ( schemeFile));
        }
        catch ( Exception e)
        {
            throw new RuntimeException ("Failed to load properties (location='"
                    + schemeFile.getCanonicalPath () + "')", e);
        }
        // for some reason, opening an empty ("") resource as a stream, returns the token "com"
        if ( schemes.size () == 0 || (schemes.size() == 1 && "".equals ( schemes.get ( "com"))))
        {
            throw new ExceptionInInitializerError ( NO_SCHEMAS_LOADED 
            			+ " (location='" + schemeFile.getCanonicalPath () + "')");
        }
        return schemes;
	}

}
