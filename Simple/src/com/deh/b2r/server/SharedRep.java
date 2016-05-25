package com.deh.b2r.server;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SharedRep
{
  public static final class Address
  {
	//Added overrides so that the comparison of Addresses works on the contents, not the object.
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		return result;
	}
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}

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
