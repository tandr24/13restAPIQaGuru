import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ReqResDataTests extends TestBase {

    @Test
    @DisplayName("Verify that total value after get request equals 12")
    void verifyTotalTest() {
        given()
                .when()
                .get("users")
                .then()
                .log().all()
                .body("total", is(12));
    }

    @Test
    @DisplayName("New user is added")
    void verifyIfNewUserAddedTest() {
        PostUserDTO user = new PostUserDTO();
        user.setName("Max");
        user.setJob("programmer");

        PostUserDTO responseUser = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("users")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(PostUserDTO.class);

        Assertions.assertEquals(user.getName(), responseUser.getName());
        Assertions.assertEquals(user.getJob(), responseUser.getJob());
    }

    @Test
    @DisplayName("Unsuccessful login returns error")
    void unsuccessfulLoginReturnsErrorTest() {
        LoginDTO loginUser = new LoginDTO();
        loginUser.setEmail("peter@klaven");

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginUser)
                .when()
                .post("login")
                .then()
                .log().all()
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Successful login returns token")
    void verifySuccessfulLoginTest() {
        LoginDTO loginUser = new LoginDTO();
        loginUser.setEmail("eve.holt@reqres.in");
        loginUser.setPassword("cityslicka");

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginUser)
                .when()
                .post("login")
                .then()
                .log().all()
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Verification status code for user, which doesn't exist")
    void verifyStatusCodeForNonExistedUserTest() {
        given()
                .log().all()
                .when()
                .get("user/23")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("Verification if method PUT changes name and job for user with id 2")
    void verifyPutForUserNameJobTest() {
        PostUserDTO user = new PostUserDTO();
        user.setName("Max");
        user.setJob("programmer");
        user.setId(2);

        PostUserDTO responseUserPost = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("users")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(PostUserDTO.class);

        PostUserDTO userPut = new PostUserDTO();
        userPut.setName("Egor");
        userPut.setJob("tester");

        PostUserDTO responseUserPut =  given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userPut)
                .when()
                .put("users/2")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(PostUserDTO.class);

        Assertions.assertEquals(userPut.getName(), responseUserPut.getName());
        Assertions.assertEquals(userPut.getJob(), responseUserPut.getJob());

    }

    @Test
    @DisplayName("Verification if method PATCH changes job for user with id 2")
    void verifyPatchForUserNameTest() {

        PostUserDTO user = new PostUserDTO();
        user.setName("Max");
        user.setJob("programmer");
        user.setId(2);

        PostUserDTO responseUserPost = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("users")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(PostUserDTO.class);

        PostUserDTO userPatch = new PostUserDTO();
        userPatch.setJob("tester");

        PostUserDTO responseUser =  given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userPatch)
                .when()
                .patch("users/2")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(PostUserDTO.class);

        Assertions.assertEquals(userPatch.getJob(), responseUser.getJob());
    }

    @Test
    @DisplayName("Verification if method DELETE delete user with id 2")
    void verifyDeleteTest() {

        PostUserDTO user = new PostUserDTO();
        user.setName("Max");
        user.setJob("programmer");
        user.setId(2);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("users")
                .then()
                .log().all()
                .statusCode(201);

        given()
                .log().all()
                .when()
                .delete("users/2")
                .then()
                .log().all()
                .statusCode(204);

    }
}
