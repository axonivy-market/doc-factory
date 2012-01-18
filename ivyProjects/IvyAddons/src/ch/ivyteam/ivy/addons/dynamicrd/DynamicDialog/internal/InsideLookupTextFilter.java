package ch.ivyteam.ivy.addons.dynamicrd.DynamicDialog.internal;

import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.canoo.ulc.community.lookuptextfield.application.filter.AbstractRegExpStringFilter;

/**
 * Lookup text field filter that searches at the begin of each words of the string.
 * 
 * @author Patrick Joly, TI-Informatique
 * @since 15.09.2008
 */
public final class InsideLookupTextFilter extends AbstractRegExpStringFilter
{
  /**
   * Constructs a new InsideLookupTextFilter object.
   */
  public InsideLookupTextFilter()
  {
    super(Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.SINGLELINE_MASK);
  }

  protected PatternCompiler createCompiler()
  {
    return new Perl5Compiler();
  }

  protected PatternMatcher createMatcher()
  {
    return new Perl5Matcher();
  }

  @Override
  public void setPattern(String pattern)
  {
    String usedPattern;
    
    usedPattern = (pattern != null) ? pattern : "";
    String perl5pattern = Perl5Compiler.quotemeta(usedPattern);
    super.setPattern("\\b" + perl5pattern);
  }
}