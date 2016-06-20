package com.sky.interactive.controllers;

import com.sky.interactive.MockOmnitureApplication;
import junit.framework.AssertionFailedError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockOmnitureApplication.class)
@WebIntegrationTest("server.port:9000")
public class MockOmnitureControllerTests {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	RestTemplate template = new TestRestTemplate();

	@Test
	public void success_request() {
		test_with_success_and_delay(true, 500, 500);
	}

	@Test
	public void failure_request() {
		test_with_success_and_delay(false, 500, 500);
	}

	@Test
	public void failing_test_because_of_delay() {
		Instant now = Instant.now();
		template.postForEntity("http://localhost:9000/success/true/delay/0", "", String.class);
		exception.expect(AssertionError.class);
		assert_time_difference_with_now_is_greater_than(now, 500);
	}

	void test_with_success_and_delay(boolean success, long delayInMillisecondsParam, long delayInMillisecondsAgainstTest) {
		Instant now = Instant.now();
		ResponseEntity<String> responseEntity = template.postForEntity("http://localhost:9000/success/"+success+"/delay/"+delayInMillisecondsParam, "", String.class);
		assert_time_difference_with_now_is_greater_than(now, delayInMillisecondsAgainstTest);
		assert_status_is_200_ok(responseEntity);
		if (success) {
			assert_body_is_succesful(responseEntity);
		}
		else {
			assert_body_is_failure(responseEntity);
		}
	}


	void assert_status_is_200_ok(ResponseEntity responseEntity) {
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	void assert_body_is_succesful(ResponseEntity<String> responseEntity) {
		assertEquals(MockOmnitureController.SUCCESS_RESPONSE_BODY, responseEntity.getBody());
	}

	void assert_body_is_failure(ResponseEntity<String> responseEntity) {
		assertEquals(MockOmnitureController.FAILURE_RESPONSE_BODY, responseEntity.getBody());
	}

	void assert_time_difference_with_now_is_greater_than(Instant time, long differenceInMilliseconds) {
		long difference = Instant.now().toEpochMilli() - time.toEpochMilli();
		assertTrue(difference > differenceInMilliseconds);
	}

}
