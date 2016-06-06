package com.drillinginfo.rest;

import com.drillinginfo.rest.control.RESTServer;

/**
 * Launcher for REST http server.
 */
public final class DIServer
{
  public static void main(String... args) {
    RESTServer.initialize();
    
    System.out.println("Drillinginfo REST server started");

    while (RESTServer.isRunning()) 
    {
      try {Thread.sleep(1000);} catch (InterruptedException ignore) { }
    }
    
    System.out.println("Drillinginfo REST server stopped");
  }
}
