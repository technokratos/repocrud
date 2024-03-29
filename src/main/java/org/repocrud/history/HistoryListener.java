
package org.repocrud.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.time.ZonedDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.repocrud.config.SecurityUtils;
import org.repocrud.domain.CrudHistory;
import org.repocrud.domain.User;
import org.repocrud.repository.CrudHistoryRepository;
import org.repocrud.service.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class HistoryListener {

	@Autowired
	private CrudHistoryRepository repository;

	private Executor executor = Executors.newFixedThreadPool(4);

	private ThreadLocal<ObjectMapper>  objectMapperThreadLocal = new ThreadLocal<>();

	@PrePersist
	public void prePersist(Auditable ob) {
		User userDetails = (User) SecurityUtils.getUserDetails();
		ZonedDateTime now = ZonedDateTime.now();
		ob.createdBy = userDetails;
		ob.createdDate = now;
		ob.lastModifiedBy = userDetails;
		ob.lastModifiedDate = now;
		if (userDetails != null && userDetails.getCompany()!= null) {
			ob.setCompany(userDetails.getCompany());
		}
	}
	@PostPersist
	public void postPersist(Auditable ob) {
		User user = (User) SecurityUtils.getUserDetails();
		executor.execute(() -> {
			addHistory(CrudHistory.Operation.SAVE, user, ob, ob.getClass().getSimpleName());
		});

	}
	@PostLoad
	public void postLoad(Auditable ob) {

	}	
	@PreUpdate
	public void preUpdate(Auditable ob) {
		ob.lastModifiedBy = (User) SecurityUtils.getUserDetails();
		ob.lastModifiedDate = ZonedDateTime.now();
	}
	@PostUpdate
	public void postUpdate(Auditable ob) {
		User user = (User) SecurityUtils.getUserDetails();
		executor.execute(() -> {
			addHistory(CrudHistory.Operation.UPDATE, user, ob, ob.getClass().getSimpleName());
		});
	}
	@PreRemove
	public void preRemove(Auditable ob) {
		System.out.println("Listening Auditable Pre Remove : " );
	}

	@PostRemove
	public void postRemove(Auditable ob) {
		User user = (User) SecurityUtils.getUserDetails();
		executor.execute(() -> {
			addHistory(CrudHistory.Operation.DELETE, user, ob, ob.getClass().getSimpleName());
		});
	}


	public void addHistory(CrudHistory.Operation operation, User user, Object arg, String type) {
		String body = getBody(arg);
		CrudHistory crudHistory = new CrudHistory(null, user, ZonedDateTime.now(), type, operation,
				body);
		ApplicationContextProvider.getRepository(CrudHistory.class).saveAndFlush(crudHistory);
	}

	private String getBody(Object object) {

		ObjectMapper objectMapper = objectMapperThreadLocal.get();
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.registerModule(new Hibernate5Module());
			objectMapperThreadLocal.set(objectMapper);
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		}
		try {

			String s = objectMapper.writeValueAsString(object);
			return (s.length() >= 1000) ?
					s.substring(0, 1000) :
					s;

		} catch (JsonProcessingException e) {
			log.error("Error in serializtion by jaxson ", e);
		}
		return "";
	}

} 