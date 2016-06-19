package com.sapashev;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEConfig;

/**
 * @author Arslan Sapashev
 * @since 19.06.2016
 * @version 1.0
 */
public class MissedArgumentException extends Exception{
    public MissedArgumentException (String s){
        super(s);
    }
}
