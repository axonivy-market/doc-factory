package ch.ivyteam.ivy.addons.util;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * This class generates unique ID's.
 *
 * @since 21.01.2010 part of IvyAddons
 */
public class GuidGenerator
{
  /** GUID random generator */
  private static Random rnd = new Random();
  
  /** The last produced GUID */
  private static String last = null;
  
  /** 16 chars long HEX pattern for GUID : [A-F0-9]{16} */
  private static Pattern guidPattern = Pattern.compile("[A-F0-9]{16}"); 
  
  /**
   * Initializes the GUID generator with the given seed.
   * @param seed random seed for the GUID generator
   */
  public static void init(long seed)
  {
    rnd.setSeed(seed);
  }

  /**
   * Generate a new unique id with 16 hex digits.
   * @return unique id of length 16
   */
  public static String generateID()
  {
    long id_millis;
    String id_part_time;
    String id_part_random;
    StringBuffer guid;

    id_millis = System.currentTimeMillis();
    id_part_time = Long.toHexString(id_millis).toUpperCase();
    id_part_random = Long.toHexString(rnd.nextInt(16777215)).toUpperCase();
    guid = new StringBuffer(id_part_time + id_part_random);

    if (guid.length() > 16)
    {
      guid.delete(16, guid.length());
    }
    while (guid.length() < 16)
    {
      guid.insert(0, "0");
    }    
    
    if (guid.toString().equals(last))
    {
      // try again
      return generateID();
    }
    
    last = guid.toString();
    return last;
  }

  /**
   * Maps the given GUID onto an int. Since GUID values are Hex-Numbers
   * of length 16 and max(Integer) is 0x7fffffff, this mapping is surjective,
   * i.e. collisions cannot be prevented.
   * 
   * <p>The algorithm simply takes the last 7 hex-digits of guid and returns
   * their integer value.
   * @param guid
   * @return integer value for given guid
   */
  public static int guidToInt(String guid)
  {
    if (! isGuid(guid))
      throw new IllegalArgumentException("<"+ guid + "> is not a guid (must have 16 hex chars)");
    
    guid = guid.substring(guid.length() - 7);
    return Integer.parseInt(guid, 16);
  }
  
  /**
   * Maps the given GUID onto a long. Since GUID values are Hex-Numbers
   * of length 16 and max(Long) is 0x7fffffffffffffff, this mapping is 
   * surjective, i.e. collisions are rare, but cannot be fully prevented.
   * 
   * <p>The algorithm simply takes the last 14 hex-digits of guid and returns
   * their long value.
   * @param guid
   * @return long value for given guid
   */
  public static long guidToLong(String guid)
  {
    if (! isGuid(guid))
      throw new IllegalArgumentException("<"+ guid + "> is not a guid (must have 16 hex chars)");
    
    guid = guid.substring(guid.length() - 15);
    return Long.parseLong(guid, 16);
  }
  
  /**
   * Tests if the string respresents a guid or not
   * @param string the string to test
   * @return true if string contains guid, otherwise false
   */
  public static boolean isGuid(String string)
  {
    if (string == null)
      return false;
    
    return guidPattern.matcher(string).matches();
  }
  
}
