/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.data;

import com.latticeware.eqexplorer.Munger;
import java.util.Arrays;

/**
 *
 * @author sfisque
 */
public class Fragment_wld_03
extends Fragment_Ref_wld
{  
    /*
    Size1 : DWORD
Contains the number of texture filenames in this fragment. Most of the time there is only one name. Entries (there are Size1 of them):
NameLength: WORD
Contains the length of the filename in bytes.
FileName: BYTEs
    */
    public Integer size1;
    public Short nameLength;
    public String fileName;

    
    @Override
    public void processBytes( byte[] _buffer )
    {
        super.processBytes( _buffer );
        
        byte[] _slice = Arrays.copyOfRange( _buffer, 4, 8 );
        size1 = Munger.fourBytesToInt( _slice );

        _slice = Arrays.copyOfRange( _buffer, 8, 10 );
        nameLength = (short) Munger.twoBytesToInt( _slice );
        
        _slice = Arrays.copyOfRange( _buffer, 10, 10 + nameLength );
System.out.println( this.toString() );
Munger.hexDump( _slice );

        fileName = Munger.wldStringConvert( nameLength, _slice ).get( 0 );
    }

    @Override
    public String toString()
    {
        return "Fragment_wld_03{" 
                + "size=" + size
                + ", id=" + id
                + ", nameReference=" + nameReference 
                + ", size1=" + size1 
                + ", nameLength=" + nameLength 
                + ", fileName=" + fileName 
                + '}';
    }
    
    
    
}
