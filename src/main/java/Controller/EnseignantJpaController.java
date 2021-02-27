/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.IllegalOrphanException;
import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gestbiblio.gestbiblio.Entité.Adherent;
import gestbiblio.gestbiblio.Entité.Enseignant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hp
 */
public class EnseignantJpaController implements Serializable {

    public EnseignantJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Enseignant enseignant) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Adherent adherentOrphanCheck = enseignant.getAdherent();
        if (adherentOrphanCheck != null) {
            Enseignant oldEnseignantOfAdherent = adherentOrphanCheck.getEnseignant();
            if (oldEnseignantOfAdherent != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Adherent " + adherentOrphanCheck + " already has an item of type Enseignant whose adherent column cannot be null. Please make another selection for the adherent field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adherent adherent = enseignant.getAdherent();
            if (adherent != null) {
                adherent = em.getReference(adherent.getClass(), adherent.getIdAdh());
                enseignant.setAdherent(adherent);
            }
            em.persist(enseignant);
            if (adherent != null) {
                adherent.setEnseignant(enseignant);
                adherent = em.merge(adherent);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEnseignant(enseignant.getIdAdh()) != null) {
                throw new PreexistingEntityException("Enseignant " + enseignant + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Enseignant enseignant) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Enseignant persistentEnseignant = em.find(Enseignant.class, enseignant.getIdAdh());
            Adherent adherentOld = persistentEnseignant.getAdherent();
            Adherent adherentNew = enseignant.getAdherent();
            List<String> illegalOrphanMessages = null;
            if (adherentNew != null && !adherentNew.equals(adherentOld)) {
                Enseignant oldEnseignantOfAdherent = adherentNew.getEnseignant();
                if (oldEnseignantOfAdherent != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Adherent " + adherentNew + " already has an item of type Enseignant whose adherent column cannot be null. Please make another selection for the adherent field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (adherentNew != null) {
                adherentNew = em.getReference(adherentNew.getClass(), adherentNew.getIdAdh());
                enseignant.setAdherent(adherentNew);
            }
            enseignant = em.merge(enseignant);
            if (adherentOld != null && !adherentOld.equals(adherentNew)) {
                adherentOld.setEnseignant(null);
                adherentOld = em.merge(adherentOld);
            }
            if (adherentNew != null && !adherentNew.equals(adherentOld)) {
                adherentNew.setEnseignant(enseignant);
                adherentNew = em.merge(adherentNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = enseignant.getIdAdh();
                if (findEnseignant(id) == null) {
                    throw new NonexistentEntityException("The enseignant with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Enseignant enseignant;
            try {
                enseignant = em.getReference(Enseignant.class, id);
                enseignant.getIdAdh();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enseignant with id " + id + " no longer exists.", enfe);
            }
            Adherent adherent = enseignant.getAdherent();
            if (adherent != null) {
                adherent.setEnseignant(null);
                adherent = em.merge(adherent);
            }
            em.remove(enseignant);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Enseignant> findEnseignantEntities() {
        return findEnseignantEntities(true, -1, -1);
    }

    public List<Enseignant> findEnseignantEntities(int maxResults, int firstResult) {
        return findEnseignantEntities(false, maxResults, firstResult);
    }

    private List<Enseignant> findEnseignantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Enseignant.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Enseignant findEnseignant(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Enseignant.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnseignantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Enseignant> rt = cq.from(Enseignant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
