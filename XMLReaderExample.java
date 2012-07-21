import java.io.Exception;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class XMLReaderExample{

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL= "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    final URL url;

    public XMLReaderExample(String feedUrl){
        try{
            this.url = new URL(feedUrl);
        } catch(MalformedURLException e){
            throw new RuntimeException(e);        
        }
    }
    
    @SuppressWarnings("null")
     public Feed readFeed(){
        Feed feed = null;
        try{
            //set header values initial to the empty string
            boolean isFeedHeader = true;
            String description = "";
            String title = ""; 
            String link = "";
	    String language = "";
	    String copyright = "";
	    String author = "";
	    String pubdate = "";
	    String guid = "";

            //First you need to create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            //Secondly, you need to set up a new eventReader by calling it on the inputfactory
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createEventReader(in);
            //Read, parse XML document 
            while(eventReader.hasNext(){
                    //Create an event 
                    XMLEvent = eventReader.nextEvent();
                    //Check if its that start element
                    if(event.isStartElement()){
                        if(event.asStartElement().getName().getLocalPart() == (ITEM)){
                            if(isFeedHeader){
                                isFeedHeader = false;
                                feed = new Feed(title, link, description, language, copyright, pubdate);    
                            } 
                            event = eventReader.nextEvent();
                            continue;                       
                         }

                        if(event.asStartElement().getName().getLocalPart() == (TITLE)){
                            event = eventReader.nextEvent();
                            title = event.asCharacters().getData();
                            continue;
                        }
                       
                        if(event.asStartELement().getName().getLocalPart() == (DESCRIPTION))
                            event = eventReader.nextEvent();
                            description = event.asCharacters().getData();

                            }
                        if(event.asStartElement().getName().getLocalParts() == (LINK)){
                            event = eventReader.nextEvent();
                            link = event.asCharacters().getData();
                            continue;                   
                        }

                        if(event.asStartElement().getName().getLocalParts() == (GUID)){
                            event = eventReader.nextEvent();
                            guid = event.asCharacters().getData();  
                       }

                       if(event.asStartElement().getName().getLocalPart() == (LANGUAGE)) {
                                 event = eventReader.nextEvent();
                                 language = event.asCharacters().getData();
                                 continue;
                        }
                        if(event.asStartElement().getName().getLocalPart() == (AUTHOR)) {
                                  event = eventReader.nextEvent();
                                  author = event.asCharacters().getData();
                                  continue;
                        }
                        if(event.asStartElement().getName().getLocalPart() == (PUB_DATE)) {
                                   event = eventReader.nextEvent();
                                   pubdate = event.asCharacters().getData();
                                   continue;
                        }
                        if(event.asStartElement().getName().getLocalPart() == (COPYRIGHT)) {
                                  event = eventReader.nextEvent();
                                  copyright = event.asCharacters().getData();
                                  continue;
                       }
                   }
                else if(event.isEndElement()){
                    if(event.aEndElement().getName(),getLocalPart() ==(ITEM)){
                        FeedMessage message = new FeedMessage();
                        message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
			message.setLink(link);
			message.setTitle(title);
			feed.getMessages().add(message);
			event = eventReader.nextEvent();
			continue;
                     }
                }

             }
       }catch(XMLStreamException e){
                    trow new RuntimeException(e);
       }
        return feed;
    }

    private InputStream read(){
        try{
            return url.openStream();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
