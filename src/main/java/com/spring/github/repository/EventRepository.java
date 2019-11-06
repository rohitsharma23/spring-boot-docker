package com.spring.github.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.github.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
	public List<EventEntity> findAllByOrderByIdAsc();

	public List<EventEntity> findByActorIdOrderByIdAsc(Long actor);
	public List<EventEntity> findByActorIdOrderByCreatedAtDesc(Long actor);

	@Query(value = "SELECT DISTINCT actor_id, COUNT(*) FROM Event GROUP BY actor_id ORDER BY COUNT(*) DESC", nativeQuery = true)
	List<Object[]> findByActorOrderByCountDesc();
}
