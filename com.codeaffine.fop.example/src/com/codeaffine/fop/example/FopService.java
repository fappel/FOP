package com.codeaffine.fop.example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;


public class FopService {
  
  private final FopFactory fopFactory;

  public FopService() {
    this.fopFactory = FopFactory.newInstance();
  }
  
  public void format( InputStream input, OutputStream output, InputStream stylesheet  ) {
    OutputStream out = new BufferedOutputStream( output );
    try {
      doFormat( input, stylesheet, out );
    } catch( FOPException | TransformerException cause ) {
      throw new IllegalStateException( cause );
    } finally {
      flush( out );
    }
  }

  private void doFormat( InputStream input, InputStream stylesheet, OutputStream out )
    throws FOPException, TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException
  {
    Fop fop = fopFactory.newFop( MimeConstants.MIME_PDF, out );
    Result saxResult = new SAXResult( fop.getDefaultHandler() );
    Transformer transformer = createTransformer( stylesheet );
    transformer.transform( createStreamSource( input ), saxResult );
  }

  private Transformer createTransformer( InputStream stylesheet )
    throws TransformerFactoryConfigurationError, TransformerConfigurationException
  {
    return createTransformer( createStreamSource( stylesheet ) );
  }

  private Transformer createTransformer( StreamSource streamSource )
    throws TransformerFactoryConfigurationError, TransformerConfigurationException
  {
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer( streamSource );
    return transformer;
  }

  private static StreamSource createStreamSource( InputStream input ) {
    return new StreamSource( new BufferedInputStream( input ) );
  }
  
  private void flush( OutputStream out ) {
    try {
      out.flush();
    } catch( IOException ioe ) {
      throw new IllegalStateException( ioe );
    }
  }
}