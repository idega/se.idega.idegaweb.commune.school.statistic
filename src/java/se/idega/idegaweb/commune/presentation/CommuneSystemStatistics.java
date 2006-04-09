package se.idega.idegaweb.commune.presentation;

import is.idega.idegaweb.egov.musicschool.business.MusicSchoolBusiness;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import se.idega.idegaweb.commune.account.citizen.business.CitizenAccountBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

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
	private String MUSIC_SCHOOL_CHOICES_TOTAL_KEY = "music_school_choices_total";
	private String MUSIC_SCHOOL_CHOICES_TOTAL_DEFAULT = "Music school choices";
	private String MUSIC_SCHOOL_CHOICES_KEY = "music_school_choices";
	private String MUSIC_SCHOOL_CHOICES_DEFAULT = "Music school choices - first hand";
	private String CHILDCARE_VACANCIES_KEY = "childcare_vacancies";
	private String CHILDCARE_VACANCIES_DEFAULT = "Childcare vacancies";
	
	
	private boolean showHeader = true;
	private String styleClass=null;
	
	private int schoolChoicesCount = 0;
	private int citizenAccountCount = 0;
	private int musicChoicesCountTotal = 0;
	private int musicChoicesCount = 0;
	private int providerVacanciesCount = 0;
	
	//private ChildCareBusiness business;
	
	private boolean showAccountStatistics;
	private boolean showChoiceStatistics;
	private boolean showMusicChoiceStatistics;
	private boolean showChildCareVacanices;
	
	private void init(IWContext iwc) throws Exception {
		if (isShowChoiceStatistics()) {
			this.schoolChoicesCount = getSchoolChoiceBusiness(iwc).getNumberOfApplicants();	
		}

		if (isShowAccountStatistics()) {
			this.citizenAccountCount = getCitizenAccountBusiness(iwc).getNumberOfApplications();
		}
		
		if (isShowMusicChoiceStatistics()) {
			this.musicChoicesCountTotal = getMusicSchoolBusiness(iwc).getMusicSchoolStatistics(false);
			this.musicChoicesCount = getMusicSchoolBusiness(iwc).getMusicSchoolStatistics(true);
		}
		
		if (this.showChildCareVacanices){
			this.providerVacanciesCount = getProviderVacancies(iwc);	
		}
						
		
	}

	private void drawTable() {
		Table table = new Table();
		int row = 1;
		
		if (isShowHeader()) {
			table.mergeCells(1, row, 2, row);
			table.add(getSmallHeader(getResourceBundle().getLocalizedString(this.HEADER_KEY, this.HEADER_DEFAULT)), 1, row++);
		}
		if (isShowAccountStatistics()) {
			table.add(getWhichStyle(getResourceBundle().getLocalizedString(this.CITIZEN_ACOUNT_APPLICATIONS_KEY, this.CITIZEN_ACOUNT_APPLICATIONS_DEFAULT)), 1, row);
			table.add(getWhichStyle(Integer.toString(this.citizenAccountCount)), 2, row++);
		}
		if (isShowChoiceStatistics()){
			table.add(getWhichStyle(getResourceBundle().getLocalizedString(this.SCHOOL_CHOICES_KEY, this.SCHOOL_CHOICES_DEFAULT)), 1, row);
			table.add(getWhichStyle(Integer.toString(this.schoolChoicesCount)), 2, row++);	
		}
		
		if (this.showChildCareVacanices){
			table.add(getWhichStyle(getResourceBundle().getLocalizedString(this.CHILDCARE_VACANCIES_KEY, this.CHILDCARE_VACANCIES_DEFAULT)), 1, row);
			table.add(getWhichStyle(Integer.toString(this.providerVacanciesCount)), 2, row++);	
		}
		
		if (isShowMusicChoiceStatistics()) {
			table.add(getWhichStyle(getResourceBundle().getLocalizedString(this.MUSIC_SCHOOL_CHOICES_TOTAL_KEY, this.MUSIC_SCHOOL_CHOICES_TOTAL_DEFAULT)), 1, row);
			table.add(getWhichStyle(Integer.toString(this.musicChoicesCountTotal)), 2, row++);
			table.add(getWhichStyle(getResourceBundle().getLocalizedString(this.MUSIC_SCHOOL_CHOICES_KEY, this.MUSIC_SCHOOL_CHOICES_DEFAULT)), 1, row);
			table.add(getWhichStyle(Integer.toString(this.musicChoicesCount)), 2, row++);
			
			
		}
		add(table);
	}


	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		drawTable();	
	}
	
	private int getProviderVacancies(IWContext iwc) throws Exception {
		Collection schoolTypes = null;
		schoolTypes = getBusiness(iwc).getSchoolBusiness().findAllSchoolTypesForChildCare();
		
		List providers = null;
		int providerVacanciesSum = 0;
		
		providers = new Vector(getBusiness(iwc).getSchoolBusiness().findAllSchoolsByType(schoolTypes));
		if (providers != null && !providers.isEmpty()) {
			int defaultCommuneId = ((Integer) getCommuneBusiness(iwc).getDefaultCommune().getPrimaryKey()).intValue();
			School school;
			ChildCarePrognosis prognosis;
			int providerID = -1;
			
			Iterator iter = providers.iterator();
			while (iter.hasNext()) {
				school = (School) iter.next();
				if (school.getCommuneId() != defaultCommuneId) {
					continue;
				}
				providerID = ((Integer)school.getPrimaryKey()).intValue();
				prognosis = getBusiness(iwc).getPrognosis(providerID);
				
				
				if (prognosis != null) {
					int providerVacancies = prognosis.getVacancies();
					if (providerVacancies!= -1) {
						providerVacanciesSum += providerVacancies;
					}
				}
				
			}
		}
		
		return providerVacanciesSum;
	}
	
	protected SchoolChoiceBusiness getSchoolChoiceBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwac, SchoolChoiceBusiness.class);	
	}

	protected CitizenAccountBusiness getCitizenAccountBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CitizenAccountBusiness) IBOLookup.getServiceInstance(iwac, CitizenAccountBusiness.class);	
	}
	
	protected MusicSchoolBusiness getMusicSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (MusicSchoolBusiness) IBOLookup.getServiceInstance(iwac, MusicSchoolBusiness.class);	
	}
	
		
	public Text getWhichStyle(String s){
		if (this.styleClass != null){
			return getStyledClass(s);
		}
		else{
			return getSmallText(s);
		}
	}
	
	public Text getStyledClass(String s) {
		return getStyleText(s, this.styleClass);
	}
	
	
	/**
	 * @return boolean
	 */
	public boolean isShowMusicChoiceStatistics() {
		return this.showMusicChoiceStatistics;
	}

	/**
	 * @return boolean
	 */
	public boolean isShowAccountStatistics() {
		return this.showAccountStatistics;
	}

	/**
	 * @return boolean
	 */
	public boolean isShowChoiceStatistics() {
		return this.showChoiceStatistics;
	}

	/**
	 * @return boolean
	 */
	public boolean isShowHeader() {
		return this.showHeader;
	}

	/**
	 * Sets the showChildCareVacanices.
	 * @param showChoiceStatistics The showChoiceStatistics to set
	 */
	public void setShowChildCareVacanices(boolean showChildCareVacanices) {
		this.showChildCareVacanices = showChildCareVacanices;
	}
	
	/**
	 * Sets the showChoiceStatistics.
	 * @param showChoiceStatistics The showChoiceStatistics to set
	 */
	public void setShowMusicChoiceStatistics(boolean showMusicChoiceStatistics) {
		this.showMusicChoiceStatistics = showMusicChoiceStatistics;
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
	/**
	 * Sets the styleClass.
	 * @param styleClass The styleClass to set
	 */
	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}
	
	protected CommuneBusiness getCommuneBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CommuneBusiness) IBOLookup.getServiceInstance(iwac, CommuneBusiness.class);
	}
	
	protected ChildCareBusiness getBusiness(IWApplicationContext iwac) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwac, ChildCareBusiness.class);
	}
}
