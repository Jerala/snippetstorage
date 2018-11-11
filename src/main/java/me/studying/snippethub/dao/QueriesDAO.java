package me.studying.snippethub.dao;

import me.studying.snippethub.entity.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class QueriesDAO {

    @Autowired
    private EntityManager entityManager;

    public List<Queries> getMostPopularQueries() {
        Query query = this.entityManager.createQuery(
                "SELECT e FROM " + Queries.class.getName() + " e order by e.counter desc ",
                Queries.class
        );
        query.setMaxResults(10);
        return query.getResultList();
    }
}
