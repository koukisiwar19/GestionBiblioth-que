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
import gestbiblio.gestbiblio.Entité.Etudiant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hp
 */
public class EtudiantJpaController implements Serializable {

    public EtudiantJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Etudiant etudiant) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Adherent adherentOrphanCheck = etudiant.getAdherent();
        if (adherentOrphanCheck != null) {
            Etudiant oldEtudiantOfAdherent = adherentOrphanCheck.getEtudiant();
            if (oldEtudiantOfAdherent != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Adherent " + adherentOrphanCheck + " already has an item of type Etudiant whose adherent column cannot be null. Please make another selection for the adherent field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adherent adherent = etudiant.getAdherent();
            if (adherent != null) {
                adherent = em.getReference(adherent.getClass(), adherent.getIdAdh());
                etudiant.setAdherent(adherent);
            }
            em.persist(etudiant);
            if (adherent != null) {
                adherent.setEtudiant(etudiant);
                adherent = em.merge(adherent);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEtudiant(etudiant.getIdAdh()) != null) {
                throw new PreexistingEntityException("Etudiant " + etudiant + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Etudiant etudiant) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Etudiant persistentEtudiant = em.find(Etudiant.class, etudiant.getIdAdh());
            Adherent adherentOld = persistentEtudiant.getAdherent();
            Adherent adherentNew = etudiant.getAdherent();
            List<String> illegalOrphanMessages = null;
            if (adherentNew != null && !adherentNew.equals(adherentOld)) {
                Etudiant oldEtudiantOfAdherent = adherentNew.getEtudiant();
                if (oldEtudiantOfAdherent != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Adherent " + adherentNew + " already has an item of type Etudiant whose adherent column cannot be null. Please make another selection for the adherent field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (adherentNew != null) {
                adherentNew = em.getReference(adherentNew.getClass(), adherentNew.getIdAdh());
                etudiant.setAdherent(adherentNew);
            }
            etudiant = em.merge(etudiant);
            if (adherentOld != null && !adherentOld.equals(adherentNew)) {
                adherentOld.setEtudiant(null);
                adherentOld = em.merge(adherentOld);
            }
            if (adherentNew != null && !adherentNew.equals(adherentOld)) {
                adherentNew.setEtudiant(etudiant);
                adherentNew = em.merge(adherentNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = etudiant.getIdAdh();
                if (findEtudiant(id) == null) {
                    throw new NonexistentEntityException("The etudiant with id " + id + " no longer exists.");
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
             Etudiant etudiant;
           try {
                etudiant = em.getReference(Etudiant.class, id);
                etudiant.getIdAdh();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The etudiant with id " + id + " no longer exists.", enfe);
            }
          
            Adherent adherent = etudiant.getAdherent();
            if (adherent != null) {
                adherent.setEtudiant(null);
                adherent = em.merge(adherent);
            }
            em.remove(etudiant);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Etudiant> findEtudiantEntities() {
        return findEtudiantEntities(true, -1, -1);
    }

    public List<Etudiant> findEtudiantEntities(int maxResults, int firstResult) {
        return findEtudiantEntities(false, maxResults, firstResult);
    }

    private List<Etudiant> findEtudiantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Etudiant.class));
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

    public Etudiant findEtudiant(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Etudiant.class, id);
        } finally {
            em.close();
        }
    }

    public int getEtudiantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Etudiant> rt = cq.from(Etudiant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
