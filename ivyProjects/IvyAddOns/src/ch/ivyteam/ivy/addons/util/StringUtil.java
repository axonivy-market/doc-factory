package ch.ivyteam.ivy.addons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.util.NumberUtil;

/**
 * String utilities.
 * 
 * @author Patrick Joly, TI-Informatique
 * @since 23.10.2008
 */
public final class StringUtil
{
  private StringUtil()
  {
  }

  /**
   * Cleans up a string as it was a unix fs path.<br />
   * <code>/root/a/b/../c</code> becomes <code>/root/a/c</code>
   * 
   * @param fullPath path to clean up
   * @return cleaned up path
   */
  public static String cleanUpPath(String fullPath)
  {
    List<String> parts;
    int index;

    // new ArrayList is present to avoid UnsupportedOperationException when
    // remove() is invoked because asList create a fixed size list.
    parts = new ArrayList<String>(java.util.Arrays.asList(fullPath.split("/")));

    index = 0;
    while (index < parts.size())
    {
      if (parts.get(index).equals("."))
      {
        parts.remove(index);
      }
      else if (parts.get(index).equals(".."))
      {
        parts.remove(index);
        if (index == 0)
        {
          break;
        }
        else
        {
          index--;
          parts.remove(index);
        }
      }
      else
      {
        index++;
      }
    }

    return StringUtil.join(parts, "/");
  }

  /**
   * Joins different string parts with a specific string. <code>
   * list = {"a", "b", "c"}
   * separator = ";"
   * result = "a;b;c"
   * </code>
   * 
   * @param list item to join
   * @param separator string to put between two items
   * @return joined string
   */
  public static String join(List<String> list, String separator)
  {
    String usedSeparator;

    usedSeparator = separator;

    if (list == null)
    {
      return "";
    }
    if (usedSeparator == null)
    {
      usedSeparator = "";
    }

    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < list.size(); i++)
    {
      buf.append(list.get(i));
      if (i < list.size() - 1)
      {
        buf.append(usedSeparator);
      }
    }

    return buf.toString();
  }

  /**
   * Replaces in the source string the keys given by the hashtable by their values. The keys can be framed in
   * the source string with markers. <br />
   * If the key is "test" and both markers are "%", the string "%test%" is searched and replaced.
   * 
   * @param source string that should be modified
   * @param hash values to subst
   * @param startMarker begin mark of the string to replace
   * @param endMarker end mark of the string to replace
   * @return new string with substitutions done
   */
  public static String substitute(String source, Map<String, String> hash, String startMarker,
          String endMarker)
  {
    String result;

    result = source;

    if (hash != null)
    {
      for (String key : hash.keySet())
      {
        result = result.replaceAll(startMarker + key + endMarker, hash.get(key));
      }
    }

    return result;
  }

  /**
   * Builds the relative path separing 2 absolute pathes.
   * 
   * @param from absolute path from where the relative path should be found
   * @param to absolute path to where the relative path should point
   * @param separator string used between two items
   * @return relative path to go from <code>from</code> to <code>to</code>
   */
  public static String buildRelativePath(String from, String to, String separator)
  {
    StringBuffer result;
    String[] splitedFrom;
    String[] splitedTo;
    int min;
    int count;

    splitedFrom = from.split(separator);
    splitedTo = to.split(separator);

    min = splitedFrom.length;
    min = min < splitedTo.length ? min : splitedTo.length;

    // Finds the common part
    for (count = 0; count < min; count++)
    {
      if (!splitedFrom[count].equals(splitedTo[count]))
      {
        break;
      }
    }

    result = new StringBuffer();

    // Finds relative path to the common part
    for (int i = 0; i < splitedFrom.length - count; i++)
    {
      result.append("..");
      result.append(separator);
    }

    // Add relative part from the common part
    for (int i = count; i < splitedTo.length; i++)
    {
      result.append(splitedTo[i]);
      if (i < splitedTo.length - 1)
      {
        result.append(separator);
      }
    }
    return result.toString();
  }

  public static boolean toBoolean(String value, boolean defaultValue)
  {
    boolean result;
    if (defaultValue)
    {
      result = value.equals("") || value.equals("1");
    }
    else
    {
      result = value.equals("1");
    }
    return result;
  }

  public static Number toNumber(String value, Number defaultValue)
  {
    Number result;
    result = defaultValue;

    if (value != null && !"".equals(value))
    {
      try
      {
        result = NumberUtil.parseNumber(value);
      }
      catch (NumberFormatException e)
      {
        // Nothing to do
      }
    }

    return result;
  }

  /**
   * Resolves the global vars references contained in a string. Every <code>%var_name%</code> contained into
   * the string are replaced by the value defined for the global var that have the name given between
   * <code>%</code>.<br />
   * When the asked global var doesn't exist nothing is done.
   * @param value the string template
   * @return a string where global vars are resolved
   */
  public static String resolveGlobalVars(String value)
  {
    if (value.contains("%"))
    {
      Pattern p;

      p = Pattern.compile("%(.*?)(?=%)");
      Matcher m = p.matcher(value);

      while (m.find())
      {
        try
        {
          if (Ivy.var().get(m.group(1)) != null)
          {
            value = value.replaceFirst("%" + m.group(1) + "%", Ivy.var().get(m.group(1)));
            m = p.matcher(value);
          }
        }
        catch (Exception e)
        {
          // Nothing to do
        }
      }
    }
    return value;
  }
}
