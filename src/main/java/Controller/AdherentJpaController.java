/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.IllegalOrphanException;
import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import gestbiblio.gestbiblio.Entité.Adherent;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gestbiblio.gestbiblio.Entité.Emprunt;
import gestbiblio.gestbiblio.Entité.Enseignant;
import gestbiblio.gestbiblio.Entité.Etudiant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hp
 */
public class AdherentJpaController implements Serializable {

    public AdherentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Adherent adherent) throws PreexistingEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(adherent);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdherent(adherent.getIdAdh()) != null) {
                throw new PreexistingEntityException("Adherent " + adherent + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Adherent adherent) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adherent persistentAdherent = em.find(Adherent.class, adherent.getIdAdh());
           // Emprunt empruntOld = persistentAdherent.getEmprunt();
           // Emprunt empruntNew = adherent.getEmprunt();
            Enseignant enseignantOld = persistentAdherent.getEnseignant();
            Enseignant enseignantNew = adherent.getEnseignant();
            Etudiant etudiantOld = persistentAdherent.getEtudiant();
            Etudiant etudiantNew = adherent.getEtudiant();
            Collection<Emprunt> empruntCollectionOld = persistentAdherent.getEmpruntCollection();
            Collection<Emprunt> empruntCollectionNew = adherent.getEmpruntCollection();
            List<String> illegalOrphanMessages = null;
           /* if (empruntNew != null && !empruntNew.equals(empruntOld)) {
                Adherent oldAdherentOfEmprunt = empruntNew.getAdherent();
                if (oldAdherentOfEmprunt != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Emprunt " + empruntNew + " already has an item of type Adherent whose emprunt column cannot be null. Please make another selection for the emprunt field.");
                }
            }*/
            if (enseignantOld != null && !enseignantOld.equals(enseignantNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Enseignant " + enseignantOld + " since its adherent field is not nullable.");
            }
            if (etudiantOld != null && !etudiantOld.equals(etudiantNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Etudiant " + etudiantOld + " since its adherent field is not nullable.");
            }
            for (Emprunt empruntCollectionOldEmprunt : empruntCollectionOld) {
                if (!empruntCollectionNew.contains(empruntCollectionOldEmprunt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emprunt " + empruntCollectionOldEmprunt + " since its adherent1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
           /* if (empruntNew != null) {
                empruntNew = em.getReference(empruntNew.getClass(), empruntNew.getEmpruntPK());
                adherent.setEmprunt(empruntNew);
            }*/
            if (enseignantNew != null) {
                enseignantNew = em.getReference(enseignantNew.getClass(), enseignantNew.getIdAdh());
                adherent.setEnseignant(enseignantNew);
            }
            if (etudiantNew != null) {
                etudiantNew = em.getReference(etudiantNew.getClass(), etudiantNew.getIdAdh());
                adherent.setEtudiant(etudiantNew);
            }
            Collection<Emprunt> attachedEmpruntCollectionNew = new ArrayList<Emprunt>();
            if(empruntCollectionNew != null){
                for (Emprunt empruntCollectionNewEmpruntToAttach : empruntCollectionNew) {
                    empruntCollectionNewEmpruntToAttach = em.getReference(empruntCollectionNewEmpruntToAttach.getClass(), empruntCollectionNewEmpruntToAttach.getEmpruntPK());
                    attachedEmpruntCollectionNew.add(empruntCollectionNewEmpruntToAttach);
                }
            }

            empruntCollectionNew = attachedEmpruntCollectionNew;
            adherent.setEmpruntCollection(empruntCollectionNew);
            adherent = em.merge(adherent);
/*            if (empruntOld != null && !empruntOld.equals(empruntNew)) {
                empruntOld.setAdherent(null);
                empruntOld = em.merge(empruntOld);
            }
            if (empruntNew != null && !empruntNew.equals(empruntOld)) {
                empruntNew.setAdherent(adherent);
                empruntNew = em.merge(empruntNew);
            }*/
            if (enseignantNew != null && !enseignantNew.equals(enseignantOld)) {
                Adherent oldAdherentOfEnseignant = enseignantNew.getAdherent();
                if (oldAdherentOfEnseignant != null) {
                    oldAdherentOfEnseignant.setEnseignant(null);
                    oldAdherentOfEnseignant = em.merge(oldAdherentOfEnseignant);
                }
                enseignantNew.setAdherent(adherent);
                enseignantNew = em.merge(enseignantNew);
            }
            if (etudiantNew != null && !etudiantNew.equals(etudiantOld)) {
                Adherent oldAdherentOfEtudiant = etudiantNew.getAdherent();
                if (oldAdherentOfEtudiant != null) {
                    oldAdherentOfEtudiant.setEtudiant(null);
                    oldAdherentOfEtudiant = em.merge(oldAdherentOfEtudiant);
                }
                etudiantNew.setAdherent(adherent);
                etudiantNew = em.merge(etudiantNew);
            }
            for (Emprunt empruntCollectionNewEmprunt : empruntCollectionNew) {
                if (!empruntCollectionOld.contains(empruntCollectionNewEmprunt)) {
                    Adherent oldAdherent1OfEmpruntCollectionNewEmprunt = empruntCollectionNewEmprunt.getAdherent1();
                    empruntCollectionNewEmprunt.setAdherent1(adherent);
                    empruntCollectionNewEmprunt = em.merge(empruntCollectionNewEmprunt);
                    if (oldAdherent1OfEmpruntCollectionNewEmprunt != null && !oldAdherent1OfEmpruntCollectionNewEmprunt.equals(adherent)) {
                        oldAdherent1OfEmpruntCollectionNewEmprunt.getEmpruntCollection().remove(empruntCollectionNewEmprunt);
                        oldAdherent1OfEmpruntCollectionNewEmprunt = em.merge(oldAdherent1OfEmpruntCollectionNewEmprunt);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = adherent.getIdAdh();
                if (findAdherent(id) == null) {
                    throw new NonexistentEntityException("The adherent with id " + id + " no longer exists.");
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
            Adherent adherent;
            try {
                adherent = em.getReference(Adherent.class, id);
                adherent.getIdAdh();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adherent with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Enseignant enseignantOrphanCheck = adherent.getEnseignant();
            if (enseignantOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Adherent (" + adherent + ") cannot be destroyed since the Enseignant " + enseignantOrphanCheck + " in its enseignant field has a non-nullable adherent field.");
            }
            Etudiant etudiantOrphanCheck = adherent.getEtudiant();
            if (etudiantOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Adherent (" + adherent + ") cannot be destroyed since the Etudiant " + etudiantOrphanCheck + " in its etudiant field has a non-nullable adherent field.");
            }
            Collection<Emprunt> empruntCollectionOrphanCheck = adherent.getEmpruntCollection();
            for (Emprunt empruntCollectionOrphanCheckEmprunt : empruntCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Adherent (" + adherent + ") cannot be destroyed since the Emprunt " + empruntCollectionOrphanCheckEmprunt + " in its empruntCollection field has a non-nullable adherent1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
          //  Emprunt emprunt = adherent.getEmprunt();
/*            if (emprunt != null) {
                emprunt.setAdherent(null);
                emprunt = em.merge(emprunt);
            }*/
            em.remove(adherent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Adherent> findAdherentEntities() {
        return findAdherentEntities(true, -1, -1);
    }

    public List<Adherent> findAdherentEntities(int maxResults, int firstResult) {
        return findAdherentEntities(false, maxResults, firstResult);
    }

    private List<Adherent> findAdherentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Adherent.class));
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

    public Adherent findAdherent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Adherent.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdherentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Adherent> rt = cq.from(Adherent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
