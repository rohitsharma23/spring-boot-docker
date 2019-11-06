package com.spring.github.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "EVENT")
public class EventEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String type;

	@OneToOne
	private ActorEntity actor;
	@OneToOne
	private RepoEntity repo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name="CREATED_AT", columnDefinition = "datetime2")
	private Timestamp createdAt;

	public EventEntity() {
	}

	public EventEntity(Long id, String type, ActorEntity actor, RepoEntity repo, Timestamp createdAt) {
		this.id = id;
		this.type = type;
		this.actor = actor;
		this.repo = repo;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ActorEntity getActor() {
		return actor;
	}

	public void setActor(ActorEntity actor) {
		this.actor = actor;
	}

	public RepoEntity getRepo() {
		return repo;
	}

	public void setRepo(RepoEntity repo) {
		this.repo = repo;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

}
