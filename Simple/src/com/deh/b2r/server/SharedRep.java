package com.deh.b2r.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

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

		@Override
		public boolean equals(Object object){
			if(object == null){
				return false;
			}else if(!(object instanceof Address)){
				return false;
			}else {
				Address address = (Address)object;
				if(street.equals(address.street)){
					return true;
				}			
			}
			return false;
		}	

		public static List<Address> getAllStreets(){
			List<Address> streets = null;
			try {
				File file = new File("Streets.dat");
				if (!file.exists()) {
					Address street = new Address("Default Street");
					streets = new ArrayList<>();
					streets.add(street);
					saveStreetList(streets);		
				}
				else{
					FileInputStream fis = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fis);
					streets = (List<Address>) ois.readObject();
					ois.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}		
			return streets;
		}

		private static void saveStreetList(List<Address> streets) {
			try {
				File file = new File("Streets.dat");
				FileOutputStream fos;

				fos = new FileOutputStream(file);

				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(streets);
				oos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
