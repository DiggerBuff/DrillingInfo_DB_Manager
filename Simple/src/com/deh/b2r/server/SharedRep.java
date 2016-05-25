package com.deh.b2r.server;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SharedRep
{
  public static final class Address
  {
    @JsonProperty("street")
    public final String street;

    public Address(@JsonProperty("street") String street) {
      this.street = street;
    }

    public String toString() {
      return street;
    }
  }
  
  
}
