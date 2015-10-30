// IRemoteService.aidl
package com.example.aidl;
// Declare any non-default types here with import statements

/** Example service interface */
interface IRemoteService {
    /** Request the process ID of this service, to do evil things with it. */
    int getPid();

    /** Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(in int anInt,in long aLong,in boolean aBoolean, in float aFloat,
            in double aDouble,in String aString);
        
    void complexTypes(inout Rect r); 
    
}