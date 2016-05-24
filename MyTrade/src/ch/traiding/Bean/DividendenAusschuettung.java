package ch.traiding.Bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "ausschuettung")
@ViewScoped
public class DividendenAusschuettung {

	public void ausschuetten() {
		System.out.println("Wird ausgeschüttet");
	}

	public void confirmDividendenAusschuettung() {
		addMessage("Dividende", "Die Dividende wurde erfolgreich ausgeschüttet");
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
