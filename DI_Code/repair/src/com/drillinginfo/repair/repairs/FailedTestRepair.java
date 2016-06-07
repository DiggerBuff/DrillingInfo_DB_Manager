package com.drillinginfo.repair.repairs;

import com.drillinginfo.repair.DataRepair;

/**
 * Sample repair for testing.
 */
public class FailedTestRepair implements DataRepair
{
  @Override
  public String getSummary()
  {
    return "Test repair that always detects true and repairs false.";
  }
  
  @Override
  public boolean detect()
  {
    return true;
  }

  @Override
  public boolean repair()
  {
    return false;
  }
}
