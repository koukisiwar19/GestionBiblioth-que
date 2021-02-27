/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestbiblio.gestbiblio.Entité;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "etudiant")
@NamedQueries({
    @NamedQuery(name = "Etudiant.findAll", query = "SELECT e FROM Etudiant e"),
    @NamedQuery(name = "Etudiant.findByIdAdh", query = "SELECT e FROM Etudiant e WHERE e.idAdh = :idAdh"),
    @NamedQuery(name = "Etudiant.findByClasse", query = "SELECT e FROM Etudiant e WHERE e.classe = :classe")})
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idAdh")
    private Integer idAdh;
    @Basic(optional = false)
    @Column(name = "classe")
    private String classe;
    @JoinColumn(name = "idAdh", referencedColumnName = "idAdh", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Adherent adherent;

    public Etudiant() {
    }

    public Etudiant(Integer idAdh) {
        this.idAdh = idAdh;
    }

    public Etudiant(Integer idAdh, String classe) {
        this.idAdh = idAdh;
        this.classe = classe;
    }

    public Integer getIdAdh() {
        return idAdh;
    }

    public void setIdAdh(Integer idAdh) {
        this.idAdh = idAdh;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
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
        if (!(object instanceof Etudiant)) {
            return false;
        }
        Etudiant other = (Etudiant) object;
        if ((this.idAdh == null && other.idAdh != null) || (this.idAdh != null && !this.idAdh.equals(other.idAdh))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestbiblio.gestbiblio.Entit\u00e9.Etudiant[ idAdh=" + idAdh + " ]";
    }
    
}
