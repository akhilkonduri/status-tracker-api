package gov.sam.status.tracker;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.sam.status.tracker.util.StatusTrackerUtils;

public class StatusTrackerAPI implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	/**
	 * AWS lambda function handler for status tracker.
	 *
	 */
	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		String responseStr = "";
		int statusCode;

		try {
			context.getLogger().log("User selected duns: " + input.getQueryStringParameters().get("ueiDUNS"));
			String ueiDUNS = input.getQueryStringParameters().get("ueiDUNS");
			responseStr = new ObjectMapper().writerWithDefaultPrettyPrinter()
					.writeValueAsString(StatusTrackerUtils.populateInfo(ueiDUNS, context));
			statusCode = 200;

		} catch (Exception e) {

			statusCode = 400;
			e.printStackTrace();
		}

		return new APIGatewayProxyResponseEvent().withStatusCode(statusCode)
				.withHeaders(StatusTrackerUtils.populateResponseheaders()).withBody(responseStr);
	}
}
