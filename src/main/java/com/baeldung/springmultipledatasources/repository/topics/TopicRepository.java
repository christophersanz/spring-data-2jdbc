package com.baeldung.springmultipledatasources.repository.topics;

import com.baeldung.springmultipledatasources.model.topics.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}