/**
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Reads Formatter profiles
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class FormatterProfileReader
{

   private final Map<String, Properties> profiles = new LinkedHashMap<String, Properties>();

   /**
    * Creates a new {@link FormatterProfileReader} instance
    * 
    * @param inputStream a XML with the Eclipse Formatter format
    * @return
    * @throws IOException
    */
   public static FormatterProfileReader fromEclipseXml(InputStream inputStream) throws IOException
   {
      return new FormatterProfileReader(inputStream);
   }

   /**
    * Private constructor. Use the static methods to create instances of this object
    * 
    */
   private FormatterProfileReader(InputStream is) throws IOException
   {
      final EclipseFormatterProfileHandler handler = new EclipseFormatterProfileHandler();
      final SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser;
      try
      {
         parser = factory.newSAXParser();
         parser.parse(new InputSource(is), handler);
      }
      catch (ParserConfigurationException e)
      {
         throw new IOException("Error while parsing formatter XML", e);
      }
      catch (SAXException e)
      {
         throw new IOException(e);
      }
   }

   /**
    * @return the default settings for {@link FormatterProfileReader}
    */
   public Properties getDefaultProperties()
   {
      return profiles.values().iterator().next();
   }

   /**
    * Returns the {@link Properties} related to the formatter name
    */
   public Properties getPropertiesFor(String formatterName)
   {
      return profiles.get(formatterName);
   }

   /**
    * Returns the profile names in this {@link FormatterProfileReader}
    */
   public Set<String> getProfileNames()
   {
      return Collections.unmodifiableSet(profiles.keySet());
   }

   private class EclipseFormatterProfileHandler extends DefaultHandler
   {
      private final static String XML_NODE_PROFILE = "profile"; //$NON-NLS-1$
      private final static String XML_NODE_SETTING = "setting"; //$NON-NLS-1$

      private final static String XML_ATTRIBUTE_ID = "id"; //$NON-NLS-1$
      private final static String XML_ATTRIBUTE_NAME = "name"; //$NON-NLS-1$
      private final static String XML_ATTRIBUTE_VALUE = "value"; //$NON-NLS-1$

      private String currentProfileName;
      private Properties currentProperties;

      @Override
      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
      {
         if (XML_NODE_PROFILE.equals(qName))
         {
            // Start Profile
            currentProfileName = attributes.getValue(XML_ATTRIBUTE_NAME);
            currentProperties = new Properties();
         }
         else if (XML_NODE_SETTING.equals(qName))
         {
            final String key = attributes.getValue(XML_ATTRIBUTE_ID);
            final String value = attributes.getValue(XML_ATTRIBUTE_VALUE);
            currentProperties.setProperty(key, value);
         }
      }

      @Override
      public void endElement(String uri, String localName, String qName) throws SAXException
      {
         if (XML_NODE_PROFILE.equals(qName))
         {
            // End Profile
            profiles.put(currentProfileName, currentProperties);
            currentProfileName = null;
            currentProperties = null;
         }
      }
   }

}
