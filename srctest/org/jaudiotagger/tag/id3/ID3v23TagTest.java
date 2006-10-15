/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.*;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 */
public class ID3v23TagTest extends TestCase
{
    /**
     * Constructor
     * @param arg0
     */
    public ID3v23TagTest(String arg0) {
        super(arg0);
    }

    /**
     * Command line entrance.
     * @param args
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    /////////////////////////////////////////////////////////////////////////
    // TestCase classes to override
    /////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    protected void setUp()
    {
    }

    /**
     *
     */
    protected void tearDown()
    {
    }

    /**
     *
     */
//    protected void runTest()
//    {
//    }

    /**
     * Builds the Test Suite.
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(ID3v23TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////

    public void testReadID3v1ID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

    public void testReadID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

     public void testReadPaddedID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2pad.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

     public void testDeleteID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());

        mp3File.setID3v1Tag(null);
        mp3File.setID3v2Tag(null);
        try
        {
            mp3File.save();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNull(mp3File.getID3v2Tag());
    }

    public void testCreateIDv23Tag()
    {
        ID3v23Tag v2Tag = new ID3v23Tag();
        assertEquals((byte)2,v2Tag.getRelease());
        assertEquals((byte)3,v2Tag.getMajorVersion());
        assertEquals((byte)0,v2Tag.getRevision());
    }

    public void testCreateID3v23FromID3v11()
    {
           ID3v11Tag v11Tag = ID3v11TagTest.getInitialisedTag();
           ID3v23Tag v2Tag = new ID3v23Tag(v11Tag);
           assertNotNull(v11Tag);
           assertNotNull(v2Tag);
           assertEquals(ID3v11TagTest.ARTIST,((FrameBodyTPE1)((ID3v23Frame)v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST)).getBody()).getText());
           assertEquals(ID3v11TagTest.ALBUM,((FrameBodyTALB)((ID3v23Frame)v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ALBUM)).getBody()).getText());
           assertEquals(ID3v11TagTest.COMMENT,((FrameBodyCOMM)((ID3v23Frame)v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_COMMENT)).getBody()).getText());
           assertEquals(ID3v11TagTest.TITLE,((FrameBodyTIT2)((ID3v23Frame)v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TITLE)).getBody()).getText());
           assertEquals(ID3v11TagTest.TRACK_VALUE,((FrameBodyTRCK)((ID3v23Frame)v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TRACK)).getBody()).getText());
           assertTrue(((FrameBodyTCON)((ID3v23Frame)v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_GENRE)).getBody()).getText().endsWith(ID3v11TagTest.GENRE_VAL));
           assertEquals(ID3v11TagTest.YEAR,((FrameBodyTYER)((ID3v23Frame)v2Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER)).getBody()).getText());

            assertEquals((byte)2,v2Tag.getRelease());
            assertEquals((byte)3,v2Tag.getMajorVersion());
            assertEquals((byte)0,v2Tag.getRevision());

    }

}
