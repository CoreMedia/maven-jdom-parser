package org.apache.maven.model.jdom.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Iterator;

import org.codehaus.plexus.util.StringUtils;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Text;

/**
 * Common JDom functions
 *
 * @author Robert Scholte
 * @since 3.0
 */
public final class JDomUtils
{

    private JDomUtils()
    {
        // noop
    }

    /**
     * Tries to detect the indentation that is used within the given element and returns it.
     * <p/>
     * The method actually returns all characters (supposed to be whitespaces) that occur after the last linebreak in a
     * text element that is followed by an XML element.
     *
     * @param element the element whose contents should be used to detect the indentation.
     * @return the detected indentation or {@code null} if not indentation can be detected.
     */
    public static String detectIndentation( Element element )
    {
        String indentCandidate = null;

        for ( Content childElement : element.getContent() )
        {
            if ( childElement instanceof Text )
            {
                String text = ( (Text) childElement ).getText();
                int lastLsIndex = StringUtils.lastIndexOfAny( text, new String[]{"\n", "\r"} );
                if ( lastLsIndex > -1 )
                {
                    indentCandidate = text.substring( lastLsIndex + 1 );
                }
            }
            else if ( indentCandidate != null )
            {
                if ( childElement instanceof Element )
                {
                    break;
                }
                else
                {
                    indentCandidate = null;
                }
            }
        }

        return indentCandidate;
    }

    /**
     * Returns the given elements child element with the specified name.
     *
     * @param name   the name of the child element.
     * @param parent the parent of the requested element - must not be {@code null}.
     * @return the requested element or {@code null}.
     */
    public static Element getChildElement( String name, Element parent )
    {
        return parent.getChild( name, parent.getNamespace() );
    }

    /**
     * Returns the trimmed text value of the given elements child element with the specified name.
     *
     * @param name   the name of the child element.
     * @param parent the parent of the element whose text value is requested - must not be {@code null}.
     * @return the trimmed text value of the element or {@code null}.
     */
    public static String getChildElementTextTrim( String name, Element parent )
    {
        Element child = getChildElement( name, parent );
        if ( child == null )
        {
            return null;
        }
        else
        {
            String text = child.getTextTrim();
            if ( "null".equals( text ) )
            {
                return null;
            }
            else
            {
                return text;
            }
        }
    }

    /**
     * Updates the text value of the given element. The primary purpose of this method is to preserve any whitespace and
     * comments around the original text value.
     *
     * @param element The element to update, must not be <code>null</code>.
     * @param value   The text string to set, must not be <code>null</code>.
     */
    public static void rewriteValue( Element element, String value )
    {
        Text text = null;
        if ( element.getContent() != null )
        {
            for ( Iterator<?> it = element.getContent().iterator(); it.hasNext(); )
            {
                Object content = it.next();
                if ( ( content instanceof Text ) && ( (Text) content ).getTextTrim().length() > 0 )
                {
                    text = (Text) content;
                    while ( it.hasNext() )
                    {
                        content = it.next();
                        if ( content instanceof Text )
                        {
                            text.append( (Text) content );
                            it.remove();
                        }
                        else
                        {
                            break;
                        }
                    }
                    break;
                }
            }
        }
        if ( text == null )
        {
            element.addContent( value );
        }
        else
        {
            String chars = text.getText();
            String trimmed = text.getTextTrim();
            int idx = chars.indexOf( trimmed );
            String leadingWhitespace = chars.substring( 0, idx );
            String trailingWhitespace = chars.substring( idx + trimmed.length() );
            text.setText( leadingWhitespace + value + trailingWhitespace );
        }
    }

    public static Element rewriteElement( String name, String value, Element root, Namespace namespace )
    {
        Element tagElement = root.getChild( name, namespace );
        if ( tagElement != null )
        {
            if ( value != null )
            {
                rewriteValue( tagElement, value );
            }
            else
            {
                int index = root.indexOf( tagElement );
                root.removeContent( index );
                for ( int i = index - 1; i >= 0; i-- )
                {
                    if ( root.getContent( i ) instanceof Text )
                    {
                        root.removeContent( i );
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            if ( value != null )
            {
                Element element = new Element( name, namespace );
                element.setText( value );
                root.addContent( "  " ).addContent( element ).addContent( "\n  " );
                tagElement = element;
            }
        }
        return tagElement;
    }


}
