package com.github.mkopylec.reverseproxy

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification

import static com.github.mkopylec.reverseproxy.stubs.DestinationStubs.stubRequest
import static org.apache.commons.lang3.StringUtils.EMPTY

@WebIntegrationTest(randomPort = true)
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = TestApplication)
abstract class BasicSpec extends Specification {

    @Rule
    public WireMockRule localhost8080 = new WireMockRule(8080)
    @Rule
    public WireMockRule localhost8081 = new WireMockRule(8081)

    @Shared
    private RestTemplate restTemplate = new RestTemplate()
    @Autowired
    private EmbeddedWebApplicationContext context

    protected ResponseEntity<String> sendRequest(HttpMethod method, String uri, Map<String, String> headers = [:], String body = EMPTY) {
        def url = "http://localhost:$context.embeddedServletContainer.port$uri"
        def httpHeaders = new HttpHeaders()
        headers.each { name, value -> httpHeaders.add(name, value) }
        def request = new HttpEntity<>(body, httpHeaders)
        def response = restTemplate.exchange(url, method, request, String)

        return response
    }

    protected void stubRequest(HttpMethod method, String uri) {
        stubRequest(method, uri, localhost8080, localhost8081)
    }

    protected void stubRequest(HttpMethod method, String uri, Map<String, String> requestHeaders) {
        stubRequest(method, uri, requestHeaders, localhost8080, localhost8081)
    }

    protected void stubRequest(HttpMethod method, String uri, String requestBody) {
        stubRequest(method, uri, requestBody, localhost8080, localhost8081)
    }
}