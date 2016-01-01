/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.roaster.model.source;

/**
 * Provides location information about an element in the source file
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public interface LocationCapable
{
   /**
    * Returns the character index into the original source file indicating where the source fragment corresponding to
    * this element begins.
    * 
    * @return the 0-based character index, or -1 if no source position information is recorded for this element
    */
   int getStartPosition();

   /**
    * Returns the character index into the original source file indicating where the source fragment corresponding to
    * this element ends.
    *
    * @return a (possibly 0) length, or <code>0</code> if no source position information is recorded for this node
    */
   int getEndPosition();

   /**
    * Returns the line number corresponding to the given source character position in the original source string. The
    * initial line of the compilation unit is numbered 1, and each line extends through the last character of the
    * end-of-line delimiter. The very last line extends through the end of the source string and has no line delimiter.
    * For example, the source string <code>class A\n{\n}</code> has 3 lines corresponding to inclusive character ranges
    * [0,7], [8,9], and [10,10]. Returns -1 for a character position that does not correspond to any source line, or -2
    * if no line number information is available for this compilation unit.
    *
    * @return the 1-based line number, or <code>-1</code> if the character position does not correspond to a source line
    *         in the original source file or <code>-2</code> if line number information is not known for this
    *         compilation unit
    */
   int getLineNumber();

   /**
    * Returns the column number corresponding to the given source character position in the original source string.
    * Column number are zero-based. Return <code>-1</code> if it is beyond the valid range or <code>-2</code> if the
    * column number information is unknown.
    *
    * @return the 0-based column number, or <code>-1</code> if the character position does not correspond to a source
    *         line in the original source file or <code>-2</code> if column number information is unknown for this
    *         compilation unit
    */
   int getColumnNumber();

}
