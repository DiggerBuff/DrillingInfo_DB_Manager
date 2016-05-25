package com.deh.b2r.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddressBook
{
	  private List<SharedRep.Address> streets = new ArrayList<>();
	  
	  public AddressBook()
	  {
		  //System.out.println("Init ran");
		  try {
			  	File file = new File("AddressBook.txt");
			  	if(!file.exists()) {
			  	    file.createNewFile();
			  	} 
	            FileReader reader = new FileReader(file);
	            BufferedReader bufferedReader = new BufferedReader(reader);
	 
	            String line;
	 
	            while ((line = bufferedReader.readLine()) != null) {
	                SharedRep.Address add = new SharedRep.Address(line);
	                streets.add(add);
	            }
	            reader.close();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  }
	  
	  public void addStreet(SharedRep.Address street)
	  {
		  //System.out.println("Add ran");
		  try {
	            
            	FileWriter writer = new FileWriter("AddressBook.txt", true);
            	BufferedWriter bufferedWriter = new BufferedWriter(writer);
                streets.add(street);
                
            	bufferedWriter.write(street.toString());
            	bufferedWriter.newLine();
            	bufferedWriter.close();
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  }
	  
	  public List<SharedRep.Address> getStreets()
	  {
		  //System.out.println("Get ran");
		  List<SharedRep.Address> temp = new ArrayList<>(streets);
		  return temp;
	  }
}