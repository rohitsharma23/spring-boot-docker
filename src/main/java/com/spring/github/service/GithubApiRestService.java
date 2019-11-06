package com.spring.github.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.github.entity.ActorEntity;
import com.spring.github.entity.CustomActor;
import com.spring.github.entity.EventEntity;
import com.spring.github.entity.RepoEntity;
import com.spring.github.model.Actor;
import com.spring.github.model.Event;
import com.spring.github.model.Repo;
import com.spring.github.repository.ActorRepository;
import com.spring.github.repository.EventRepository;
import com.spring.github.repository.RepoRepository;

@Service
public class GithubApiRestService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private ActorRepository actorRepository;
	@Autowired
	private RepoRepository repoRepository;

	public String save(Event event) {

		ActorEntity a = new ActorEntity(event.getActor().getId(), event.getActor().getLogin(),
				event.getActor().getAvatar());
		RepoEntity r = new RepoEntity(event.getRepo().getId(), event.getRepo().getName(), event.getRepo().getUrl());
		EventEntity e = null;
		if (null == eventRepository.findOne(event.getId())) {
			e = eventRepository.save(new EventEntity(event.getId(), event.getType(), actorRepository.save(a),
					repoRepository.save(r), event.getCreatedAt()));
		}
		if (null != e) {
			return "Save successful.";
		} else
			return null;
	}

	public void deleteAll() {

		eventRepository.deleteAll();
		actorRepository.deleteAll();
		repoRepository.deleteAll();
	}

	public List<Event> getAll() {
		List<EventEntity> eventEntities = eventRepository.findAllByOrderByIdAsc();
		List<Event> events = new ArrayList<Event>();
		eventEntities.stream().forEach(entity -> {
			events.add(new Event(entity.getId(), entity.getType(),
					new Actor(entity.getActor().getId(), entity.getActor().getLogin(), entity.getActor().getAvatar()),
					new Repo(entity.getRepo().getId(), entity.getRepo().getName(), entity.getRepo().getUrl()),
					entity.getCreatedAt()));
		});
		return events;
	}

	public List<Event> getAllEventsByActors(Long actorId) {
		ActorEntity actor = actorRepository.findOne(actorId);
		if (Objects.isNull(actor))
			return null;
		List<EventEntity> eventEntities = eventRepository.findByActorIdOrderByIdAsc(actorId);
		List<Event> events = new ArrayList<Event>();
		eventEntities.stream().forEach(entity -> {
			events.add(new Event(entity.getId(), entity.getType(),
					new Actor(entity.getActor().getId(), entity.getActor().getLogin(), entity.getActor().getAvatar()),
					new Repo(entity.getRepo().getId(), entity.getRepo().getName(), entity.getRepo().getUrl()),
					entity.getCreatedAt()));
		});
		return events;
	}

	public int updateAvatarUrl(Actor actor) {
		ActorEntity found = actorRepository.findOne(actor.getId());
		if (Objects.isNull(found))
			return 0;
		if (!found.getLogin().equals(actor.getLogin()))
			return 1;
		found.setAvatar(actor.getAvatar());
		actorRepository.saveAndFlush(found);
		return 2;
	}

	public List<Actor> getActorsByEventsCount() {
		List<Object[]> actorEntities = eventRepository.findByActorOrderByCountDesc();
		List<Actor> actors = new ArrayList<>();
		List<CustomActor> customActors = new ArrayList<CustomActor>();
		actorEntities.stream().forEach(actor -> {

			BigInteger b = (BigInteger) actor[0];
			BigInteger count = (BigInteger) actor[1];
			ActorEntity entity = actorRepository.findOne(b.longValue());
			List<EventEntity> events = eventRepository.findByActorIdOrderByIdAsc(b.longValue());

			events.sort(new Comparator<EventEntity>() {
				@Override
				public int compare(final EventEntity e1, final EventEntity e2) {
					return e2.getCreatedAt().compareTo(e1.getCreatedAt());
				}
			});

			CustomActor c = new CustomActor(b.longValue(), events.get(0).getCreatedAt(), entity.getLogin(), entity,
					count.intValue());
			customActors.add(c);
		});

		customActors.sort(new Comparator<CustomActor>() {
			@Override
			public int compare(final CustomActor e1, final CustomActor e2) {
				int c = 0;
				if (e1.getCount() == e2.getCount()) {
					c = e2.getCreatedAt().compareTo(e1.getCreatedAt());
					if (c == 0)
						c = e1.getLogin().compareTo(e2.getLogin());
				}
				return c;
			}
		});
		customActors.stream().forEach(ca -> {
			actors.add(new Actor(ca.getActorId(), ca.getLogin(), ca.getActor().getAvatar()));
		});

		return actors;

	}

	public List<Actor> getActorsByEventsStreak() {
		List<ActorEntity> actorEntities = actorRepository.findAll();
		Map<Long, Integer> actorStreakMap = new HashMap<Long, Integer>();
		List<CustomActor> finalList = new ArrayList<>();
		actorEntities.stream().forEach(actorEntity -> {
			int streak = 0;
			actorStreakMap.put(actorEntity.getId(), 0);
			List<EventEntity> eventEntities = eventRepository.findByActorIdOrderByCreatedAtDesc(actorEntity.getId());
			List<Timestamp> timestamps = eventEntities.stream().map(EventEntity::getCreatedAt)
					.collect(Collectors.toList());

			Timestamp prev = timestamps.get(0);
			for (int i = 1; i < timestamps.size(); i++) {
				Timestamp next = timestamps.get(i);
				
				SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				Date dPrev= new Date();
				Date dNext= new Date();
				try {
					dPrev= myFormat.parse(prev.toString().substring(0, prev.toString().indexOf(' ')));
					dNext=myFormat.parse(next.toString().substring(0, next.toString().indexOf(' ')));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if (TimeUnit.DAYS.convert((dPrev.getTime()-dNext.getTime()), TimeUnit.MILLISECONDS) == 1) {
					streak++;
				} else {
					streak = 0;
				}
				prev = next;
			}
			actorStreakMap.put(actorEntity.getId(), streak + 1);
			finalList.add(new CustomActor(actorEntity.getId(), eventEntities.get(0).getCreatedAt(),
					actorEntity.getLogin(), actorEntity, streak + 1));
		});

		finalList.sort(new Comparator<CustomActor>() {
			@Override
			public int compare(final CustomActor e1, final CustomActor e2) {
				return Integer.valueOf(e2.getCount()).compareTo(Integer.valueOf(e1.getCount()));
			}
		});

		finalList.sort(new Comparator<CustomActor>() {
			@Override
			public int compare(final CustomActor e1, final CustomActor e2) {
				int c = 0;
				if (e1.getCount() == e2.getCount()) {
					c = e2.getCreatedAt().compareTo(e1.getCreatedAt());
					if (c == 0)
						c = e1.getLogin().compareTo(e2.getLogin());
				}
				return c;
			}
		});

		List<Actor> actors = new ArrayList<>();
		finalList.stream().forEach(ca -> {
			actors.add(new Actor(ca.getActorId(), ca.getLogin(), ca.getActor().getAvatar()));
		});
		return actors;

	}
}
