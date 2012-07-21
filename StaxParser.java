import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class StaxParser{
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String DESCRIPTION = "description";
    private static final String SOURCE = "source";
    private static final String ENCLOSURE= "enclosure";
    private static final String PUBDATE = "pubDate";
    private static final String GUID = "guid"; 
    private static final String CATEGORY = "category";
   
        public List<Item> readConfig(String configFile){
            List<Item> items = new ArrayList<Item>();
            try{
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = FileInputStream(configFile); //may add fetcher here
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
                Item item = null;
                while(eventReader.hasNext()){
                    XMLEvent event = eventReader.nextEvent();

                    if(startElement.getName().getLocalPart() == (ITEM)){
                        item = new Item();
                    //we read the attributes from this tag and the date attribute to 
                    //our object
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while(attributes.hasNext()){
                        Attribute attribute = attributes.next();
                        if(attribute.getName().toString().equals(TITLE)){
                            item.setTitle(attribute.getValue()));
                        }
                    }
                }
            if(event.isStartElement()){
                if(event.asStartElement().getName().getLocalPart().equals(LINK)){
                    event = eventReader.nextEvent();
                    item.setMode(event.asCharacters().getData());
                    continue;
                }
            }
            if(event.asStartElement().getName().getLocalPart().equals(PUBDATE)){
                event = eventReader.nextEvent();
                item.setPubDate(event.asCharacters().getData());
                continue;
            }

            if(event.asStartElement().getName().getLocalPart().equals(DESCRIPTION)){
                event = eventReader.nextEvent();
                item.setDescription(event.asCharacters().getData());
                continue;
            }

            if(event.asStartElement().getName().getLocalPart().equals(SOURCE)){
                event = eventReader.nextEvent();
                item.setSource(event.asCharacters().getData());
                continue;
            }

            if(event.asStartElement().getName().getLocalPart().equals(ENCLOSURE)){
                event = eventReader.nextEvent();
                item.setEnclosure(event.asCharacters().getData());
                continue;
            }

             if(event.asStartElement().getName().getLocalPart().equals(GUID)){
                 event = eventReader.nextEvent();
                item.setGuid(event.asCharacters().getData());
                continue;
            }

            if(event.asStartElement().getName().getLocalPart().equals(CATEGORY)){
                event = eventReader.nextEvent();
                item.setCategory(event.asCharacters().getData());
                continue;
            }
    }
            if(event.isEndElement()){
                EndElement endElement = event.asEndElement();
                if(endElement.getName().getLocalPart() == (ITEM){
                        items.add(item);
                }
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(XMLStreamException e){
                 e.printStackTrace();
            }
        return items;
       }
    }
