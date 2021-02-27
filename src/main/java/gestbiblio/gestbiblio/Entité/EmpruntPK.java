/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestbiblio.gestbiblio.Entité;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author hp
 */
@Embeddable
public class EmpruntPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idAdh")
    private int idAdh;
    @Basic(optional = false)
    @Column(name = "idLivre")
    private int idLivre;

    public EmpruntPK() {
    }

    public EmpruntPK(int idAdh, int idLivre) {
        this.idAdh = idAdh;
        this.idLivre = idLivre;
    }

    public int getIdAdh() {
        return idAdh;
    }

    public void setIdAdh(int idAdh) {
        this.idAdh = idAdh;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAdh;
        hash += (int) idLivre;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpruntPK)) {
            return false;
        }
        EmpruntPK other = (EmpruntPK) object;
        if (this.idAdh != other.idAdh) {
            return false;
        }
        if (this.idLivre != other.idLivre) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestbiblio.gestbiblio.Entit\u00e9.EmpruntPK[ idAdh=" + idAdh + ", idLivre=" + idLivre + " ]";
    }
    
}
