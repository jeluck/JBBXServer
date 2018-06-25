package com.jbb.mgt.server.rs.formatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Provider
@Consumes({"application/*+json", "text/json"})
@Produces({"application/*+json", "text/json"})
public class JsonProvider extends JacksonJsonProvider {
	
}
