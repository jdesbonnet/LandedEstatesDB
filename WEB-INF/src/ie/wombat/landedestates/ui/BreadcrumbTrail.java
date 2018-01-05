package ie.wombat.landedestates.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Make Bootstrap compatible breadcrumb trail HTML for UI.
 * 
 * @author Joe Desbonnet
 *
 */
public class BreadcrumbTrail {
	
	private List<Crumb> crumbs = new ArrayList<>();
	
	/**
	 * Add crumb. Allow for method chaining.
	 * @param label
	 * @param url
	 * @return
	 */
	public BreadcrumbTrail add(String label, String url) {
		crumbs.add(new Crumb(label,url));
		return this;
	}
	
	/**
	 * Add crumb with no URL. Allow for method chaining.
	 * @param label
	 * @return
	 */
	public BreadcrumbTrail add(String label) {
		crumbs.add(new Crumb(label,null));
		return this;
	}
	
	/**
	 * Add crumb with URL. 
	 * @param label
	 * @param url
	 */
	public void addCrumb (String label, String url) {
		crumbs.add(new Crumb(label,url));
	}

	/**
	 * Add crumb with no URL.
	 * @param label
	 * @return
	 */
	public void addCrumb (String label) {
		crumbs.add(new Crumb(label,null));
	}
	
	/**
	 * Clear any added breadcrumbs.
	 */
	public BreadcrumbTrail clear() {
		crumbs.clear();
		return this;
	}
	
	public String getHtml() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("<nav aria-label='breadcrumb'>");
		buf.append("<ul class='breadcrumb'>\n");
		
		for (Crumb crumb : crumbs) {
			buf.append("<li class='breadcrumb-item'>");
			if (crumb.url != null ) {
				buf.append("<a href='" + crumb.url + "'>");
				buf.append(crumb.label);
				buf.append("</a>");
			} else {
				buf.append(crumb.label);
			}
			buf.append("</li>\n");
		}
		
		buf.append("</ul></nav>\n");
		
		return buf.toString();
	}
	
	public String toString() {
		return getHtml();
	}
	
	private static class Crumb {
		public String label=null;
		public String url=null;
		public Crumb (String label, String url) {
			this.label = label;
			this.url = url;
		}
	}
}
