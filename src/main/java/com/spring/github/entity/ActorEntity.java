package com.spring.github.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "actor")
public class ActorEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String login;
	@Column(name = "AVATAR_URL")
	private String avatarUrl;
	
	public ActorEntity() {}

	public ActorEntity(Long id, String login, String avatarUrl) {
		this.id = id;
		this.login = login;
		this.avatarUrl = avatarUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAvatar() {
		return avatarUrl;
	}

	public void setAvatar(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
}
