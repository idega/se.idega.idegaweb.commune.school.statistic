package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

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

	private boolean showAccountStatistics;
	private boolean showChoiceStatistics;
	
	private void init(IWContext iwc) throws Exception {
		if (isShowChoiceStatistics()) {
			String[] validStatuses = new String[] { SchoolChoiceBMPBean.CASE_STATUS_PLACED, SchoolChoiceBMPBean.CASE_STATUS_PRELIMINARY, SchoolChoiceBMPBean.CASE_STATUS_MOVED };
			schoolChoicesCount = getSchoolChoiceBusiness(iwc).getNumberOfApplicants(validStatuses);	
		}

		if (isShowAccountStatistics()) {
			citizenAccountCount = getCitizenAccountBusiness(iwc).getNumberOfApplications();
		}
	}

	private void drawTable() {
		Table table = new Table();
		int row = 1;
		if (isShowHeader()) {
			table.mergeCells(1, row, 2, row);
			table.add(getSmallHeader(getResourceBundle().getLocalizedString(HEADER_KEY, HEADER_DEFAULT)), 1, row++);
		}
		if (isShowAccountStatistics()) {
			table.add(getSmallText(getResourceBundle().getLocalizedString(CITIZEN_ACOUNT_APPLICATIONS_KEY, CITIZEN_ACOUNT_APPLICATIONS_DEFAULT)), 1, row);
			table.add(getSmallText(Integer.toString(citizenAccountCount)), 2, row++);
		}
		if (isShowChoiceStatistics()) {
			table.add(getSmallText(getResourceBundle().getLocalizedString(SCHOOL_CHOICES_KEY, SCHOOL_CHOICES_DEFAULT)), 1, row);
			table.add(getSmallText(Integer.toString(schoolChoicesCount)), 2, row);
		}
		add(table);
	}


	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		drawTable();	
	}
	
	protected SchoolChoiceBusiness getSchoolChoiceBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwac, SchoolChoiceBusiness.class);	
	}

	protected CitizenAccountBusiness getCitizenAccountBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwac, CitizenAccountBusiness.class);	
	}
	
	/**
	 * @return boolean
	 */
	public boolean isShowAccountStatistics() {
		return showAccountStatistics;
	}

	/**
	 * @return boolean
	 */
	public boolean isShowChoiceStatistics() {
		return showChoiceStatistics;
	}

	/**
	 * @return boolean
	 */
	public boolean isShowHeader() {
		return showHeader;
	}

	/**
	 * Sets the showAccountStatistics.
	 * @param showAccountStatistics The showAccountStatistics to set
	 */
	public void setShowAccountStatistics(boolean showAccountStatistics) {
		this.showAccountStatistics = showAccountStatistics;
	}

	/**
	 * Sets the showChoiceStatistics.
	 * @param showChoiceStatistics The showChoiceStatistics to set
	 */
	public void setShowChoiceStatistics(boolean showChoiceStatistics) {
		this.showChoiceStatistics = showChoiceStatistics;
	}

	/**
	 * Sets the showHeader.
	 * @param showHeader The showHeader to set
	 */
	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;	
	}
}
