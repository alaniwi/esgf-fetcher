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
import org.apache.commons.validator.routines.UrlValidatorimport;

import java.net.*;
import java.io.*; //FileInput(Output)Stream, InputStream,
import java.nio.*;
import java.nio.channels.spi;

/**
   Description:
   This class pulls files from desginated targets to local host
*/
public static class Fetcher {

    private static File file;
    private URL url;
    private HttpClient client;
    int retry;
    Path directory;
    String fileName;

    public void fetchURLContent(String address, int retries) {
	url = new URL(address);
        isValid(); //check this method. it might be unecessary! 
	
	retry = retries;
	HttpMethod getHttp = new GetMethod(url);
	getHttp.setFollowRedirects(true);
	HttpMethodRetryHandler retryHandler = new HttpMethodRetryHandler() { retryMethod(); }
        getHttp.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryHandler);
	
	try{
	    int codeStatus =  client.executeMethod(getHttp);
	    if(statusCode != HttpStatus.SC_OK){ System.err.println("Method failed: " + getHttp.getStatusLine().toString()); }
	    else{System.out.prinln("Success");}
	    /*InputStream in = getHttp.getResponseBodyAdStream();
	      FileOutPutStream out = new FileOutputStream(setDirectory);
	      int count = -1;
	      while((count = in.read(buffer)) != -1){
	      out.write(buffer, 0, count);
	      }
	      out.flush();
	      out.close(); */
	}catch(HttpException e){
	    System.err.println(e.getMessage);
	    e.printStackTrace();
	}
	//Read response body.Add buffer, and to check if there are more urls
	//InputStream in =  getHttp.getResponseBodyAsStream(); //byte[]
	//Buffer here!
	byte[] responseBody = getUrl.getResponseBodyAsStream();
	
	try {
	    SeekByteChannel sbc = Files.newByteChannel(responseBody);
	    ByteByffer buffer = ByteBuffer.allocate(1024);	
	    String encoding = System.getProperty("file.ecoding");
	    
	    while(sbc.read(buffer)) {
		buffer.rewind();
		System.out.print(Charset.forName(encoding).decode(buffer)); //gets name of the charset,Changes to Unicode
		buffer.flip();
	    }
	}catch(ClosedChannelException e) {
	    System.err.println(e.getMessage);
	    e.printStackTrace();
	}catch(AsynchronousCloseException e) {
	    System.err.println(e.getMessage);
	    e.printStackTrace();
	}catch(ClosedByInterruptException e) {
	    System.err.println(e.getMessage);
	    e.printStackTrace();  
	}catch(IOException io) {
	    System.err.println(e.getMessage);
	    e.printStackTrace();
	}finally { getHttp.releaseConnection(); }
    }

    public boolean retryMethod(final HttpMethod method, final IOException, int retryCount) {
	if(retryCount >= retry){ return false; }
	if(exception instanceof NoHttpResponseException){ return true; }
	if(!method.isRequestSent()){ return true;}
	
	return false;
    }

    public boolean setDirectory(String dir) {
	filename = url.getFile();   
	return file(url, new File(filename));
	/*	File currentDir = System.getProperty("user.dir");
		File dirc = new File(dir, filename);
		if(!dirc.exists){dirc.createNewFile();)
	*/
    }
	
    public String  getUrlInfo() {
	Path path = url.getPath;
	String fileN = url.getFile();
	return System.out.println("FileName: " + fileN + "Path: " + path);
    }

    public boolean validateUrl() {
	boolean isValid = false;
	String[] schemes = {"http", "https"};
	UrlValidator urlValidator = new UrlValidator(schemes);
	
	if(!UrlValidator.isValid(url)) {
	    System.out.print("Url was invalid. Please try again");
            isValid = false;
	}else{
	    isValid = true;
	}
	return isValid;
    }

    public static void main(String [ ] args) {
	if(args.length != 3) {
	    System.err.println("Please enter 3 arguments: url,retry number, and the directory to save");
	    System.exit(1);
	}
	
	String address = args[0];
        try{ 
	    int numRetries = Integer.parseInt(args[1]);
	}catch (NumberFormatException e) {
	    System.err.println("The third argument" + " must be an integer");
	    System.exit(1);
	}

	directory = args[2];
	client = new HttpClient();
	fetchURLContent(address, numRetries);
	setDirectory(args[2]);
    }
}
