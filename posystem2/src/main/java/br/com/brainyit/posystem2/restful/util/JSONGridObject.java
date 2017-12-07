/**
 * 
 */
package br.com.brainyit.posystem2.restful.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;

/**
 * @author rafael
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class JSONGridObject {
	
	@XmlElement
	private String identifier;
	
	@XmlElement
	private String label;
	
	@XmlElement
	private JSONArray items;

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the items
	 */
	public JSONArray getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(JSONArray items) {
		this.items = items;
	}

	/* (non-Javadoc)
	 * @see org.codehaus.jettison.json.JSONObject#toString()
	 */
	@Override
	public String toString() {
        try {
            StringBuffer sb = new StringBuffer("{");
            sb.append("identifier:");
            sb.append("'");
            sb.append(this.identifier);
            sb.append("'");
            sb.append(",");
            sb.append("label:");
            sb.append("'");
            sb.append(this.label);
            sb.append("'");
            sb.append(",");
            sb.append("items:");
            sb.append(this.items.toString());
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
	}
}
