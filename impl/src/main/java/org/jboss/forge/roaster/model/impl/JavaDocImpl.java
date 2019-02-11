/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.impl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.jboss.forge.roaster.model.JavaDocTag;
import org.jboss.forge.roaster.model.source.JavaDocSource;

/**
 * A {@link JavaDocSource} implementation
 * 
 * Based on http://stackoverflow.com/questions/557007/how-to-insert-line-comments-in-code-programmatically-in-eclipse
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
@SuppressWarnings("unchecked")
public class JavaDocImpl<O> implements JavaDocSource<O>
{
   private static final String TAG_NAME_CANNOT_BE_NULL = "Tag name cannot be null";
   private final O origin;
   private final Javadoc javadoc;

   public JavaDocImpl(O origin, Javadoc javadoc)
   {
      requireNonNull(javadoc, "Javadoc cannot be null");
      this.origin = origin;
      this.javadoc = javadoc;
   }

   @Override
   public O getOrigin()
   {
      return origin;
   }

   @Override
   public Object getInternal()
   {
      return javadoc;
   }

   @Override
   public String getText()
   {
      List<TagElement> tagList = javadoc.tags();
      StringBuilder text = new StringBuilder();
      for (TagElement tagElement : tagList)
      {
         if (tagElement.getTagName() == null)
            for (Object fragment : tagElement.fragments())
            {
               if (fragment instanceof TextElement)
               {
                  text.append(((TextElement) fragment).getText());
               }
            }
      }
      return text.toString().trim();
   }

   @Override
   public String getFullText()
   {
      List<TagElement> tagList = javadoc.tags();
      StringBuilder text = new StringBuilder();
      for (TagElement tagElement : tagList)
      {
         if (tagElement.getTagName() != null)
         {
            text.append(tagElement.getTagName());
         }
         List<Object> fragments = tagElement.fragments();
         for (Iterator<Object> iterator = fragments.iterator(); iterator.hasNext();)
         {
            Object fragment = iterator.next();
            if (fragment instanceof SimpleName)
            {
               // Param name
               text.append(' ').append(fragment);
            }
            else
            {
               text.append(fragment);
               if (iterator.hasNext())
               {
                  text.append(' ');
               }
            }
         }
         text.append(System.getProperty("line.separator"));
      }
      return text.toString().trim();
   }

   @Override
   public Set<String> getTagNames()
   {
      Set<String> tagNames = new LinkedHashSet<>();
      List<TagElement> tagList = javadoc.tags();
      for (TagElement tagElement : tagList)
      {
         String tagName = tagElement.getTagName();
         if (tagName != null)
            tagNames.add(tagName);
      }
      return tagNames;
   }

   @Override
   public List<JavaDocTag> getTags()
   {
      List<JavaDocTag> tags = new ArrayList<>();
      List<TagElement> tagElements = javadoc.tags();
      for (TagElement tagElement : tagElements)
      {
         if (tagElement.getTagName() != null)
            tags.add(new JavaDocTagImpl(tagElement));
      }
      return tags;
   }

   @Override
   public List<JavaDocTag> getTags(String tagName)
   {
      requireNonNull(tagName, TAG_NAME_CANNOT_BE_NULL);
      List<JavaDocTag> tags = new ArrayList<>();
      List<TagElement> tagElements = javadoc.tags();
      for (TagElement tagElement : tagElements)
      {
         if (tagName.equals(tagElement.getTagName()))
            tags.add(new JavaDocTagImpl(tagElement));
      }
      return tags;
   }

   @Override
   public JavaDocSource<O> setFullText(String text)
   {
      javadoc.tags().clear();
      setText(text);
      return this;
   }

   @Override
   public JavaDocSource<O> setText(String text)
   {
      TagElement tagElement = null;
      List<TagElement> tags = javadoc.tags();
      for (TagElement tagElementItem : tags)
      {
         if (tagElementItem.getTagName() == null)
         {
            tagElement = tagElementItem;
            break;
         }
      }
      if (tagElement == null)
      {
         tagElement = javadoc.getAST().newTagElement();
         javadoc.tags().add(0, tagElement);
      }
      tagElement.fragments().clear();

      TextElement textElement = javadoc.getAST().newTextElement();
      textElement.setText(text);
      tagElement.fragments().add(textElement);

      return this;
   }

   @Override
   public JavaDocSource<O> addTagValue(String tagName, String tagValue)
   {
      requireNonNull(tagName, TAG_NAME_CANNOT_BE_NULL);
      TagElement tagElement = javadoc.getAST().newTagElement();
      TextElement textElement = javadoc.getAST().newTextElement();

      tagElement.setTagName(tagName);
      textElement.setText(tagValue);

      tagElement.fragments().add(textElement);
      javadoc.tags().add(tagElement);

      return this;
   }

   @Override
   public JavaDocSource<O> addTagValue(JavaDocTag tag)
   {
      addTagValue(tag.getName(), tag.getValue());
      return this;
   }

   @Override
   public JavaDocSource<O> removeTags(String tagName)
   {
      requireNonNull(tagName, TAG_NAME_CANNOT_BE_NULL);
      List<TagElement> tags = javadoc.tags();
      Iterator<TagElement> iterator = tags.iterator();
      while (iterator.hasNext())
      {
         TagElement next = iterator.next();
         if (tagName.equals(next.getTagName()))
            iterator.remove();
      }
      return this;
   }

   @Override
   public JavaDocSource<O> removeTag(JavaDocTag tag)
   {
      List<TagElement> tags = javadoc.tags();
      tags.remove(tag.getInternal());
      return this;
   }

   @Override
   public JavaDocSource<O> removeAllTags()
   {
      List<TagElement> tags = javadoc.tags();
      Iterator<TagElement> iterator = tags.iterator();
      while (iterator.hasNext())
      {
         TagElement next = iterator.next();
         if (next.getTagName() != null)
            iterator.remove();
      }
      return this;
   }

}
