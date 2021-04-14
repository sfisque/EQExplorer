/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sfisque
 */
public class FileHeader_wld
{
/*
Magic : DWORD
This always contains 0x54503D02. It identifies the file as a .WLD file.
Version : DWORD
For old-format .WLD files, this always contains 0x00015500. For new-format .WLD files, this always contains 0x1000C800.
FragmentCount : DWORD
Contains the number of fragments in the .WLD file, minus 1 (that is, the highest fragment index, starting at 0).
Header3 : DWORD
Should contain the number of 0x22 BSP Region fragments in the file.
Header4 : DWORD
Unknown purpose. Should contain 0x000680D4.
StringHashSize : DWORD    
    */
    public Integer magic;
    public Integer version;
    public Integer fragmentCount;
    public Integer bspRegionCount;
    public Integer unknown;
    public Integer stringHashSize;
    public Integer headerSix;
    
    public List<String> stringTable;

    
    public FileHeader_wld()
    {
        stringTable = new ArrayList<>();
    }

    
    public FileHeader_wld( Integer magic, Integer version, Integer fragmentCount
            , Integer bspRegionCount, Integer unknown, Integer stringHashSize
            , Integer headerSix )
    {
        this.magic = magic;
        this.version = version;
        this.fragmentCount = fragmentCount;
        this.bspRegionCount = bspRegionCount;
        this.unknown = unknown;
        this.stringHashSize = stringHashSize;
        this.headerSix = headerSix;
        
        if( this.unknown != 0x000680D4 )
        {
            throw new IllegalArgumentException( "field 5 contains quesitonable data");
        }
    }

    
    public boolean isOldVersion()
    {
        return version == 0x00015500;
    }
    
    
    public void addString( String _s )
    {
        stringTable.add( _s );
    }
    
    
    @Override
    public String toString()
    {
        return "FileHeader_wld{" 
                + "magic=" + magic
                + ", version=" + version 
                + ", fragmentCount=" + fragmentCount 
                + ", bspRegionCount=" + bspRegionCount
                + ", unknown=" + unknown 
                + ", stringHashSize=" + stringHashSize 
                + ", headerSix=" + headerSix 
                + ", stringTable=" + stringTable
                + '}';
    }
        
}
