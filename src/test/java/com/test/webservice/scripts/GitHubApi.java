package com.test.webservice.scripts;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.test.webservice.models.response.SingleRepoResponsePOJO;
import com.test.webservice.models.request.SingleRepoPOJO;
import com.test.webservice.models.request.UpdateRepoPOJO;
import com.test.webservice.models.request.CreateNewRepoPOJO;
import com.test.webservice.models.request.DeleteRepoPOJO;
import com.test.webservice.models.response.GetAllRepoPOJO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ResponseOptions;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import com.test.constants.Endpoints;

public class GitHubApi {
	String extractedToken = Endpoints.TOKEN;

	@BeforeClass
	public void init() {
		RestAssured.baseURI = "https://api.github.com/";

	}

	@Test
	public void Getsinglerepository() {

		Header ob = new Header("token", extractedToken);
		SingleRepoPOJO data = new SingleRepoPOJO();
		data.setOwner("Anusha8025");
		data.setRepo("Cucumber-Assignment");
		String Owner = data.getOwner();
		String Repo = data.getRepo();

		Response res = RestAssured.given().pathParam("param1", Owner).pathParam("param2", Repo)
				.log().all()
				.header(ob)
				.contentType(ContentType.JSON)
				.body(data)
				.when()
				.get("repos/{param1}/{param2}");
		
		res.then().statusCode(200).contentType("application/json; charset=utf-8");
		System.out.println("Answer1 = " + res.body().jsonPath().getString("full_name"));
		System.out.println("Answer2 = " + res.body().jsonPath().getString("default_branch"));
		Assert.assertEquals(res.body().jsonPath().getString("full_name"), "Anusha8025/Cucumber-Assignment",
				"Correct name returned");
		Assert.assertEquals(res.body().jsonPath().getString("default_branch"), "main", "Correct branch returned");
	}

	// validate schema for Getsinglerepository
	@Test
	public void GetsinglerepositorySchema() {

		Header ob = new Header("token", extractedToken);
		Response res = RestAssured.given().log().all().header(ob).when().get("repos/Anusha8025/Sept2023");
		res.then().log().body().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GitHubSchema.json"));
		// res.prettyPrint();
	}

	@Test
	public void GetsinglerepositorywithNonExistingRepoName() {

		Header ob = new Header("token", extractedToken);
		SingleRepoPOJO data = new SingleRepoPOJO();
		data.setOwner("Anusha8025");
		data.setRepo("Oct2023");
		String Owner = data.getOwner();
		String Repo = data.getRepo();

		Response res = RestAssured.given().pathParam("param1", Owner).pathParam("param2", Repo).log().all().header(ob)
				.contentType(ContentType.JSON).body(data).when().get("repos/{param1}/{param2}");
		res.then().statusCode(404).extract().path("message");

		Assert.assertEquals(res.statusCode(), 404, "Correct statuscode returned");
		Assert.assertEquals(res.body().jsonPath().getString("message"), "Not Found", "Correct message displayed");
		// System.out.println("body =" +res.body().jsonPath().getString("message"));
	}

	@Test
	public void GetAllRepositories() {
		System.out.println("inside GetAllRepositories token=" + extractedToken);
		Header ob = new Header("token", extractedToken);
		// SERIALIZATION

		Response response = RestAssured.given().auth().oauth2(extractedToken).header(ob).contentType(ContentType.JSON)
				.when().get("user/repos");
		response.then()
				// .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GitHubSchema.json"))
				.statusCode(200).contentType("application/json; charset=utf-8");

		List<GetAllRepoPOJO> res = RestAssured.given().auth().oauth2(extractedToken).log().all().header(ob).when()
				.get("user/repos").as(new TypeRef<List<GetAllRepoPOJO>>() {
				});
		System.out.println("size = " + res.size());
		// System.out.println("Code"+((ResponseOptions<Response>) res).getStatusCode());

		List<String> reponames = new ArrayList<String>();
		for (int i = 0; i < res.size(); i++) {

			if (res.get(i).getVisibility().equalsIgnoreCase("public")) {
				reponames.add(res.get(i).getName());
			}
		}

		System.out.println("Public repositories are: " + "\n" + reponames.toString());
	}
	
	@Test
	public void CreateRepository() {
		Header ob = new Header("token", extractedToken);
		CreateNewRepoPOJO data=new CreateNewRepoPOJO();
		data.setName("TestAPIRepo17");
		data.setDescription("This is your test repo!");
		data.setHomepage("https://github.com");
		data.setPrivate("false");
		
		// SERIALIZATION
		

		Response res = RestAssured.given()
				.auth().oauth2(extractedToken)
				.log().all()
				.header(ob)
				.contentType(ContentType.JSON)
				.body(data)
				.when()
				.post("user/repos");
		res.then()
		// .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GitHubSchema.json"))
		.statusCode(201)
		.contentType("application/json; charset=utf-8");
		System.out.println("Name = " + res.body().jsonPath().getString("name"));
		System.out.println("login = " + res.body().jsonPath().getJsonObject("owner.login"));
		System.out.println("type = " + res.body().jsonPath().getJsonObject("owner.type"));
		Assert.assertEquals(res.body().jsonPath().getString("name"), "TestAPIRepo17","Correct name returned");
		Assert.assertEquals(res.body().jsonPath().getJsonObject("owner.login"), "Anusha8025", "Correct login returned");
		Assert.assertEquals(res.body().jsonPath().getJsonObject("owner.type"), "User", "Correct type returned");

		
	}
	
	@Test
	public void CreateRepositorywithExistingName() {
		Header ob = new Header("token", extractedToken);
		CreateNewRepoPOJO data=new CreateNewRepoPOJO();
		data.setName("TestAPIRepo10");
		data.setDescription("This is your test repo!");
		data.setHomepage("https://github.com");
		data.setPrivate("false");
		
		// SERIALIZATION
		

		Response res = RestAssured.given()
				.auth().oauth2(extractedToken)
				.log().all()
				.header(ob)
				.contentType(ContentType.JSON)
				.body(data)
				.when()
				.post("user/repos");
		res.then()
		// .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GitHubSchema.json"))
		.statusCode(422)
		.extract().path("message");

		Assert.assertEquals(res.statusCode(), 422, "Correct statuscode returned");
		Assert.assertEquals(res.body().jsonPath().getString("message"), "Repository creation failed.", "Correct message displayed");

		
	}
	
	@Test
	public void UpdateRepo() {
		Header ob = new Header("token", extractedToken);
		UpdateRepoPOJO data=new UpdateRepoPOJO();
		data.setName("UpdatedTestAPIRepo10");
		data.setDescription("This is your test repo 10 updated!");
		data.setPrivate("false");
		data.setOwner("Anusha8025");
		data.setRepo("TestAPIRepo10");
		String Owner = data.getOwner();
		String Repo = data.getRepo();
		
		// SERIALIZATION
		

		Response res = RestAssured.given()
				.pathParam("param1", Owner).pathParam("param2", Repo)
				.auth().oauth2(extractedToken)
				.log().all()
				.header(ob)
				.contentType(ContentType.JSON)
				.body(data)
				.when()
				.patch("repos/{param1}/{param2}");
		res.then()
		// .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GitHubSchema.json"))
		.statusCode(200);
		System.out.println("Name = " + res.body().jsonPath().getString("name"));

		Assert.assertEquals(res.statusCode(), 200, "Correct statuscode returned");
		Assert.assertEquals(res.body().jsonPath().getString("name"), "UpdatedTestAPIRepo10", "Correct name displayed");

		
	}
	
	@Test
	public void DeleteRepo() {
		Header ob = new Header("token", extractedToken);
		DeleteRepoPOJO data=new DeleteRepoPOJO();
		data.setOwner("Anusha8025");
		data.setRepo("TestAPIRepo15");
		String Owner = data.getOwner();
		String Repo = data.getRepo();
		
		// SERIALIZATION
		

		Response res = RestAssured.given()
				.pathParam("param1", Owner).pathParam("param2", Repo)
				.auth().oauth2(extractedToken)
				.log().all()
				.header(ob)
				.contentType(ContentType.JSON)
				.body(data)
				.when()
				.delete("repos/{param1}/{param2}");
		res.then()
		// .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GitHubSchema.json"))
		.statusCode(204);
		System.out.println("body =" +res.body().prettyPrint());

		Assert.assertEquals(res.statusCode(), 204, "Correct statuscode returned");
		Assert.assertEquals(res.body().prettyPrint(), "", "Correct value displayed");
	}
}
