package sec.project.repository;

import sec.project.domain.Signup;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class SignupRepositoryImpl implements SignupRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Signup> findByOwner(String owner) {

        /*
         * @todo A1-Injection.
         * @todo Untrusted data can be sent as part of a query
         */
        TypedQuery<Signup> query =
                em.createQuery("SELECT s FROM Signup s WHERE s.owner = '"+owner+"'", Signup.class);
        return query.getResultList();
    }
}