package org.apache.maven.model.jdom.etl;

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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.model.Model;
import org.apache.maven.model.jdom.JDomModel;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.WriterFactory;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * JDom implementation for extracting, transform, loading the Model (pom.xml)
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomModelETL implements ModelETL
{
    private ModelETLRequest modelETLRequest = new ModelETLRequest();

    private Document document;

    private String intro = null;
    private String outtro = null;

    @Override
    public void extract( File pomFile ) throws IOException, JDOMException
    {
        String content = readXmlFile( pomFile, modelETLRequest.getLineSeparator() );
        // we need to eliminate any extra whitespace inside elements, as JDOM will nuke it
        content = content.replaceAll( "<([^!][^>]*?)\\s{2,}([^>]*?)>", "<$1 $2>" );
        content = content.replaceAll( "(\\s{2,})/>", "$1 />" );

        SAXBuilder builder = new SAXBuilder();
        document = builder.build( new StringReader( content ) );

        // Normalize line endings to platform's style (XML processors like JDOM normalize line endings to "\n" as
        // per section 2.11 of the XML spec)
        normaliseLineEndings( document );

        // rewrite DOM as a string to find differences, since text outside the root element is not tracked
        StringWriter w = new StringWriter();
        printDocumentToWriter( document, w, modelETLRequest.getLineSeparator() );

        int index = content.indexOf( w.toString() );
        if ( index >= 0 )
        {
            intro = content.substring( 0, index );
            outtro = content.substring( index + w.toString().length() );
        }
        else
        {
            /*
             * NOTE: Due to whitespace, attribute reordering or entity expansion the above indexOf test can easily
             * fail. So let's try harder. Maybe some day, when JDOM offers a StaxBuilder and this builder employes
             * XMLInputFactory2.P_REPORT_PROLOG_WHITESPACE, this whole mess can be avoided.
             */
            // CHECKSTYLE_OFF: LocalFinalVariableName
            final String SPACE = "\\s++";
            final String XML = "<\\?(?:(?:[^\"'>]++)|(?:\"[^\"]*+\")|(?:'[^\']*+'))*+>";
            final String INTSUB = "\\[(?:(?:[^\"'\\]]++)|(?:\"[^\"]*+\")|(?:'[^\']*+'))*+\\]";
            final String DOCTYPE =
                "<!DOCTYPE(?:(?:[^\"'\\[>]++)|(?:\"[^\"]*+\")|(?:'[^\']*+')|(?:" + INTSUB + "))*+>";
            final String PI = XML;
            final String COMMENT = "<!--(?:[^-]|(?:-[^-]))*+-->";

            final String INTRO =
                "(?:(?:" + SPACE + ")|(?:" + XML + ")|(?:" + DOCTYPE + ")|(?:" + COMMENT + ")|(?:" + PI + "))*";
            final String OUTRO = "(?:(?:" + SPACE + ")|(?:" + COMMENT + ")|(?:" + PI + "))*";
            final String POM = "(?s)(" + INTRO + ")(.*?)(" + OUTRO + ")";
            // CHECKSTYLE_ON: LocalFinalVariableName

            Matcher matcher = Pattern.compile( POM ).matcher( content );
            if ( matcher.matches() )
            {
                intro = matcher.group( 1 );
                outtro = matcher.group( matcher.groupCount() );
            }
        }
    }

    @Override
    public void transform()
    {

    }

    @Override
    public void load( File targetFile ) throws IOException
    {
        writePom( targetFile );
    }

    @Override
    public Model getModel()
    {
        return new JDomModel( document );
    }

    private void normaliseLineEndings( Document document )
    {
        for ( Iterator<?> i = document.getDescendants( new ContentFilter( ContentFilter.COMMENT ) ); i.hasNext(); )
        {
            Comment c = (Comment) i.next();
            c.setText( normalizeLineEndings( c.getText(), modelETLRequest.getLineSeparator() ) );
        }
        for ( Iterator<?> i = document.getDescendants( new ContentFilter( ContentFilter.CDATA ) ); i.hasNext(); )
        {
            CDATA c = (CDATA) i.next();
            c.setText( normalizeLineEndings( c.getText(), modelETLRequest.getLineSeparator() ) );
        }
    }

    private void writePom( File pomFile ) throws IOException
    {
        Element rootElement = document.getRootElement();

        if ( modelETLRequest.isAddSchema() )
        {
            String modelVersion = modelETLRequest.getProject().getModelVersion();
            Namespace pomNamespace = Namespace.getNamespace( "", "http://maven.apache.org/POM/" + modelVersion );
            rootElement.setNamespace( pomNamespace );
            Namespace xsiNamespace = Namespace.getNamespace( "xsi", "http://www.w3.org/2001/XMLSchema-instance" );
            rootElement.addNamespaceDeclaration( xsiNamespace );

            if ( rootElement.getAttribute( "schemaLocation", xsiNamespace ) == null )
            {
                rootElement.setAttribute( "schemaLocation", "http://maven.apache.org/POM/" + modelVersion
                    + " http://maven.apache.org/maven-v" + modelVersion.replace( '.', '_' ) + ".xsd", xsiNamespace );
            }

            // the empty namespace is considered equal to the POM namespace, so match them up to avoid extra xmlns=""
            ElementFilter elementFilter = new ElementFilter( Namespace.getNamespace( "" ) );
            for ( Iterator<?> i = rootElement.getDescendants( elementFilter ); i.hasNext(); )
            {
                Element e = (Element) i.next();
                e.setNamespace( pomNamespace );
            }
        }

        
        try ( Writer writer = WriterFactory.newXmlWriter( pomFile ) )
        {
            if ( intro != null )
            {
                writer.write( intro );
            }

            printDocumentToWriter( document, writer, modelETLRequest.getLineSeparator() );

            if ( outtro != null )
            {
                writer.write( outtro );
            }
        }
    }

    private static void printDocumentToWriter( Document document, Writer writer, String lineSeparator )
        throws IOException
    {
        Format format = Format.getRawFormat();
        format.setLineSeparator( lineSeparator );
        XMLOutputter out = new XMLOutputter( format );
        out.output( document.getRootElement(), writer );
    }

    /**
     * Gets the string contents of the specified XML file. Note: In contrast to an XML processor, the line separators in
     * the returned string will be normalized to use the platform's native line separator. This is basically to save
     * another normalization step when writing the string contents back to an XML file.
     * <p/>
     * Method was copied from
     * <a href="https://github.com/apache/maven-release/blob/9ea29a796d0e6191bebdf9accf4338fde541195d/maven-release-manager/src/main/java/org/apache/maven/shared/release/util/ReleaseUtil.java#L138">org.apache.maven.shared.release.util.ReleaseUtil#readXmlFile(java.io.File, java.lang.String)</a>
     *
     * @param file The path to the XML file to read in, must not be <code>null</code>.
     * @param ls   The line separator to be used for normalization.
     * @return The string contents of the XML file.
     * @throws IOException If the file could not be opened/read.
     */
    private static String readXmlFile( File file, String ls )
        throws IOException
    {
        try ( Reader reader = ReaderFactory.newXmlReader( file ) )
        {
            return normalizeLineEndings( IOUtil.toString( reader ), ls );
        }
    }

    /**
     * Normalizes the line separators in the specified string.
     * <p/>
     * Method was copied from
     * <a href="https://github.com/apache/maven-release/blob/9ea29a796d0e6191bebdf9accf4338fde541195d/maven-release-manager/src/main/java/org/apache/maven/shared/release/util/ReleaseUtil.java#L156">org.apache.maven.shared.release.util.ReleaseUtil#normalizeLineEndings(java.lang.String, java.lang.String)</a>
     *
     * @param text      The string to normalize, may be <code>null</code>.
     * @param separator The line separator to use for normalization, typically "\n" or "\r\n", must not be
     *                  <code>null</code>.
     * @return The input string with normalized line separators or <code>null</code> if the string was <code>null</code>
     *         .
     */
    private static String normalizeLineEndings( String text, String separator )
    {
        String norm = text;
        if ( text != null )
        {
            norm = text.replaceAll( "(\r\n)|(\n)|(\r)", separator );
        }
        return norm;
    }

    public void setModelETLRequest( ModelETLRequest modelETLRequest )
    {
        this.modelETLRequest = modelETLRequest;
    }
}
