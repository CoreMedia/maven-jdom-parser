package org.apache.maven.model.jdom;

/*
 * Copyright 2018 CoreMedia AG, Hamburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

import org.apache.maven.model.ActivationProperty;
import org.jdom2.Element;

/**
 * JDOM implementation of POMs {@code property} element.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomActivationProperty extends ActivationProperty
{
    private static final String ELEMENT_NAME = "name";
    private static final String ELEMENT_VALUE = "value";

    private final Element jdomElement;

    public JDomActivationProperty( Element element )
    {
        jdomElement = element;

        super.setName( getChildElementTextTrim( ELEMENT_NAME, jdomElement ) );
        super.setValue( getChildElementTextTrim( ELEMENT_VALUE, jdomElement ) );
    }

    @Override
    public void setName( String name )
    {
        rewriteElement( ELEMENT_NAME, name, jdomElement );
        super.setName( name );
    }

    @Override
    public void setValue( String value )
    {
        rewriteElement( ELEMENT_VALUE, value, jdomElement );
        super.setValue( value );
    }
}
