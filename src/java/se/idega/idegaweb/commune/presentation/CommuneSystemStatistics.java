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
	private String SCHOOL_ALL_CHOICES_KEY = "school_choices_all";
	private String SCHOOL_ALL_CHOICES_DEFAULT = "Total school choices";
	private String SCHOOL_HANDLED_CHOICES_KEY = "school_choices_handled";
	private String SCHOOL_HANDLED_CHOICES_DEFAULT = "Handled school choices_";
	private String SCHOOL_UNHANDLED_CHOICES_KEY = "school_choices_unhandled";
	private String SCHOOL_UNHANDLED_CHOICES_DEFAULT = "Unhandled school choices";
	
	private boolean showHeader = true;
	
	private int allChoices = 0;
	private int handledChoices = 0;
	private int unhandledChoices = 0;
	private int citizenAccountCount = 0;

	private void init(IWContext iwc) throws Exception {
		String[] allStatuses = new String[] { SchoolChoiceBMPBean.CASE_STATUS_PRELIMINARY, SchoolChoiceBMPBean.CASE_STATUS_MOVED, SchoolChoiceBMPBean.CASE_STATUS_PLACED };
		String[] handledStatuses = new String[] { SchoolChoiceBMPBean.CASE_STATUS_PLACED };
		String[] unhandledStatuses = new String[] { SchoolChoiceBMPBean.CASE_STATUS_PRELIMINARY, SchoolChoiceBMPBean.CASE_STATUS_MOVED };
		int tempAll = getSchoolChoiceHome().getCount(allStatuses);
		if (tempAll > 0) {
			allChoices = tempAll;	
		}
		int tempHandled = getSchoolChoiceHome().getCount(handledStatuses);
		if (tempHandled > 0) {
			handledChoices = tempHandled;	
		}
		int tempUnhandled = getSchoolChoiceHome().getCount(unhandledStatuses);
		if (tempUnhandled > 0) {
			unhandledChoices = tempUnhandled;	
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
		table.add(getSmallText(getResourceBundle().getLocalizedString(SCHOOL_ALL_CHOICES_KEY, SCHOOL_ALL_CHOICES_DEFAULT)), 1, row);
		table.add(getSmallText(Integer.toString(allChoices)), 2, row);
		++row;
		table.add(getSmallText(getResourceBundle().getLocalizedString(SCHOOL_HANDLED_CHOICES_KEY, SCHOOL_HANDLED_CHOICES_DEFAULT)), 1, row);
		table.add(getSmallText(Integer.toString(handledChoices)), 2, row);
		++row;
		table.add(getSmallText(getResourceBundle().getLocalizedString(SCHOOL_UNHANDLED_CHOICES_KEY, SCHOOL_UNHANDLED_CHOICES_DEFAULT)), 1, row);
		table.add(getSmallText(Integer.toString(unhandledChoices)), 2, row);
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
