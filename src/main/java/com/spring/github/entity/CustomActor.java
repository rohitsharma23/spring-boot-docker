package com.spring.github.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;

public class CustomActor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long actorId;
	private Timestamp createdAt;
	private String login;
	private ActorEntity actor;
	private int count;

	public CustomActor(Long actorId, Timestamp createdAt, String login, ActorEntity actor, int count) {
		this.actorId = actorId;
		this.createdAt = createdAt;
		this.login = login;
		this.actor = actor;
		this.count = count;
	}

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public ActorEntity getActor() {
		return actor;
	}

	public void setActor(ActorEntity actor) {
		this.actor = actor;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
