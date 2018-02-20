package com.saralein.cobspec.controller;

import com.saralein.cobspec.data.CookieStore;
import com.saralein.server.assertions.CookieAssertion;
import com.saralein.server.exchange.Cookie;
import com.saralein.server.request.Request;
import com.saralein.server.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertTrue;

public class CookieControllerTest {
    private Cookie chocolateCookie;
    private Cookie bakerCookie;
    private List<Cookie> cookies;
    private CookieController cookieController;
    private CookieStore cookieStore;
    private Request eatCookieRequest;
    private Response eatCookieResponse;

    @Before
    public void setUp() {
        chocolateCookie = new Cookie("type", "chocolate");
        bakerCookie = new Cookie("baker", "Phil");
        cookies = new ArrayList<Cookie>() {{
            add(chocolateCookie);
            add(bakerCookie);
        }};
        cookieStore = new CookieStore();
        cookieController = new CookieController(cookieStore);

        eatCookieRequest = new Request.Builder()
                .uri("/eat_cookie")
                .cookies(cookies)
                .build();

        eatCookieResponse = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .body("mmmm chocolate\nmmmm Phil\n")
                .build();
    }

    @Test
    public void respondsWithSetCookieAndAddsCookiesToStore() {
        Response expected = new Response.Builder()
                .status(200)
                .addHeader("Content-Type", "text/plain")
                .setCookies(cookies)
                .body("Eat")
                .build();
        Map<String, String> parameters = new HashMap<String, String>() {{
            put("baker", "Phil");
            put("type", "chocolate");
        }};
        Request request = new Request.Builder()
                .uri("/cookie")
                .method("GET")
                .parameters(parameters)
                .build();
        Response response = cookieController.call(request);

        assert (response.equals(expected));
        assertTrue(cookieStore.containsCookie(chocolateCookie));
        assertTrue(cookieStore.containsCookie(new Cookie("baker", "Phil")));
        CookieAssertion.assertCookiesAreEqual(cookies, response.getCookies());
    }

    @Test
    public void usesCookiesToRespond() {
        cookieStore.addCookies(cookies);
        Response response = cookieController.call(eatCookieRequest);

        assert (response.equals(eatCookieResponse));
    }

    @Test
    public void excludesCookieNotInStoreInResponse() {
        cookieStore.addCookies(cookies);
        cookies.add(new Cookie("amount", "12"));
        Response response = cookieController.call(eatCookieRequest);

        assert (response.equals(eatCookieResponse));
    }
}
