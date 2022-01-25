package com.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.app.model.LocationStates;

@Service
public class CoronaVirusDataService {

	private List<LocationStates> allStates = new ArrayList<>();

	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusDate() throws IOException, InterruptedException {
		List<LocationStates> newStates = new ArrayList<>();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		// System.out.println(httpResponse.body());

		StringReader cvsBodyReader = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(cvsBodyReader);
		for (CSVRecord record : records) {
//			String ProvinceState = record.get("Province/State");
//			System.out.println(ProvinceState);
			LocationStates lcoStates = new LocationStates();
			lcoStates.setState(record.get("Province/State"));
			lcoStates.setCountry(record.get("Country/Region"));
			lcoStates.setLastTotalCase(Integer.parseInt(record.get(record.size() - 1)));
			 newStates.add(lcoStates);
			//System.out.println(lcoStates);
		}

		this.allStates = newStates;
	}
	public List<LocationStates> getAllStates() {
		return allStates;
	}


	

}
