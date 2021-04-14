/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.io;

import com.latticeware.eqexplorer.Munger;
import com.latticeware.eqexplorer.data.FileHeader_wld;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sfisque
 */
public class Stream_wld
{
    private static final Logger LOG = Logger.getLogger(Stream_wld.class.getName() );
    
    private static final int[] conversion = { 0x95, 0x3A, 0xC5, 0x2A, 0x95, 0x7A, 0x95, 0x6A };

        
    private FileHeader_wld wldHeader = new FileHeader_wld();


    public void load( byte[] fileBuffer ) 
    throws IOException
    {
        try ( ByteArrayInputStream _bais = new ByteArrayInputStream( fileBuffer ) )
        {
            byte[] _buffer = new byte[ 4 ];
            _bais.read( _buffer, 0, 4 );
            wldHeader.magic = Munger.fourBytesToInt( _buffer );
            _bais.read( _buffer, 0, 4 );
            wldHeader.version = Munger.fourBytesToInt( _buffer );
            _bais.read( _buffer, 0, 4 );
            wldHeader.fragmentCount = Munger.fourBytesToInt( _buffer );
            _bais.read( _buffer, 0, 4 );
            wldHeader.bspRegionCount = Munger.fourBytesToInt( _buffer );
            _bais.read( _buffer, 0, 4 );
            wldHeader.unknown = Munger.fourBytesToInt( _buffer );
            _bais.read( _buffer, 0, 4 );
            wldHeader.stringHashSize = Munger.fourBytesToInt( _buffer );
            _bais.read( _buffer, 0, 4 );
            wldHeader.headerSix = Munger.fourBytesToInt( _buffer );
            
            byte[] _stringBuffer = new byte[ wldHeader.stringHashSize ];
            
            _bais.read( _stringBuffer, 0, wldHeader.stringHashSize );
            
            byte[] _string = new byte[ wldHeader.stringHashSize ];
            int _len = 0;

            for( int _index = 0; _index < wldHeader.stringHashSize; _index ++ )
            {
                int _char = ( _stringBuffer[ _index ] ^ conversion[ _index % 8 ] ) % 256;

                if( _char == 0 )
                {
                    if( _len > 0 )
                    {
                        String _s = new String( _string, 0, _len );
                        wldHeader.addString( _s );
                    }
                    _len = 0;
                }
                else
                {
                    _string[ _len ] = (byte) _char;
                    _len ++;
                }
            }
            
            System.out.println( wldHeader );
        }
        catch( IOException err )
        {
            LOG.log( Level.SEVERE, "", err );
        }
    }
    
}
