package com.codeaffine.fop.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;


@Path( "/pdf" )
public class PdfService implements StreamingOutput {

  private FopService fopService;

  @GET
  @Produces( "application/pdf" )
  public Response print() {
    ResponseBuilder builder = Response.ok( this );
    builder.header( "Content-Disposition", 
                    "attachment; filename=project-team-" + LocalDateTime.now() + ".pdf" );
    return builder.build();
  }

  @Override
  public void write( OutputStream output ) throws IOException, WebApplicationException {
    InputStream input = getClass().getResourceAsStream( "projectteam.xml" );
    InputStream stylesheet = getClass().getResourceAsStream( "projectteam2fo.xsl" );
    fopService.format( input, output, stylesheet );
  }
  
  public void setFopService( FopService fopService ) {
    this.fopService = fopService;
  }
}