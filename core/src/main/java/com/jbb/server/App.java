package com.jbb.server;

import com.jbb.server.common.lang.LanguageHelper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        System.out.println(LanguageHelper.getLocalizedMessage("jbb.error.live.detect.code." + "-5008",
                            "zh"));
        
        System.out.println(LanguageHelper.getLocalizedMessage("jbb.error.live.detect.code." + "-5005",
            "zh"));
    }
}
