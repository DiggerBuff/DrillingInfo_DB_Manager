package com.drillinginfo.repair.repairs;

import com.drillinginfo.repair.DataRepair;

/**
 * Sample repair for testing.
 */
public class NeverNeededTestRepair implements DataRepair
{
  @Override
  public String getSummary()
  {
    return "Test repair that always detects false.";
  }
  
  @Override
  public boolean detect()
  {
    return false;
  }

  @Override
  public boolean repair()
  {
    return false;
  }

}
