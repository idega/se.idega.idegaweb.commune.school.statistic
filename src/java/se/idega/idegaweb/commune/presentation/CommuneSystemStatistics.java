package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

import se.idega.idegaweb.commune.account.citizen.data.CitizenAccount;
import se.idega.idegaweb.commune.account.citizen.data.CitizenAccountHome;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean;
import se.idega.idegaweb.commune.school.data.SchoolChoiceHome;

/**
 * @author gimmi
 */
public class CommuneSystemStatistics extends CommuneBlock {

	private String HEADER_KEY = "statistics_header";
	private String HEADER_DEFAULT = "Statistics";
	private String CITIZEN_ACOUNT_APPLICATIONS_KEY = "citizen_account_applications";
	private String CITIZEN_ACOUNT_APPLICATIONS_DEFAULT = "Citizen account applications";
	private String SCHOOL_CHOICES_KEY = "school_choices";
	private String SCHOOL_CHOICES_DEFAULT = "School choices";
	
	private boolean showHeader = true;
	
	private int schoolChoicesCount = 0;
	private int citizenAccountCount = 0;

	private void init(IWContext iwc) throws Exception {
		String[] validStatuses = new String[] { SchoolChoiceBMPBean.CASE_STATUS_PLACED, SchoolChoiceBMPBean.CASE_STATUS_PRELIMINARY, SchoolChoiceBMPBean.CASE_STATUS_MOVED };
		int tempSCC = getSchoolChoiceHome().getCount(validStatuses);
		if (tempSCC > 0) {
			schoolChoicesCount = tempSCC;	
		}
		int tempCAC = getCitizenAccountHome().getTotalCount();
		if (tempCAC > 0) {
			citizenAccountCount = tempCAC;	
		}
	}

	private void drawTable(IWContext iwc) {
		Table table = new Table();
		int row = 1;
		if (showHeader) {
			table.mergeCells(1, row, 2, row);
			table.add(getSmallHeader(getResourceBundle().getLocalizedString(HEADER_KEY, HEADER_DEFAULT)), 1, row);
			++row;
		}
		table.add(getSmallText(getResourceBundle().getLocalizedString(CITIZEN_ACOUNT_APPLICATIONS_KEY, CITIZEN_ACOUNT_APPLICATIONS_DEFAULT)), 1, row);
		table.add(getSmallText(Integer.toString(citizenAccountCount)), 2, row);
		++row;
		table.add(getSmallText(getResourceBundle().getLocalizedString(SCHOOL_CHOICES_KEY, SCHOOL_CHOICES_DEFAULT)), 1, row);
		table.add(getSmallText(Integer.toString(schoolChoicesCount)), 2, row);
		add(table);
	}


	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		drawTable(iwc);	
	}
	
	protected SchoolChoiceBusiness getSchoolChoiceBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwac, SchoolChoiceBusiness.class);	
	}

	protected SchoolChoiceHome getSchoolChoiceHome() throws RemoteException {
		return (SchoolChoiceHome) IDOLookup.getHome(SchoolChoice.class);	
	}

	protected CitizenAccountHome getCitizenAccountHome() throws RemoteException {
		return (CitizenAccountHome) IDOLookup.getHome(CitizenAccount.class);	
	}
	
	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;	
	}
}
