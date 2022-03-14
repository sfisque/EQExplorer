/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latticeware.eqexplorer.io;

import com.latticeware.eqexplorer.Munger;
import com.latticeware.eqexplorer.data.BitmapInfo;
import com.latticeware.eqexplorer.data.BitmapInfoReference;
import com.latticeware.eqexplorer.data.FileHeader_wld;
import com.latticeware.eqexplorer.data.Fragment_wld;
import com.latticeware.eqexplorer.data.BitmapName;
import com.latticeware.eqexplorer.data.BspRegion;
import com.latticeware.eqexplorer.data.BspRegionType;
import com.latticeware.eqexplorer.data.BspTree;
import com.latticeware.eqexplorer.data.GlobalAmbientLight;
import com.latticeware.eqexplorer.data.Material;
import com.latticeware.eqexplorer.data.MaterialList;
import com.latticeware.eqexplorer.data.Mesh;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static final HashMap<Byte,Class<? extends Fragment_wld>> PROCESSOR_MAP;

        
    private final FileHeader_wld wldHeader = new FileHeader_wld();
    private final List<Fragment_wld> fragmentList = new ArrayList<>();
    
    
    
    static
    {
        PROCESSOR_MAP = new HashMap<>( 31 );
        
            // Materials

        PROCESSOR_MAP.put((byte) 0x03, BitmapName.class);
        PROCESSOR_MAP.put( (byte) 0x04, BitmapInfo.class);
        PROCESSOR_MAP.put( (byte) 0x05, BitmapInfoReference.class);
        PROCESSOR_MAP.put( (byte) 0x30, Material.class);
        PROCESSOR_MAP.put( (byte) 0x31, MaterialList.class);
        
            // BSP Tree

        PROCESSOR_MAP.put( (byte) 0x21, BspTree.class);
        PROCESSOR_MAP.put( (byte) 0x22, BspRegion.class);
        PROCESSOR_MAP.put( (byte) 0x29, BspRegionType.class);
        
            // Meshes

        PROCESSOR_MAP.put( (byte) 0x36, Mesh.class);
        PROCESSOR_MAP.put( (byte) 0x37, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x2f, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x2d, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x2c, Fragment_wld.class);
        
            // Animation

        PROCESSOR_MAP.put( (byte) 0x10, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x11, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x12, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x13, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x14, Fragment_wld.class);

            // Lights

        PROCESSOR_MAP.put( (byte) 0x1b, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x1c, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x28, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x2a, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x35, GlobalAmbientLight.class );
        
            // Vertex colors
        PROCESSOR_MAP.put( (byte) 0x32, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x33, Fragment_wld.class);
        
        // Particle Cloud
        PROCESSOR_MAP.put( (byte) 0x26, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x27, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x34, Fragment_wld.class);
        
        // General
        PROCESSOR_MAP.put( (byte) 0x15, Fragment_wld.class);

        // unknown use
        PROCESSOR_MAP.put( (byte) 0x06, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x07, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x08, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x09, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x16, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x17, Fragment_wld.class);
        PROCESSOR_MAP.put( (byte) 0x18, Fragment_wld.class);
    }
    

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
            
            wldHeader.stringTable = Munger.wldStringConvert( wldHeader.stringHashSize, _stringBuffer );

            LOG.log( Level.INFO, "{0}\n\n", wldHeader );
                        
            while( _bais.available() > 0 && fragmentList.size() < wldHeader.fragmentCount )
            {
                _bais.read( _buffer, 0, 4 );
                Integer _size = Munger.fourBytesToInt( _buffer );
                _bais.read( _buffer, 0, 4 );
                Integer _id = Munger.fourBytesToInt( _buffer );
                
                Fragment_wld _frag = PROCESSOR_MAP.get( _id.byteValue() ).getConstructor().newInstance();
                _frag.id = _id;
                _frag.size = _size;
                
                byte[] _fragBuffer = new byte[ _size ];
                _bais.read( _fragBuffer, 0, _size );
                _frag.processBytes( _fragBuffer );
                
                LOG.log( Level.INFO, "{0}::{1}\n\n", new Object[] { _frag, hexdump( _fragBuffer ) } );
                // deconstruct the extra into the fragment type bits based on id field
                
                // then add it to the fragment list
                
                fragmentList.add( _frag );
            }
            
            LOG.log( Level.INFO, "{0}\n\n", fragmentList );
        }
        catch( IOException | SecurityException | NoSuchMethodException 
                | InvocationTargetException 
                | InstantiationException 
                | IllegalAccessException 
                | IllegalArgumentException err )
        {
            LOG.log( Level.SEVERE, "", err );
        }
    }

    
    private String hexdump( byte[] _stream )
    {
        StringBuilder _sb = new StringBuilder();
        
        for( byte _b : _stream )
        {
            _sb.append( String.format( " 0x%02x", _b ) );
        }
        
        return _sb.toString();
    }

}
