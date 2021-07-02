package testingGeeks.Q4;

import static io.restassured.RestAssured.given;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ReqresTest {
	private String API_URL = "https://reqres.in/" ; 

	@Test
	public void listUsersTest() 

	{
		String endPointURL = String.format("%s%s",this.API_URL,"api/users"); 
		Response todoResponse = given()
				.contentType(ContentType.JSON)
				.when().get(endPointURL)
				.then()
				.statusCode(200)
				.extract().response();
		JsonPath jsonResponse = todoResponse.body().jsonPath();
		int actualNumberOfUsers =  Integer.valueOf(jsonResponse.getString("per_page")) ;
		Assert.assertTrue(actualNumberOfUsers > 1);
	
	}


	@Test
	public void SingleUserNotFound() 

	{
		String endPointURL = String.format("%s%s",this.API_URL,"api/users/23"); 
		Response todoResponse = given()
				.contentType(ContentType.JSON)
				.when().get(endPointURL)
				.then()
				.statusCode(404)
				.extract().response();
		Assert.assertEquals("{}", todoResponse.asString());

	}


	@Test
	public void successfulRegistration() 

	{  
		String endPointURL = String.format("%s%s",this.API_URL,"api/register"); 
		String email= "eve.holt@reqres.in";
		String password= "pistol";


		RequestSpecification request = RestAssured.given() ;
		request.contentType(ContentType.JSON) ;
		JSONObject requestparams = new  JSONObject() ; 
		requestparams.put("email", email);
		requestparams.put("password", password);
		request.body(requestparams.toString()) ;

		Response todoResponse = 
				request.post(endPointURL)
				.then()
				.statusCode(200)
				.extract().response();
		JsonPath jsonResponse = todoResponse.body().jsonPath();
		Assert.assertTrue(todoResponse.asString().contains("token"));
		Assert.assertNotNull(jsonResponse.getString("token"));

	}
	
	@Test
	public void unSuccessfulRegistration() 

	{  
		String endPointURL = String.format("%s%s",this.API_URL,"api/register"); 
		String email= "sydney@fife";


		RequestSpecification request = RestAssured.given() ;
		request.contentType(ContentType.JSON) ;
		JSONObject requestparams = new  JSONObject() ; 
		requestparams.put("email", email);
		request.body(requestparams.toString()) ;

		Response todoResponse = 
				request.post(endPointURL)
				.then()
				.statusCode(400)
				.extract().response();
		Assert.assertTrue(todoResponse.asString().contains("error"));

	}





}
