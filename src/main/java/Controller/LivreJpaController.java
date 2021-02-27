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
import gestbiblio.gestbiblio.Entité.Emprunt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import gestbiblio.gestbiblio.Entité.Livre;

/**
 *
 * @author hp
 */
public class LivreJpaController implements Serializable {
 private EntityManagerFactory emf = null;
       
   
    public LivreJpaController(EntityManagerFactory mf) {
        this.emf = mf;
    }


    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Livre livre) throws PreexistingEntityException,Exception {
      
         
       EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(livre);
            em.getTransaction().commit();
        } catch (Exception ex) {
                if(findLivre (livre.getIdLivre())!= null)
                {       
                throw new PreexistingEntityException("Livre " + livre + " already exists.", ex);
            }
   
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


  

    public void edit(Livre livre) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livre persistentLivre = em.find(Livre.class, livre.getIdLivre());
            Emprunt empruntOld = persistentLivre.getEmprunt();
            Emprunt empruntNew = livre.getEmprunt();
            Collection<Emprunt> empruntCollectionOld = persistentLivre.getEmpruntCollection();
            Collection<Emprunt> empruntCollectionNew = livre.getEmpruntCollection();
            List<String> illegalOrphanMessages = null;
            if (empruntOld != null && !empruntOld.equals(empruntNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Emprunt " + empruntOld + " since its livre field is not nullable.");
            }
            if (empruntNew != null && !empruntNew.equals(empruntOld)) {
                Livre oldLivreOfEmprunt = empruntNew.getLivre();
                if (oldLivreOfEmprunt != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Emprunt " + empruntNew + " already has an item of type Livre whose emprunt column cannot be null. Please make another selection for the emprunt field.");
                }
            }
            for (Emprunt empruntCollectionOldEmprunt : empruntCollectionOld) {
                if (!empruntCollectionNew.contains(empruntCollectionOldEmprunt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emprunt " + empruntCollectionOldEmprunt + " since its livre field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empruntNew != null) {
                empruntNew = em.getReference(empruntNew.getClass(), empruntNew.getEmpruntPK());
                livre.setEmprunt(empruntNew);
            }
            Collection<Emprunt> attachedEmpruntCollectionNew = new ArrayList<Emprunt>();
            if(empruntCollectionNew != null ){
                for (Emprunt empruntCollectionNewEmpruntToAttach : empruntCollectionNew) {
                    empruntCollectionNewEmpruntToAttach = em.getReference(empruntCollectionNewEmpruntToAttach.getClass(), empruntCollectionNewEmpruntToAttach.getEmpruntPK());
                    attachedEmpruntCollectionNew.add(empruntCollectionNewEmpruntToAttach);
                }
            }
            empruntCollectionNew = attachedEmpruntCollectionNew;
            livre.setEmpruntCollection(empruntCollectionNew);
            livre = em.merge(livre);
            if (empruntNew != null && !empruntNew.equals(empruntOld)) {
                empruntNew.setLivre(livre);
                empruntNew = em.merge(empruntNew);
            }
            for (Emprunt empruntCollectionNewEmprunt : empruntCollectionNew) {
                if (!empruntCollectionOld.contains(empruntCollectionNewEmprunt)) {
                    Livre oldLivreOfEmpruntCollectionNewEmprunt = empruntCollectionNewEmprunt.getLivre();
                    empruntCollectionNewEmprunt.setLivre(livre);
                    empruntCollectionNewEmprunt = em.merge(empruntCollectionNewEmprunt);
                    if (oldLivreOfEmpruntCollectionNewEmprunt != null && !oldLivreOfEmpruntCollectionNewEmprunt.equals(livre)) {
                        oldLivreOfEmpruntCollectionNewEmprunt.getEmpruntCollection().remove(empruntCollectionNewEmprunt);
                        oldLivreOfEmpruntCollectionNewEmprunt = em.merge(oldLivreOfEmpruntCollectionNewEmprunt);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = livre.getIdLivre();
                if (findLivre(id) == null) {
                    throw new NonexistentEntityException("The livre with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livre livre;
            try {
                livre = em.getReference(Livre.class, id);
                livre.getIdLivre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The livre with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Emprunt empruntOrphanCheck = livre.getEmprunt();
            if (empruntOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Livre (" + livre + ") cannot be destroyed since the Emprunt " + empruntOrphanCheck + " in its emprunt field has a non-nullable livre field.");
            }
            Collection<Emprunt> empruntCollectionOrphanCheck = livre.getEmpruntCollection();
            for (Emprunt empruntCollectionOrphanCheckEmprunt : empruntCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Livre (" + livre + ") cannot be destroyed since the Emprunt " + empruntCollectionOrphanCheckEmprunt + " in its empruntCollection field has a non-nullable livre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(livre);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Livre> findLivreEntities() {
        return findLivreEntities(true, -1, -1);
    }

    public List<Livre> findLivreEntities(int maxResults, int firstResult) {
        return findLivreEntities(false, maxResults, firstResult);
    }

    private List<Livre> findLivreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Livre.class));
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

    public Livre findLivre(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Livre.class, id);
        } finally {
            em.close();
        }
    }

    public int getLivreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Livre> rt = cq.from(Livre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
