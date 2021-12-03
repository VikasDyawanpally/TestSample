package tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CountrySearchTest {
	Properties prop = new Properties();

	@Test
	public void testWithValidCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		expectedCountryCapital.put("British Indian Ocean Territory", "[\"Diego Garcia\"]");
		Map<String, String> actualCountryCapital = getCountryCapitalByFullName("British Indian Ocean Territory");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithValidPartialCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		expectedCountryCapital.put("United Mexican States", "[\"Mexico City\"]");
		expectedCountryCapital.put("United States of America", "[\"Washington, D.C.\"]");
		expectedCountryCapital.put("United Republic of Tanzania", "[\"Dodoma\"]");
		Map<String, String> actualCountryCapital = getCountryCapitalByName("united");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithValidAlpha2CountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		expectedCountryCapital.put("United States of America", "[\"Washington, D.C.\"]");
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("USA");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithValidPartialCountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("u");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithValidSmallCaseCharactersCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		expectedCountryCapital.put("Republic of India", "[\"New Delhi\"]");
		expectedCountryCapital.put("British Indian Ocean Territory", "[\"Diego Garcia\"]");
		Map<String, String> actualCountryCapital = getCountryCapitalByName("british indian ocean territory");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithoutSpacesInCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		expectedCountryCapital.put("Republic of India", "[\"New Delhi\"]");
		Map<String, String> actualCountryCapital = getCountryCapitalByName("britishindianoceanterritory");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithInValidCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByName("abcd");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithInValidDataTypeCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByName("1234");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithEmptyCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByName("");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithSpecialCharactersCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByName("@!$%^&*()");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithSQLInjectionCountryName() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByName("%a");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithValidSmallCaseCharactersCountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		expectedCountryCapital.put("United States of America", "[\"Washington, D.C.\"]");
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("us");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithInValidCountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("abcd");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithInValidDataTypeCountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("1234");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithEmptyCountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithSpecialCharactersCountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("@!$%^&*()");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	@Test
	public void testWithSQLInjectionCountryCode() throws FileNotFoundException, IOException {
		Map<String, String> expectedCountryCapital = new HashMap<String, String>();
		Map<String, String> actualCountryCapital = getCountryCapitalByCode("%S");
		Assert.assertEquals(actualCountryCapital, expectedCountryCapital, "Incorrect data");
	}

	public Map<String, String> getCountryCapitalByCode(String countryCode) throws FileNotFoundException, IOException {
		Map<String, String> countriesMap = new HashMap<String, String>();
		prop.load(new FileInputStream("Config.properties"));
		RestAssured.baseURI = prop.getProperty("BaseURL");
		RequestSpecification httpRequest = RestAssured.given();
		Response countryCodeResponse = httpRequest.request(Method.GET, "alpha/" + countryCode);
		System.out.println("country Code Response" + countryCodeResponse.getBody().asString());
		if (countryCodeResponse.getStatusCode() != 200) {
			System.out.println("There are no countries available for provided data " + countryCode);
		} else {
			if (countryCodeResponse.getStatusCode() == 200) {
				JSONArray countriesData = new JSONArray(countryCodeResponse.getBody().asString());
				for (int i = 0; i < countriesData.length(); i++) {
					JSONObject countryData = countriesData.getJSONObject(i);
					countriesMap.put(countryData.getJSONObject("name").get("official").toString(),
							countryData.get("capital").toString());

				}
			}
		}
		return countriesMap;
	}

	public Map<String, String> getCountryCapitalByName(String country) throws FileNotFoundException, IOException {
		Map<String, String> countriesMap = new HashMap<String, String>();
		prop.load(new FileInputStream("Config.properties"));
		RestAssured.baseURI = prop.getProperty("BaseURL");
		RequestSpecification httpRequest = RestAssured.given();
		Response countryNameResponse = httpRequest.request(Method.GET, "name/" + country);
		System.out.println("country Name Response" + countryNameResponse.getBody().asString());
		if (countryNameResponse.getStatusCode() != 200) {
			System.out.println("There are no countries available for provided data " + country);
		} else {
			if (countryNameResponse.getStatusCode() == 200) {
				Map<String, String> countryNameMap = countriesList(countryNameResponse);
				countriesMap.putAll(countryNameMap);
			}
		}
		return countriesMap;
	}

	public Map<String, String> countriesList(Response response) {
		Map<String, String> countriesMap = new HashMap<String, String>();
		JSONArray countriesJsonArrray = new JSONArray(response.getBody().asString());
		for (int i = 0; i < countriesJsonArrray.length(); i++) {
			JSONObject countryJsonObject = countriesJsonArrray.getJSONObject(i);
			countriesMap.put(countryJsonObject.getJSONObject("name").get("official").toString(),
					countryJsonObject.get("capital").toString());
		}
		return countriesMap;

	}

	public Map<String, String> getCountryCapitalByFullName(String country) throws FileNotFoundException, IOException {
		Map<String, String> countriesMap = new HashMap<String, String>();
		prop.load(new FileInputStream("Config.properties"));
		RestAssured.baseURI = prop.getProperty("BaseURL");
		RequestSpecification httpRequest = RestAssured.given();
		Response countryNameResponse = httpRequest.request(Method.GET, "name/" + country + "?fullText=true");
		System.out.println("country Name Response" + countryNameResponse.getBody().asString());
		if (countryNameResponse.getStatusCode() != 200) {
			System.out.println("There are no countries available for provided data " + country);
		} else {
			if (countryNameResponse.getStatusCode() == 200) {
				Map<String, String> countryNameMap = countriesList(countryNameResponse);
				countriesMap.putAll(countryNameMap);
			}
		}
		return countriesMap;
	}
}
