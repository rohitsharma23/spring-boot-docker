package com.spring.github.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.github.entity.ActorEntity;
import com.spring.github.model.Actor;
import com.spring.github.model.Event;
import com.spring.github.service.GithubApiRestService;

@RestController
public class GithubApiRestController {

	@Autowired
	private GithubApiRestService service;

	@ResponseBody
	@RequestMapping(value = { "/events" }, method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> postEvents(@RequestBody Event event) {
		System.out.println("############## POST ###########");
		String str = service.save(event);
		if (null != str)
			return ResponseEntity.status(HttpStatus.CREATED).body(str);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Save unsuccessful.");
	}

	@ResponseBody
	@RequestMapping(value = { "/erase" }, method = {
			RequestMethod.DELETE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> erase() {

		service.deleteAll();
		return ResponseEntity.status(HttpStatus.OK).body("All records deleted");
	}

	@ResponseBody
	@RequestMapping(value = { "/events" }, method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getEvents(@RequestHeader Map<String, String> headers) {
		System.out.println("*************** GET **************");
		/*headers.forEach((key, value) -> {
			System.out.println(String.format("Header '%s' = %s", key, value));
		});*/

		return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
	}

	@ResponseBody
	@RequestMapping(value = { "/events/actors/{actorID}" }, method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEventsByActors(@PathVariable String actorID) {

		List<Event> events = service.getAllEventsByActors(Long.parseLong(actorID));
		if (null != events)
			return ResponseEntity.status(HttpStatus.OK).body(events);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(events);
	}

	@ResponseBody
	@RequestMapping(value = { "/actors" }, method = { RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAllEventsByActors(@RequestBody Actor actor) {

		int status = service.updateAvatarUrl(actor);

		if (status == 0)// does not exist
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actor not found.Update failed.");
		else if (status == 1)// being updated
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Actor is being updated.Please retry.");
		else if (status == 2)// success
			return ResponseEntity.status(HttpStatus.OK).body("Avatar URL updated");
		return ResponseEntity.status(HttpStatus.OK).body("");
	}

	@ResponseBody
	@RequestMapping(value = { "/actors" }, method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Actor>> getActorsByEventsCount() {

		List<Actor> actors = service.getActorsByEventsCount();
		return ResponseEntity.status(HttpStatus.OK).body(actors);
	}

	@ResponseBody
	@RequestMapping(value = { "/actors/streak" }, method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Actor>> getActorsByEventsStreak() {

		List<Actor> actors = service.getActorsByEventsStreak();
		return ResponseEntity.status(HttpStatus.OK).body(actors);
	}
}
