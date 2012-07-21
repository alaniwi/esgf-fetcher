/***************************************************************************
*                                                                          *
*  Organization: Earth System Grid Federation                              *
*                                                                          *
****************************************************************************
*                                                                          *
*   Copyright (c) 2009, Lawrence Livermore National Security, LLC.         *
*   Produced at the Lawrence Livermore National Laboratory                 *
*   LLNL-CODE-420962                                                       *
*                                                                          *
*   All rights reserved. This file is part of the:                         *
*   Earth System Grid Federation (ESGF)                                    *
*   Data Node Software Stack, Version 1.0                                  *
*                                                                          *
*   For details, see http://esgf.org/esg-node-site/                        *
*   Please also read this link                                             *
*    http://esgf.org/LICENSE                                               *
*                                                                          *
*   * Redistribution and use in source and binary forms, with or           *
*   without modification, are permitted provided that the following        *
*   conditions are met:                                                    *
*                                                                          *
*   * Redistributions of source code must retain the above copyright       *
*   notice, this list of conditions and the disclaimer below.              *
*                                                                          *
*   * Redistributions in binary form must reproduce the above copyright    *
*   notice, this list of conditions and the disclaimer (as noted below)    *
*   in the documentation and/or other materials provided with the          *
*   distribution.                                                          *
*                                                                          *
*   Neither the name of the LLNS/LLNL nor the names of its contributors    *
*   may be used to endorse or promote products derived from this           *
*   software without specific prior written permission.                    *
*                                                                          *
*   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS    *
*   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT      *
*   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS      *
*   FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL LAWRENCE    *
*   LIVERMORE NATIONAL SECURITY, LLC, THE U.S. DEPARTMENT OF ENERGY OR     *
*   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,           *
*   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT       *
*   LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF       *
*   USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND    *
*   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,     *
*   OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT     *
*   OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF     *
*   SUCH DAMAGE.                                                           *
*                                                                          *
***************************************************************************/
package org.esg.node.fetcher;

import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.commons.validator.routines.*;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.io.FileUtils;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream; 
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.CharBuffer;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
   Description:
   Testing the Fetcher class...
*/
public class TestFetcher{

    HttpClient client = new HttpClient();
    GetMethod http = new GetMethod("http://rainbow.llnl.gov/dist/esgf-installer/HOWTO");
    Fetcher fetcher = new Fetcher();
    private int expectedResponseStatus;
    private String expectedResponseBody;
    

    @Test
    public void testGet() throws URIException,URISyntaxException{
        URI uri = new URI("http://rainbow.llnl.gov/dist/esgf-installer/HOWTO", true);
        assertEquals(uri, http.getURI());
    }

    @Test
    public void testURLValidatorWithSchemes(){
      String[] schemes = {"http", "https"};
      UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_2_SLASHES);
      assertTrue(urlValidator.isValid("http://rainbow.llnl.gov/dist/esgf-installer/HOWTO"));
    } 

    @Test
    public void testInvalidURLValidator(){
      String[] schemes = {"http", "https", "ftp"};
      UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_2_SLASHES);
      assertFalse(urlValidator.isValid("http://hostname/test/index.html"));
    }
   
   @Test 
   public void testURLValidatorWithParenthesis(){
      String[] schemes ={"http", "https", "ftp"};
      UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_2_SLASHES);
      assertTrue(urlValidator.isValid("http://somewhere.com/pathxyz/file(1).html"));
   }


   @Test 
    public void testRetryMethod(){
       int retry = 6;
       DefaultHttpMethodRetryHandler retryHandler = new DefaultHttpMethodRetryHandler(retry, false);
       assertEquals(retry, retryHandler.getRetryCount());
    }

    /* @Test
    public void testDirectory(){
     
    }*/
   
    @Test
    public void testGetResponse(){
       GetMethod httpG = new GetMethod("/"); 
       httpG.getParams().setParameter("http.socket.timeout", new Integer(5000));
       httpG.releaseConnection();
       assertEquals(5000, httpG.getParams().getParameter("http.socket.timeout"));

         
    }
    @Test
    public void testReadContent()throws IOException{
        File file1 = fetcher.fetchURLContent("http://rainbow.llnl.gov/dist/esgf-installer/HOWTO", 4, new File("Result2.txt"));
        File file2 = new File("HOWTO.txt");
        assertTrue(FileUtils.contentEquals(file1, file2));
         
       } 

    /* @Test 
        public void testReadContentWithFile() throws IOException{

        }*/
}