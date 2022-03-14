package com.latticeware.eqexplorer;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.latticeware.eqexplorer.io.Stream_s3d;
import com.latticeware.eqexplorer.io.Stream_wld;
import com.latticeware.eqexplorer.data.DirectoryEntry_s3d;
import java.util.Scanner;


/**
 *
 * @author sfisque
 */
public class Main
{
    private static final Logger LOG = Logger.getLogger( Main.class.getName() );


    public static void main( String[] _argv )
    {
        String _fileName = "";
        if( _argv.length > 0 )
        {
            _fileName = _argv[ 0 ];
        }
        else
        {
            System.out.println( "provide an s3d file to introspect" );
            
            Scanner _scanner = new Scanner( System.in );
            _fileName = _scanner.next();
        }
        
        Stream_s3d s3dStream = new Stream_s3d();
        
        try
        {
            s3dStream.load( _fileName );
            
            for( DirectoryEntry_s3d entry_s3d : s3dStream.getEntries() )
            {
                if( entry_s3d.fileName != null && entry_s3d.fileName.endsWith( ".wld" ) )
                {
                    Stream_wld wldStream = new Stream_wld();
                    
                    wldStream.load( entry_s3d.rawData );
                }
            }
        }
        catch( IOException ex )
        {
            LOG.log( Level.SEVERE, null, ex );
        }
    }
        
}
