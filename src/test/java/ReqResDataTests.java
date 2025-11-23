import io.restassured.http.ContentType;
import org.apache.http.entity.mime.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ReqResDataTests extends TestBase {
    Header header = new Header();

    @Test
    @DisplayName("Verify that total value after get request equals 12")
    void verifyTotal() {
        given()
                .when()
                .get("users")
                .then()
                .log().all()
                .body("total", is(12));
    }

    @Test
    @DisplayName("New user is added")
    void verifyIfNewUserAdded() {
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
    void unsuccessfulLoginReturnsError() {
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
    void verifySuccessfulLogin() {
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
    void verifyStatusCodeForNonExistedUser() {
        given()
                .log().all()
                .when()
                .get("user/23")
                .then()
                .log().all()
                .statusCode(404);
    }
}
