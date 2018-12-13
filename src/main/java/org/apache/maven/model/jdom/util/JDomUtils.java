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

import static java.lang.Math.max;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Parent;
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
     * Inserts a new child element to the given root element. The position where the element is inserted is calculated
     * using the element order that is defined in the {@link JDomCfg} (see {@link JDomCfg#getElementOrder()}). In the
     * root element, the new element is always prepended by a text element containing a linebreak followed by the
     * indentation characters. The indentation characters are (tried to be) detected from the root element (see {@link
     * #detectIndentation(Element)} ).
     *
     * @param name the name of the new element.
     * @param root the root element.
     * @return the new element.
     */
    public static Element insertNewElement( String name, Element root )
    {
        Element newElement;

        String indent = detectIndentation( root );

        newElement = new Element( name, root.getNamespace() );
        newElement.addContent( "\n" + indent );
        int newElementIndex = calcNewElementIndex( name, root );
        root.addContent( newElementIndex, newElement );

        if ( isBlankLineBeforeElement( name ) )
        {
            root.addContent( newElementIndex, new Text( "\n\n" + indent ) );
        }
        else
        {
            root.addContent( newElementIndex, new Text( "\n" + indent ) );
        }

        return newElement;
    }

    private static int calcNewElementIndex( String name, Element root )
    {
        int addIndex = 0;

        List<String> elementOrder = JDomCfg.getInstance().getElementOrder();

        for ( int i = elementOrder.indexOf( name ) - 1; i >= 0; i-- )
        {
            String addAfterElementName = elementOrder.get( i );
            if ( !addAfterElementName.equals( "" ) )
            {
                Element addAfterElement = root.getChild( addAfterElementName, root.getNamespace() );
                if ( addAfterElement != null )
                {
                    addIndex = root.indexOf( addAfterElement ) + 1;
                    break;
                }
            }
        }

        return addIndex;
    }

    private static boolean isBlankLineBeforeElement( String name )
    {
        List<String> elementOrder = JDomCfg.getInstance().getElementOrder();
        return elementOrder.get( elementOrder.indexOf( name ) - 1 ).equals( "" );
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
        String indent = null;

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
                    indent = indentCandidate;
                    break;
                }
                else
                {
                    indentCandidate = null;
                }
            }
        }

        if ( indent == null )
        {
            Parent parent = element.getParent();
            if ( parent instanceof Element )
            {
                indent = detectIndentation( (Element) parent ) + "  ";
            }
            else
            {
                return "";
            }
        }

        return indent;
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
                tagElement = element;
                root.addContent(
                    max( 0, root.getContentSize() - 1 ),
                    asList(
                        new Text( "\n" + detectIndentation( root ) ),
                        element ) );
            }
        }
        return tagElement;
    }

    /**
     * Transform the list of elements to a corresponding list of element texts.
     *
     * @param elements the list of elements.
     * @return the list of element texts.
     */
    public static List<String> toElementTextList( List<Element> elements )
    {
        List<String> elementTextList = new ArrayList<>( elements.size() );
        for ( Element element : elements )
        {
            elementTextList.add( element.getTextTrim() );
        }
        return elementTextList;
    }
}
