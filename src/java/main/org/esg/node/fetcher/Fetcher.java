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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.OutputStream;
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
import java.util.List;
/**
   Description:
   This class pulls files from desginated targets to local host
*/
public class Fetcher {

    private File file;
    private String url;
    private HttpClient client;
    private int retry;
    private InputStream in;
    private static final String[] DEFAULT_SCHEMES = {"http", "https", "ftp"};
    private URL aUrl;
    private String fileName;
    private GetMethod getHttp;
    String[] arrayOfUrls;
   
     public Fetcher(){
        client = null;
        retry = 0;
        url = null;
        aUrl = null;
        fileName = null;  
        getHttp = null;
        file = null;
    }

    public Fetcher(String[] array, int numRetries){
        arrayOfUrls = array;
        for(String urls: arrayOfUrls){
            fetchURLContent(urls, numRetries, fileInfo(urls));
        }  
        //FileUtils.toFile(arrayOfUrls);
    }

    /* Method gets an URL, gets its contents, and uses apache File Utils to process a stream, and copy it into a file. 
       If the file doesn't exist, it would create it. If it already exists it would be overwritten.
       It also checks that the URL is valid, and has a retry method that if the fetching fails it would retry x times according to what
       the user specified. 
    */
    public File fetchURLContent(String address, int retries, File file){
        url = address;
	retry = retries;     
        UrlValidator urlValidator = new UrlValidator(DEFAULT_SCHEMES, UrlValidator.ALLOW_2_SLASHES);
	if(urlValidator.isValid(url)) {
	    System.out.print("Success! Url was valid");
	}else{ System.out.print("Url was invalid. Please try again."); }

	client = new HttpClient();
	getHttp = new GetMethod(url);
	getHttp.setFollowRedirects(true);
	DefaultHttpMethodRetryHandler retryHandler = new DefaultHttpMethodRetryHandler(retry, false); 
        getHttp.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryHandler);
	
	try{
	    int codeStatus =  client.executeMethod(getHttp);
	    if(codeStatus != HttpStatus.SC_OK){ System.out.println("Method failed: " + getHttp.getStatusLine()); }
	    else{System.out.println("Success");}
            InputStream in = getHttp.getResponseBodyAsStream();
            copyInputStreamToFile(in, file);
            // FileUtils.copyInputStreamToFile(in, file);
            //readContent(in, url, file);
            
 
	}catch(HttpException e){
	    System.out.println(e.getMessage());
	    e.printStackTrace();
	}catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace(); 
        }finally { getHttp.releaseConnection(); }

        return file;
    }
    
    public void copyInputStreamToFile(InputStream source, File destination) throws IOException{
        try{
            FileOutputStream output = FileUtils.openOutputStream(destination);
            try{
                IOUtils.copyLarge(source,output);
                output.close();
            }finally{
                IOUtils.closeQuietly(output);
            } 
        }finally{
            IOUtils.closeQuietly(source);
        }
    }

    public File fileInfo(String url){
        try{ aUrl = new URL(url);
        }catch(MalformedURLException e){
            System.err.println("Malformend exception: " + e.getMessage());
        }
        fileName = aUrl.getFile();
        String workDir = System.getProperty("user.dir");
        file = new File(workDir,fileName);
        String userPath = file.getPath();
        return file;
    }
 
    public static void main(String[] args) throws Exception {
	if(args.length !=3) {
	    System.out.println("Please enter 3 arguments: url,retry number, and the directory to save");
	    System.exit(1);
	}
      
      
        File urlFile = new File(args[0]);
	int numRetries = Integer.parseInt(args[1]);

        if(urlFile.isFile()){
            String encoding = System.getProperty("file.encoding");
            List<String> list = FileUtils.readLines(urlFile, encoding);
            String[] urlArray = list.toArray(new String[0]);
            String directory = args[2];
            Fetcher fetcher = new Fetcher(urlArray,numRetries);
        }else{
          Fetcher fetcher = new Fetcher();
          File aFile = new File(args[2]);
          String http = args[0];
          fetcher.fetchURLContent(http, numRetries, aFile); 
          fetcher.fileInfo(http);
        }
       

       
    }
}
