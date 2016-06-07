package com.drillinginfo.repair;

import java.util.ArrayList;
import java.util.List;

import com.drillinginfo.repair.repairs.SuccessfulTestRepair;
import com.drillinginfo.repair.repairs.FailedTestRepair;
import com.drillinginfo.repair.repairs.NeverNeededTestRepair;
import com.drillinginfo.repair.repairs.SlowToRunTestRepair;

public enum DataRepairs
{
  TEST_REPAIR_1(SuccessfulTestRepair.class, "Successful Test Repair"),
  TEST_REPAIR_2(FailedTestRepair.class, "Failing Test Repair"),
  TEST_REPAIR_3(NeverNeededTestRepair.class, "Never Needed Test Repair"),
  TEST_REPAIR_4(SlowToRunTestRepair.class, "Slow To Run Test Repair");
  
  // TODO add versioning
  
  public static List<String> getAllRepairNames()
  {
    List<String> result = new ArrayList<>();
    for (DataRepairs repair : DataRepairs.values())
    {
      if (!result.contains(repair.getDisplayName()))
      {
        result.add(repair.getDisplayName());
      }
    }
    return result;
  }
  
  public static DataRepairs getEnumForName(final String displayName)
  {
    if (displayName != null && !displayName.isEmpty())
    {
      for (DataRepairs repair : DataRepairs.values())
      {
        if (repair.getDisplayName().equals(displayName))
        {
          return repair;
        }
      }
    }
    return null;
  }
  
  public static DataRepair getRepair(final DataRepairs repair)
  {
    if (repair != null)
    {
      return repair.createDataRepair();
    }
    return null;
  }
  
  public static DataRepair getRepairForName(final String displayName)
  {
    return getRepair(getEnumForName(displayName));
  }
  
  public String getDisplayName()
  {
    return _name;
  }
  
  public DataRepair createDataRepair()
  {
    try
    {
      return _repair.newInstance();
    } catch (InstantiationException | IllegalAccessException e)
    {
      // TODO move to logger
      e.printStackTrace();
    }
    return null;
  }
  
  private final Class<? extends DataRepair> _repair;
  private final String _name;
  
  private DataRepairs(final Class<? extends DataRepair> repair, 
      final String displayName)
  {
    _repair = repair;
    _name = displayName;
  }
}
