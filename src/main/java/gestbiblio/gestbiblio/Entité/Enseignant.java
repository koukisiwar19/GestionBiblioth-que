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
@Table(name = "enseignant")
@NamedQueries({
    @NamedQuery(name = "Enseignant.findAll", query = "SELECT e FROM Enseignant e"),
    @NamedQuery(name = "Enseignant.findByIdAdh", query = "SELECT e FROM Enseignant e WHERE e.idAdh = :idAdh"),
    @NamedQuery(name = "Enseignant.findByGrade", query = "SELECT e FROM Enseignant e WHERE e.grade = :grade")})
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idAdh")
    private Integer idAdh;
    @Basic(optional = false)
    @Column(name = "grade")
    private String grade;
    @JoinColumn(name = "idAdh", referencedColumnName = "idAdh", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Adherent adherent;

    public Enseignant() {
    }

    public Enseignant(Integer idAdh) {
        this.idAdh = idAdh;
    }

    public Enseignant(Integer idAdh, String grade) {
        this.idAdh = idAdh;
        this.grade = grade;
    }

    public Integer getIdAdh() {
        return idAdh;
    }

    public void setIdAdh(Integer idAdh) {
        this.idAdh = idAdh;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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
        if (!(object instanceof Enseignant)) {
            return false;
        }
        Enseignant other = (Enseignant) object;
        if ((this.idAdh == null && other.idAdh != null) || (this.idAdh != null && !this.idAdh.equals(other.idAdh))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestbiblio.gestbiblio.Entit\u00e9.Enseignant[ idAdh=" + idAdh + " ]";
    }
    
}
