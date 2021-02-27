/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestbiblio.gestbiblio.Entité;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "emprunt")
@NamedQueries({
    @NamedQuery(name = "Emprunt.findAll", query = "SELECT e FROM Emprunt e"),
    @NamedQuery(name = "Emprunt.findByIdAdh", query = "SELECT e FROM Emprunt e WHERE e.empruntPK.idAdh = :idAdh"),
    @NamedQuery(name = "Emprunt.findByIdLivre", query = "SELECT e FROM Emprunt e WHERE e.empruntPK.idLivre = :idLivre"),
    @NamedQuery(name = "Emprunt.findByDateemp", query = "SELECT e FROM Emprunt e WHERE e.dateemp = :dateemp"),
    @NamedQuery(name = "Emprunt.findByDateretour", query = "SELECT e FROM Emprunt e WHERE e.dateretour = :dateretour")})
public class Emprunt implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmpruntPK empruntPK;
    @Basic(optional = false)
    @Column(name = "dateemp")
    @Temporal(TemporalType.DATE)
    private Date dateemp;
    @Basic(optional = false)
    @Column(name = "dateretour")
    @Temporal(TemporalType.DATE)
    private Date dateretour;
    //@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "emprunt")
    @JoinColumn(name = "idAdh", referencedColumnName = "idAdh", insertable = false, updatable = false)
    @ManyToOne
    private Adherent adherent;
    @JoinColumn(name = "idAdh", referencedColumnName = "idAdh", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Adherent adherent1;
    @JoinColumn(name = "idLivre", referencedColumnName = "idLivre", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Livre livre;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "emprunt")
    private Livre livre1;

    public Emprunt() {
    }

    public Emprunt(EmpruntPK empruntPK) {
        this.empruntPK = empruntPK;
    }

    public Emprunt(EmpruntPK empruntPK, Date dateemp, Date dateretour) {
        this.empruntPK = empruntPK;
        this.dateemp = dateemp;
        this.dateretour = dateretour;
    }

    public Emprunt(int idAdh, int idLivre) {
        this.empruntPK = new EmpruntPK(idAdh, idLivre);
    }

    public EmpruntPK getEmpruntPK() {
        return empruntPK;
    }

    public void setEmpruntPK(EmpruntPK empruntPK) {
        this.empruntPK = empruntPK;
    }

    public Date getDateemp() {
        return dateemp;
    }

    public void setDateemp(Date dateemp) {
        this.dateemp = dateemp;
    }

    public Date getDateretour() {
        return dateretour;
    }

    public void setDateretour(Date dateretour) {
        this.dateretour = dateretour;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public Adherent getAdherent1() {
        return adherent1;
    }

    public void setAdherent1(Adherent adherent1) {
        this.adherent1 = adherent1;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Livre getLivre1() {
        return livre1;
    }

    public void setLivre1(Livre livre1) {
        this.livre1 = livre1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empruntPK != null ? empruntPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emprunt)) {
            return false;
        }
        Emprunt other = (Emprunt) object;
        if ((this.empruntPK == null && other.empruntPK != null) || (this.empruntPK != null && !this.empruntPK.equals(other.empruntPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestbiblio.gestbiblio.Entit\u00e9.Emprunt[ empruntPK=" + empruntPK + "date emprunt"+ this.dateemp + " date retour " + this.dateretour+"]";
    }
    
}
