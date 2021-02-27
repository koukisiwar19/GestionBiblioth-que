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
import gestbiblio.gestbiblio.Entité.Emprunt;
import gestbiblio.gestbiblio.Entité.EmpruntPK;
import gestbiblio.gestbiblio.Entité.Livre;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hp
 */
public class EmpruntJpaController implements Serializable {

    public EmpruntJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Emprunt emprunt) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (emprunt.getEmpruntPK() == null) {
            emprunt.setEmpruntPK(new EmpruntPK());
        }
        emprunt.getEmpruntPK().setIdAdh(emprunt.getAdherent1().getIdAdh());
        emprunt.getEmpruntPK().setIdLivre(emprunt.getLivre().getIdLivre());
        List<String> illegalOrphanMessages = null;
        Adherent adherent1OrphanCheck = emprunt.getAdherent1();
        if (adherent1OrphanCheck != null) {
          //  Emprunt oldEmpruntOfAdherent1 = adherent1OrphanCheck.getEmprunt();
           /* if (oldEmpruntOfAdherent1 != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Adherent " + adherent1OrphanCheck + " already has an item of type Emprunt whose adherent1 column cannot be null. Please make another selection for the adherent1 field.");
            }
        }*/
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adherent adherent = emprunt.getAdherent();
            if (adherent != null) {
                adherent = em.getReference(adherent.getClass(), adherent.getIdAdh());
                emprunt.setAdherent(adherent);
            }
            Adherent adherent1 = emprunt.getAdherent1();
            if (adherent1 != null) {
                adherent1 = em.getReference(adherent1.getClass(), adherent1.getIdAdh());
                emprunt.setAdherent1(adherent1);
            }
            Livre livre = emprunt.getLivre();
            if (livre != null) {
                livre = em.getReference(livre.getClass(), livre.getIdLivre());
                emprunt.setLivre(livre);
            }
            Livre livre1 = emprunt.getLivre1();
            if (livre1 != null) {
                livre1 = em.getReference(livre1.getClass(), livre1.getIdLivre());
                emprunt.setLivre1(livre1);
            }
            em.persist(emprunt);
           /* if (adherent != null) {
                Emprunt oldEmpruntOfAdherent = adherent.getEmprunt();
                if (oldEmpruntOfAdherent != null) {
                    oldEmpruntOfAdherent.setAdherent(null);
                    oldEmpruntOfAdherent = em.merge(oldEmpruntOfAdherent);
                }
                adherent.setEmprunt(emprunt);
                adherent = em.merge(adherent);
            }*/
           /* if (adherent1 != null) {
                adherent1.setEmprunt(emprunt);
                adherent1 = em.merge(adherent1);
            }*/
            if (livre != null) {
                livre.getEmpruntCollection().add(emprunt);
                livre = em.merge(livre);
            }
            if (livre1 != null) {
                Emprunt oldEmpruntOfLivre1 = livre1.getEmprunt();
                if (oldEmpruntOfLivre1 != null) {
                    oldEmpruntOfLivre1.setLivre1(null);
                    oldEmpruntOfLivre1 = em.merge(oldEmpruntOfLivre1);
                }
                livre1.setEmprunt(emprunt);
                livre1 = em.merge(livre1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmprunt(emprunt.getEmpruntPK()) != null) {
                throw new PreexistingEntityException("Emprunt " + emprunt + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        }
    }
        
  
    public void edit(Emprunt emprunt) throws IllegalOrphanException, NonexistentEntityException, Exception {
        emprunt.getEmpruntPK().setIdAdh(emprunt.getAdherent1().getIdAdh());
        emprunt.getEmpruntPK().setIdLivre(emprunt.getLivre().getIdLivre());
        EntityManager em = null;
        
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emprunt persistentEmprunt = em.find(Emprunt.class, emprunt.getEmpruntPK());
            Adherent adherentOld = persistentEmprunt.getAdherent();
            Adherent adherentNew = emprunt.getAdherent();
            Adherent adherent1Old = persistentEmprunt.getAdherent1();
            Adherent adherent1New = emprunt.getAdherent1();
            Livre livreOld = persistentEmprunt.getLivre();
            Livre livreNew = emprunt.getLivre();
            Livre livre1Old = persistentEmprunt.getLivre1();
            Livre livre1New = emprunt.getLivre1();
            List<String> illegalOrphanMessages = null;
            /*if (adherentOld != null && !adherentOld.equals(adherentNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Adherent " + adherentOld + " since its emprunt field is not nullable.");
            } 
            if (adherent1Old != null && !adherent1Old.equals(adherent1New)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Adherent " + adherent1Old + " since its emprunt field is not nullable.");
            }
            if (adherent1New != null && !adherent1New.equals(adherent1Old)) {
                Emprunt oldEmpruntOfAdherent1 = adherent1New.getEmprunt();
                if (oldEmpruntOfAdherent1 != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Adherent " + adherent1New + " already has an item of type Emprunt whose adherent1 column cannot be null. Please make another selection for the adherent1 field.");
                }
            }
            if (livre1Old != null && !livre1Old.equals(livre1New)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Livre " + livre1Old + " since its emprunt field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                //throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (adherentNew != null) {
                adherentNew = em.getReference(adherentNew.getClass(), adherentNew.getIdAdh());
                emprunt.setAdherent(adherentNew);
            }
            if (adherent1New != null) {
                adherent1New = em.getReference(adherent1New.getClass(), adherent1New.getIdAdh());
                emprunt.setAdherent1(adherent1New);
            }
            if (livreNew != null) {
                livreNew = em.getReference(livreNew.getClass(), livreNew.getIdLivre());
                emprunt.setLivre(livreNew);
            }
            if (livre1New != null) {
                livre1New = em.getReference(livre1New.getClass(), livre1New.getIdLivre());
                emprunt.setLivre1(livre1New);
            }*/
            System.out.println("1 : "+emprunt);
            
            emprunt = em.merge(emprunt);
            
            System.out.println("2 : "+emprunt);

            if (adherentNew != null && !adherentNew.equals(adherentOld)) {
               // Emprunt oldEmpruntOfAdherent = adherentNew.getEmprunt();
                /*if (oldEmpruntOfAdherent != null) {
                    oldEmpruntOfAdherent.setAdherent(null);
                    oldEmpruntOfAdherent = em.merge(oldEmpruntOfAdherent);
                }
                adherentNew.setEmprunt(emprunt);
                adherentNew = em.merge(adherentNew);
            }*/
            
                
            /*if (adherent1New != null && !adherent1New.equals(adherent1Old)) {
               // adherent1New.setEmprunt(emprunt);
                adherent1New = em.merge(adherent1New);
            }
            if (livreOld != null && !livreOld.equals(livreNew)) {
                livreOld.getEmpruntCollection().remove(emprunt);
                livreOld = em.merge(livreOld);
            }
            if (livreNew != null && !livreNew.equals(livreOld)) {
                livreNew.getEmpruntCollection().add(emprunt);
                livreNew = em.merge(livreNew);
            }
            if (livre1New != null && !livre1New.equals(livre1Old)) {
                Emprunt oldEmpruntOfLivre1 = livre1New.getEmprunt();
                if (oldEmpruntOfLivre1 != null) {
                    oldEmpruntOfLivre1.setLivre1(null);
                    oldEmpruntOfLivre1 = em.merge(oldEmpruntOfLivre1);
                }
                livre1New.setEmprunt(emprunt);
                livre1New = em.merge(livre1New);
            }*/
            
        }
                        em.getTransaction().commit();

        }catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EmpruntPK id = emprunt.getEmpruntPK();
                if (findEmprunt(id) == null) {
                    throw new NonexistentEntityException("The emprunt with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EmpruntPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emprunt emprunt;
            try {
                emprunt = em.getReference(Emprunt.class, id);
                emprunt.getEmpruntPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emprunt with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Adherent adherentOrphanCheck = emprunt.getAdherent();
            
            if (adherentOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Emprunt (" + emprunt + ") cannot be destroyed since the Adherent " + adherentOrphanCheck + " in its adherent field has a non-nullable emprunt field.");
            }
            Adherent adherent1OrphanCheck = emprunt.getAdherent1();
            if (adherent1OrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Emprunt (" + emprunt + ") cannot be destroyed since the Adherent " + adherent1OrphanCheck + " in its adherent1 field has a non-nullable emprunt field.");
            }
            Livre livre1OrphanCheck = emprunt.getLivre1();
            if (livre1OrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Emprunt (" + emprunt + ") cannot be destroyed since the Livre " + livre1OrphanCheck + " in its livre1 field has a non-nullable emprunt field.");
            }
            if (illegalOrphanMessages != null) {
                //throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Livre livre = emprunt.getLivre();
            if (livre != null) {
                //livre.getEmpruntCollection().remove(emprunt);
                //livre = em.merge(livre);
            }
            System.out.println(" I AM HERE");
            em.remove(emprunt);       
            System.out.println(emprunt);

            em.getTransaction().commit();
            
            System.out.println(" I AM HERE 3");

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Emprunt> findEmpruntEntities() {
        return findEmpruntEntities(true, -1, -1);
    }

    public List<Emprunt> findEmpruntEntities(int maxResults, int firstResult) {
        return findEmpruntEntities(false, maxResults, firstResult);
    }

    private List<Emprunt> findEmpruntEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Emprunt.class));
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

    public Emprunt findEmprunt(EmpruntPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Emprunt.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpruntCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Emprunt> rt = cq.from(Emprunt.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
