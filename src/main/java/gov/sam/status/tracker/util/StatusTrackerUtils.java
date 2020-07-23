package gov.sam.status.tracker.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;

import gov.sam.status.tracker.pojo.Contract;
import gov.sam.status.tracker.pojo.ContractOpps;
import gov.sam.status.tracker.pojo.StatusTrackerResponse;

public class StatusTrackerUtils {

	/**
	 * Utility method to populate status tracker response object.
	 * 
	 * @param ueiDuns
	 * @param context
	 * @return
	 */
	public static StatusTrackerResponse populateInfo(String ueiDuns, Context context) {

		StatusTrackerResponse response = new StatusTrackerResponse();
		response.getEntity().setUeiDUNS(ueiDuns);

		entityManagementInvoker(response, context);
		contractDataInvoker(response, context);
		contractOppsInvoker(response, context);

		return response;

	}

	/**
	 * Utility Method to fetch/process entity management data for given entity.
	 * 
	 * @param response
	 * @param context
	 */
	public static void entityManagementInvoker(StatusTrackerResponse response, Context context) {

		try {
			URL entityUrl = new URL(
					Constants.ENTITY_MANAGEMENT_API_URL + "&ueiDUNS=" + response.getEntity().getUeiDUNS());
			context.getLogger().log("Entity Url :" + entityUrl);

			String entityStr = IOUtils.toString(entityUrl, Charset.forName("UTF-8"));

			if (StringUtils.isNotEmpty(entityStr)) {
				JSONArray entityData = new JSONObject(entityStr).optJSONArray("entityData");
				if (entityData != null && entityData.length() >= 1) {
					JSONObject entity = ((JSONObject) entityData.get(0)).optJSONObject("entityRegistration");
					response.getEntity().setLegalBusinessName(String.valueOf(entity.opt("legalBusinessName")));
					response.getEntity().setCageCode(String.valueOf(entity.opt("cageCode")));
					response.getEntity().setRegistartionStatus(String.valueOf(entity.opt("registrationStatus")));
					response.getEntity().setExpirationDate(String.valueOf(entity.opt("expirationDate")));

					if (StringUtils.isNotEmpty(String.valueOf(entity.opt("exclusionURL")))) {
						populateExclusionInfo(response, context);

					}

					JSONObject goodsAndServices = ((JSONObject) entityData.get(0)).optJSONObject("assertions")
							.optJSONObject("goodsAndServices");
					JSONArray naicsArray = goodsAndServices.getJSONArray("naicsList");

					if (naicsArray != null) {
						for (int i = 0; i< naicsArray.length(); i++) {
							JSONObject naics = (JSONObject) naicsArray.get(i);
							response.getEntity().getNaicsList().add(String.valueOf(naics.opt("naicsCode")));
						}

					}
				}
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Utility Method to fetch/process exclusion data for given entity.
	 * 
	 * @param response
	 * @throws IOException
	 */
	private static void populateExclusionInfo(StatusTrackerResponse response, Context context) throws IOException {
		URL exclusionUrl = new URL(Constants.EXCLUSION_API_URL + "&ueiDUNS=" + response.getEntity().getUeiDUNS());

		context.getLogger().log("Exclusion Url :" + exclusionUrl);

		String exclusionStr = IOUtils.toString(exclusionUrl, Charset.forName("UTF-8"));
		if (StringUtils.isNotEmpty(exclusionStr)) {
			JSONArray exclusion = new JSONObject(exclusionStr).optJSONArray("excludedEntity");
			if (exclusion != null && exclusion.length() >= 1) {
				JSONObject details = ((JSONObject) exclusion.get(0)).optJSONObject("exclusionDetails");
				response.getExclusion().setClassificationType(String.valueOf(details.opt("classificationType")));
				response.getExclusion().setExclusionType(String.valueOf(details.opt("exclusionType")));
				response.getExclusion().setExclusionProgram(String.valueOf(details.opt("exclusionProgram")));
				response.getExclusion().setExcludingAgencyName(String.valueOf(details.opt("excludingAgencyName")));

				JSONObject identification = ((JSONObject) exclusion.get(0)).optJSONObject("exclusionIdentification");
				response.getExclusion().setUeiDuns(String.valueOf(identification.opt("exclusionType")));
				response.getExclusion().setCageCode(String.valueOf(identification.opt("cageCode")));
				response.getExclusion().setExclusionName(String.valueOf(identification.opt("name")));

				JSONArray actions = ((JSONObject) exclusion.get(0)).optJSONObject("exclusionActions")
						.getJSONArray("listOfActions");

				if (actions != null && actions.length() >= 1) {
					JSONObject listOfAction = (JSONObject) actions.get(0);
					response.getExclusion().setActiveDate(String.valueOf(listOfAction.opt("activateDate")));
					response.getExclusion().setTerminationDate(String.valueOf(listOfAction.opt("terminationDate")));
					response.getExclusion().setTerminationType(String.valueOf(listOfAction.opt("terminationType")));
				}
			}

		}
	}

	/**
	 * Utility Method to fetch/process contracts.
	 * 
	 * @param response
	 * @param context
	 */
	public static void contractDataInvoker(StatusTrackerResponse response, Context context) {

		String presentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		String pastDate = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		String dateSigned = "[" + pastDate + "," + presentDate + "]";

		try {
			URL contractUrl = new URL(Constants.CONTRACT_DATA_API_URL + "&ueiDUNS=" + response.getEntity().getUeiDUNS()
					+ "&dateSigned=" + dateSigned + "&dollarsObligated=[0,]");

			context.getLogger().log("Contract Data Url :" + contractUrl);

			String contractDataRespStr = IOUtils.toString(contractUrl, Charset.forName("UTF-8"));

			if (StringUtils.isNotEmpty(contractDataRespStr)) {
				JSONArray awards = new JSONObject(contractDataRespStr).optJSONArray("awardsData");

				if (awards != null) {
					for (int i = 0; i < awards.length(); i++) {
						Contract contract = new Contract();
						JSONObject contractDataObj = (JSONObject) awards.get(i);
						JSONObject documentInfo = contractDataObj.optJSONObject("documentInformation");
						JSONObject dateObj = contractDataObj.optJSONObject("dates");
						JSONObject dollarsObj = contractDataObj.optJSONObject("dollars");
						contract.setDateSigned(String.valueOf(dateObj.optString("dateSigned")));
						contract.setActionObligation(String.valueOf(dollarsObj.optString("actionObligation")));
						contract.setContractName(String.valueOf(documentInfo.optString("piid")));

						response.getContracts().add(contract);
					}
				}
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Utility Method to fetch/process contract opportunities.
	 * 
	 * @param response
	 * @param context
	 */
	public static void contractOppsInvoker(StatusTrackerResponse response, Context context) {

		try {

			String limit = "1000";
			String postedTo = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			String postedFrom = LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

			for (String naics : response.getEntity().getNaicsList()) {
				URL contractOppUrl = new URL(Constants.CONTRACT_OPP_URL + "&limit=" + limit + "&postedFrom="
						+ postedFrom + "&postedTo=" + postedTo + "&ncode=" + naics);

				context.getLogger().log("Contract Opportunities Url :" + contractOppUrl);

				String contractOppsRespStr = IOUtils.toString(contractOppUrl, Charset.forName("UTF-8"));

				if (StringUtils.isNotEmpty(contractOppsRespStr)) {

					JSONArray oppo = new JSONObject(contractOppsRespStr).optJSONArray("opportunitiesData");
					List<ContractOpps> contractsOppsInfo = new ArrayList<ContractOpps>();

					if (oppo != null) {
						for (int i = 0; i < oppo.length(); i++) {
							JSONObject obj = (JSONObject) oppo.get(i);
							contractsOppsInfo.add(new ContractOpps(String.valueOf(obj.optString("title"))));
						}
					}
					response.getContractOpportunities().addAll(contractsOppsInfo);
				}
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Utility method to populate response headers.
	 * 
	 * @return Map
	 */
	public static Map<String, String> populateResponseheaders() {

		Map<String, String> headers = new HashMap<>();
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		headers.put("Access-Control-Max-Age", "3600");
		headers.put("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

		return headers;
	}

}
