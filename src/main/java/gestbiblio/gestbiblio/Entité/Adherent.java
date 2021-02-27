/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestbiblio.gestbiblio.Entité;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "adherent")
@NamedQueries({
    @NamedQuery(name = "Adherent.findAll", query = "SELECT a FROM Adherent a"),
    @NamedQuery(name = "Adherent.findByIdAdh", query = "SELECT a FROM Adherent a WHERE a.idAdh = :idAdh"),
    @NamedQuery(name = "Adherent.findBySexe", query = "SELECT a FROM Adherent a WHERE a.sexe = :sexe"),
    @NamedQuery(name = "Adherent.findByNom", query = "SELECT a FROM Adherent a WHERE a.nom = :nom"),
    @NamedQuery(name = "Adherent.findByPrenom", query = "SELECT a FROM Adherent a WHERE a.prenom = :prenom"),
    @NamedQuery(name = "Adherent.findByAdresse", query = "SELECT a FROM Adherent a WHERE a.adresse = :adresse"),
    @NamedQuery(name = "Adherent.findByTelephone", query = "SELECT a FROM Adherent a WHERE a.telephone = :telephone"),
    @NamedQuery(name = "Adherent.findByEmail", query = "SELECT a FROM Adherent a WHERE a.email = :email")})
public class Adherent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAdh")
    private Integer idAdh;
    @Basic(optional = false)
    @Column(name = "sexe")
    private String sexe;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "prenom")
    private String prenom;
    @Basic(optional = false)
    @Column(name = "adresse")
    private String adresse;
    @Basic(optional = false)
    @Column(name = "telephone")
    private int telephone;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    /*@JoinColumn(name = "idAdh", referencedColumnName = "idAdh", insertable = false, updatable = false)*/
    //@JoinColumn(name = "idAdh", referencedColumnName = "idAdh", insertable = false, updatable = false)
   // @OneToOne(optional = false)
    //private Emprunt emprunt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "adherent1")
    private Collection<Emprunt> empruntCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "adherent")
    private Enseignant enseignant;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "adherent")
    private Etudiant etudiant;

    public Adherent() {
    }

    public Adherent(Integer idAdh) {
        this.idAdh = idAdh;
    }

    public Adherent(Integer idAdh, String sexe, String nom, String prenom, String adresse, int telephone, String email) {
        this.idAdh = idAdh;
        this.sexe = sexe;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
    }

    public Adherent(String sexe, String nom, String prenom, String adresse, int telephone, String email) {
        this.sexe = sexe;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
    }

    /*public Adherent(String nom, String prenom, String sexe, String adresse, int telephone, String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    public Integer getIdAdh() {
        return idAdh;
    }

    public void setIdAdh(Integer idAdh) {
        this.idAdh = idAdh;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
    public Collection<Emprunt> getEmpruntCollection() {
        return empruntCollection;
    }

    public void setEmpruntCollection(Collection<Emprunt> empruntCollection) {
        this.empruntCollection = empruntCollection;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAdh != null ? idAdh.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adherent)) {
            return false;
        }
        Adherent other = (Adherent) object;
        if ((this.idAdh == null && other.idAdh != null) || (this.idAdh != null && !this.idAdh.equals(other.idAdh))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestbiblio.gestbiblio.Entit\u00e9.Adherent[ idAdh=" + idAdh + " ]";
    }
    
}
