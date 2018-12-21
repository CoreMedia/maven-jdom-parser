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
     * using the element order that is defined in the {@link JDomCfg} (see {@link JDomCfg#getElementOrder(String)}). In
     * the root element, the new element is always prepended by a text element containing a linebreak followed by the
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

        if ( isBlankLineBeforeElement( name, root ) )
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

        List<String> elementOrder = JDomCfg.getInstance().getElementOrder( root.getName() );
        if ( elementOrder == null )
        {
            addIndex = max( 0, root.getContentSize() - 1 );
        }
        else
        {
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
        }

        return addIndex;
    }

    private static boolean isBlankLineBeforeElement( String name, Element root )
    {
        List<String> elementOrder = JDomCfg.getInstance().getElementOrder( root.getName() );
        return elementOrder != null && elementOrder.get( max( 0, elementOrder.indexOf( name ) - 1 ) ).equals( "" );
    }

    /**
     * Tries to detect the indentation that is used within the given element and returns it.
     * <p>
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
     * Remove a child element from the parent.
     *
     * @param parent      the parent element.
     * @param removeChild the child element to be removed.
     */
    public static void removeChildElement( Element parent, Element removeChild )
    {
        int index = parent.indexOf( removeChild );
        parent.removeContent( index-- );
        if ( index >= 0 && parent.getContent( index ) instanceof Text )
        {
            // Remove prepending whitespaces (linebreaks and indentation)
            parent.removeContent( index );
        }
    }

    /**
     * Resets the XML indentations of an element.
     *
     * @param element the element whose indentations should be reset.
     * @param indent  the indentation to be used.
     */
    public static void resetIndentations( Element element, String indent )
    {
        List<Content> childContents = element.getContent();
        for ( int i = 1; i < childContents.size(); i++ )
        {
            Content childContent = childContents.get( i );
            if ( childContent instanceof Element )
            {
                Element childElement = (Element) childContent;

                // Reset indentations of child elements.
                resetIndentation( childContents.get( i - 1 ), indent );

                // Reset indentations of before closing tags of child elements.
                List<Content> grandChildElements = childElement.getContent();
                if ( grandChildElements.size() > 1 )
                {
                    resetIndentation( grandChildElements.get( grandChildElements.size() - 1 ), indent );
                }
            }
        }
    }

    private static void resetIndentation( Content whitespaceContentBeforeElement, String indent )
    {
        if ( whitespaceContentBeforeElement instanceof Text )
        {
            Text whitespaceTextContent = (Text) whitespaceContentBeforeElement;
            String whitespaces = whitespaceTextContent.getText();
            int lastLsIndex = StringUtils.lastIndexOfAny( whitespaces, new String[]{"\n", "\r"} );
            whitespaceTextContent.setText( "\n" + whitespaces.substring( 0, lastLsIndex ) + indent );
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
                Element element = insertNewElement( name, root );
                element.setText( value );
                tagElement = element;
            }
        }
        return tagElement;
    }
}
