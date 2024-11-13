package com.epam.resource.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ResourceRepositoryImpl implements CustomResourceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> deleteAllByIdInReturnIds(List<Long> ids) {
        // Check which IDs exist
        List<Long> existingIds = entityManager.createQuery(
                        "SELECT r.id FROM Resource r WHERE r.id IN :ids", Long.class)
                .setParameter("ids", ids)
                .getResultList();

        if (!existingIds.isEmpty()) {
            // Delete entities by existing IDs
            Query deleteQuery = entityManager.createQuery(
                            "DELETE FROM Resource r WHERE r.id IN :ids")
                    .setParameter("ids", existingIds);
            deleteQuery.executeUpdate();
        }

        // Return the IDs of deleted entities
        return existingIds;
    }
}