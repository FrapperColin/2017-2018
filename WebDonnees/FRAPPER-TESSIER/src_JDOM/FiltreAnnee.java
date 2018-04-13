import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.filter.AbstractFilter;
import org.jdom.filter.Filter;

public class FiltreAnnee extends AbstractFilter {
	private String annee;
	public FiltreAnnee(String annee) {super();this.annee = annee;}
	public boolean matches(Object o) 
	{
		boolean ok = false;
		Element e = (Element) o;
		Attribute a = e.getAttribute("watertype");
		if (a != null)
		{
			ok = a.getValue().equals(annee);
		}
		return ok;
	}
}