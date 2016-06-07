package com.drillinginfo.repair.repairs;

import com.drillinginfo.repair.DataRepair;

/**
 * Sample repair for testing.
 */
public class SlowToRunTestRepair implements DataRepair
{
  @Override
  public String getSummary()
  {
    return "Test repair that always detects true and repairs true slowly.";
  }
  
  @Override
  public boolean detect()
  {
    return true;
  }

  @Override
  public boolean repair()
  {
    try
    {
      Thread.sleep(4000);
    }
    catch (InterruptedException ignore)
    {
      
    }
    return true;
  }

}
