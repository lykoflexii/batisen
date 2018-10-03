package com.gesbtp.atos.aop.entreprise;

import com.gesbtp.atos.security.SecurityUtils;
import com.gesbtp.atos.repository.UserRepository;
import com.gesbtp.atos.domain.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Filter;
import java.util.Optional;

@Aspect
@Component
public class EntrepriseAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private final String fieldName =  "entrepriseId";

    private final Logger log = LoggerFactory.getLogger(EntrepriseAspect.class);

    /**
     * Run method if User service is hit.
     * Filter users based on which entreprise the user is associated with.
     * Skip filter if user has no entreprise
     */
    @Before("execution(* com.gesbtp.atos.service.UserService.*(..))")
    public void beforeExecution() throws Throwable {
        Optional<String> login = SecurityUtils.getCurrentUserLogin();

        if(login.isPresent()) {
			User user = userRepository.findOneByLogin(login.get()).get();

			if (user.getEntreprise() != null) {
				Filter filter = entityManager.unwrap(Session.class).enableFilter("ENTREPRISE_FILTER");
				filter.setParameter(fieldName, user.getEntreprise().getId());
			}
		}
    }
}
