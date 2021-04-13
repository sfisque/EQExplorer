package com.latticeware.eqexplorer;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.latticeware.eqexplorer.io.Stream_s3d;


/**
 *
 * @author sfisque
 */
public class Main
{
    private static final Logger LOG = Logger.getLogger( Main.class.getName() );


    public static void main( String[] _argv )
    {
        Stream_s3d _m = new Stream_s3d();
        
        try
        {
            _m.load( _argv[ 0 ] );
        }
        catch( IOException ex )
        {
            LOG.log( Level.SEVERE, null, ex );
        }
    }
        
}
