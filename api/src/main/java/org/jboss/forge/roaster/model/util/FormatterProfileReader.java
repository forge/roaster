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

   private final Map<String, Properties> profiles = new LinkedHashMap<>();

   /**
    * Creates a new {@link FormatterProfileReader} instance.
    *
    * @param inputStream a XML with the Eclipse Formatter format
    * @return a new generated FormatterProfileReader
    * @throws IOException if the reading fails because of an IO event
    * @throws IllegalStateException if the reading fails because of an internal error
    */
   public static FormatterProfileReader fromEclipseXml(InputStream inputStream) throws IOException
   {
      return new FormatterProfileReader(inputStream);
   }

   private FormatterProfileReader(InputStream inputStream) throws IOException
   {
      final EclipseFormatterProfileHandler handler = new EclipseFormatterProfileHandler();
      final SAXParserFactory factory = SAXParserFactory.newInstance();

      try
      {
         SAXParser parser = factory.newSAXParser();
         parser.parse(new InputSource(inputStream), handler);
      }
      catch (ParserConfigurationException e)
      {
         throw new IllegalStateException("Error while creating the parser", e);
      }
      catch (SAXException e)
      {
         throw new IOException("Error while parsing formatter XML", e);
      }
   }

   /**
    * Get the default settings.
    * 
    * @return the default settings for {@link FormatterProfileReader}
    */
   public Properties getDefaultProperties()
   {
      return profiles.values().iterator().next();
   }

   /**
    * Returns the {@link Properties} related to the formatter name.
    * 
    * @param formatterName the formatter name
    * @return the properties related to the given formatter name
    */
   public Properties getPropertiesFor(String formatterName)
   {
      return profiles.get(formatterName);
   }

   /**
    * Returns the profile names in this {@link FormatterProfileReader}.
    * 
    * @return the profiler names
    */
   public Set<String> getProfileNames()
   {
      return Collections.unmodifiableSet(profiles.keySet());
   }

   private class EclipseFormatterProfileHandler extends DefaultHandler
   {
      private static final String XML_NODE_PROFILE = "profile";
      private static final String XML_NODE_SETTING = "setting";

      private static final String XML_ATTRIBUTE_ID = "id";
      private static final String XML_ATTRIBUTE_NAME = "name";
      private static final String XML_ATTRIBUTE_VALUE = "value";

      private String currentProfileName;
      private Properties currentProperties;

      @Override
      public void startElement(String uri, String localName, String qName, Attributes attributes)
      {
         if (XML_NODE_PROFILE.equals(qName))
         {
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
      public void endElement(String uri, String localName, String qName)
      {
         if (XML_NODE_PROFILE.equals(qName))
         {
            profiles.put(currentProfileName, currentProperties);
            currentProfileName = null;
            currentProperties = null;
         }
      }
   }
}